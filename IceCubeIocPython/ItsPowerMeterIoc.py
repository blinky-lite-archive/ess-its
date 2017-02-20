from IceCubePyClassIoc import GenericIOC
import usbtmc
import json
import time

class PowerMeterIOC(GenericIOC):
    def initialiseDevice(self):
        # Power meter initialization
        usbInst =  usbtmc.Instrument(2733, 27)

        usbCommand = "SYST:PRES"
        print "Sending " + usbCommand + " to device"
        usbInst.write(usbCommand)
        time.sleep(2)

        usbCommand = "*RCL 1"
        print "Sending " + usbCommand + " to device"
        usbInst.write(usbCommand)

        usbCommand = "INIT:ALL:CONT ON"
        print "Sending " + usbCommand + " to device"
        usbInst.write(usbCommand)

        self.usbInst = usbInst

    def getDataFromDevice(self):
 #       usbCommand = "SENS1:AVER:RES"
 #       print "Sending " + usbCommand + " to device"
 #       self.usbInst.write(usbCommand)
        usbCommand = "FETC1?"
        print "Sending " + usbCommand + " to device"
        power1 = self.usbInst.ask(usbCommand)
        print "Received " + power1 + " from device"
        power1f = float(power1) + 43.9
        power1 = str(power1f)
        time.sleep(2)
#        usbCommand = "SENS2:AVER:RES"
#        print "Sending " + usbCommand + " to device"
#        self.usbInst.write(usbCommand)
        usbCommand = "FETC2?"
        print "Sending " + usbCommand + " to device"
        power2 = self.usbInst.ask(usbCommand)
        print "Received " + power2 + " from device"
        power2f = float(power2) + 59.5 + 7.2
        power2 = str(power2f)
        data = {"power1": power1, "power2": power2}
        return json.dumps(data)

if __name__ == "__main__":
    def handleIncomingMessage(client, usbInst, msg):
        # handle messages from broker
        if "/set/rf" in msg.topic:
            i = 1

    itsPowerMeterIOC = PowerMeterIOC(brokerFile = 'itsmqttbroker.dat')

    itsPowerMeterIOC.handleIncomingMessage = handleIncomingMessage
    itsPowerMeterIOC.periodicPollPeriodSecs = 2
    itsPowerMeterIOC.mqttStart(
            clientId       = "itsPowerMeter01Ioc",
            subscribeTopic = "itsPowerMeter01/set/#",
            publishTopic   = "itsPowerMeter01/get")
    itsPowerMeterIOC.client.user_data_set(itsPowerMeterIOC.usbInst)

    itsPowerMeterIOC.run()
