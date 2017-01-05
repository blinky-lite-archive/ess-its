from se.esss.litterbox.icecube.ioc.python.IceCubePyClassIoc import GenericIOC
import usbtmc
import json
import time

class RfSigGenIOC(GenericIOC):
    def initialiseDevice(self):
        # Power meter initialization
        usbInst =  usbtmc.Instrument(2733, 72)

        usbCommand = "*RST"
        print "Sending " + usbCommand + " to device"
        usbInst.write(usbCommand)
        time.sleep(2)

        usbCommand = "FREQ 352.21MHZ"
        print "Sending " + usbCommand + " to device"
        usbInst.write(usbCommand)
        
        usbCommand = "POW -50"
        print "Sending " + usbCommand + " to device"
        usbInst.write(usbCommand)

        usbCommand = "OUTP OFF"
        print "Sending " + usbCommand + " to device"
        usbInst.write(usbCommand)

        usbCommand = "PULM:SOUR EXT"
        print "Sending " + usbCommand + " to device"
        usbInst.write(usbCommand)
        
        usbCommand = "SOUR:PULM:TRIG:EXT:IMP G10K"
        print "Sending " + usbCommand + " to device"
        usbInst.write(usbCommand)

        usbCommand = "PULM:STAT ON"
        print "Sending " + usbCommand + " to device"
        usbInst.write(usbCommand)

        self.usbInst = usbInst

if __name__ == "__main__":
    def handleIncomingMessage(client, usbInst, msg):
        # handle messages from broker
        if "/set/rf" in msg.topic:
            data = json.loads(str(msg.payload))

            usbCommand = "OUTP " + data['rfPowOn']
            print "Sending " + usbCommand + " to device"
            usbInst.write(usbCommand)

            usbCommand = "FREQ " + data['rfFreq'] + "MHZ"
            print "Sending " + usbCommand + " to device"
            usbInst.write(usbCommand)

            usbCommand = "POW " + data['rfPowLvl']
            print "Sending " + usbCommand + " to device"
            usbInst.write(usbCommand)

    itsRfSigGenIOC = RfSigGenIOC(brokerFile = 'itsmqttbroker.dat')

    itsRfSigGenIOC.handleIncomingMessage = handleIncomingMessage
    itsRfSigGenIOC.periodicPollPeriodSecs = 1
    itsRfSigGenIOC.mqttStart(
            clientId       = "itsRfSigGen01Ioc",
            subscribeTopic = "itsRfSigGen01/set/#",
            publishTopic   = "itsRfSigGen01/get")
    itsRfSigGenIOC.client.user_data_set(itsRfSigGenIOC.usbInst)

    itsRfSigGenIOC.run()

