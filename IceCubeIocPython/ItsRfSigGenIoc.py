from IceCubePyClassIoc import GenericIOC
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

        usbCommand = "FREQ 704.42MHZ"
        print "Sending " + usbCommand + " to device"
        usbInst.write(usbCommand)

        usbCommand = "POW -20"
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
        self.setJsonData = json.loads('{"rfFreq":"704.42","rfPowLvl":"-20","rfPowOn":"OFF"}')
#        print json.dumps(self.setJsonData)

if __name__ == "__main__":
    def handleIncomingMessage(client, usbInst, msg):
        # handle messages from broker
        if "/set/rf" in msg.topic:
            itsRfSigGenIOC.setJsonData = json.loads(str(msg.payload))
#            print json.dumps(itsRfSigGenIOC.setJsonData)

            usbCommand = "OUTP " + itsRfSigGenIOC.setJsonData['rfPowOn']
            print "Sending " + usbCommand + " to device"
            usbInst.write(usbCommand)

            usbCommand = "FREQ " + itsRfSigGenIOC.setJsonData['rfFreq'] + "MHZ"
            print "Sending " + usbCommand + " to device"
            usbInst.write(usbCommand)

            usbCommand = "POW " + itsRfSigGenIOC.setJsonData['rfPowLvl']
            print "Sending " + usbCommand + " to device"
            usbInst.write(usbCommand)

        if "/get/rf" in msg.topic:
            client.publish('itsRfSigGen01/echo/rf', bytearray(json.dumps(itsRfSigGenIOC.setJsonData)) ,0, False)
            print "Publishing: Topic = itsRfSigGen01/echo/rf   Data = " + json.dumps(itsRfSigGenIOC.setJsonData)	    

    itsRfSigGenIOC = RfSigGenIOC(brokerFile = 'itsmqttbroker.dat')

    itsRfSigGenIOC.handleIncomingMessage = handleIncomingMessage
    itsRfSigGenIOC.periodicPollPeriodSecs = 1
    itsRfSigGenIOC.mqttStart(
            clientId       = "itsRfSigGen01Ioc",
            subscribeTopic = "itsRfSigGen01/#",
            publishTopic   = "itsRfSigGen01/get")
    itsRfSigGenIOC.client.user_data_set(itsRfSigGenIOC.usbInst)

    itsRfSigGenIOC.run()
