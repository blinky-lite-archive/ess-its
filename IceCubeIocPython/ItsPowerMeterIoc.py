from IceCubePyClassIoc import GenericIOC
import usbtmc
import json
import time

class PowerMeter(object):
    def __init__(self, vendor_id, product_id):
        self.vendor_id = vendor_id
        self.product_id = product_id

    def connect(self):
        self.connection = usbtmc.Instrument(self.vendor_id, self.product_id)

    def write_to_device(self, command_string):
        print "Sending %s to device" % command_string
        self.connection.write(command_string)

    def request_from_device(self, request_string):
        print "Sending %s to device" % request_string
        power1 = float(self.connection.ask(request_string))
        print "Received %s from device" % power1
        return power1

    def preset(self):
        self.write_to_device("SYST:PRES")
        time.sleep(2)

    def recall_state(self, state_num):
        self.write_to_device("*RCL %s" % state_num)

    def turn_on_measurements(self):
        self.write_to_device("INIT:ALL:CONT ON")

    def getPower(self, channel_number):
        power = float(self.request_from_device("FETC%s?" % channel_number))
        return power

class PowerMeterIOC(GenericIOC):
    def initialiseDevice(self):
        powerMeter =  usbtmc.Instrument(2733, 27)

        # Preset menu
        usbCommand = "SYST:PRES"
        print "Sending " + usbCommand + " to device"
        powerMeter.write(usbCommand)
        time.sleep(2)

        # Recall state 1
        usbCommand = "*RCL 1"
        print "Sending " + usbCommand + " to device"
        powerMeter.write(usbCommand)

        # Initiate continuous measurements
        usbCommand = "INIT:ALL:CONT ON"
        print "Sending " + usbCommand + " to device"
        powerMeter.write(usbCommand)

        self.powerMeter = powerMeter

    def getDataFromDevice(self):
        usbCommand = "FETC1?"
        print "Sending " + usbCommand + " to device"
        power1 = self.powerMeter.ask(usbCommand)
        print "Received " + power1 + " from device"
        power1f = float(power1) + 43.9
        power1 = str(power1f)
        time.sleep(2)

        usbCommand = "FETC2?"
        print "Sending " + usbCommand + " to device"
        power2 = self.powerMeter.ask(usbCommand)
        print "Received " + power2 + " from device"
        power2f = float(power2) + 59.5 + 7.2
        power2 = str(power2f)
        data = {"power1": power1, "power2": power2}
        return json.dumps(data)

if __name__ == "__main__":
    def handleIncomingMessage(client, powerMeter, msg):
        pass

    itsPowerMeterIOC = PowerMeterIOC(brokerFile = 'itsmqttbroker.dat')

    itsPowerMeterIOC.handleIncomingMessage = handleIncomingMessage
    itsPowerMeterIOC.periodicPollPeriodSecs = 2
    itsPowerMeterIOC.mqttStart(
            clientId       = "itsPowerMeter02Ioc",
            subscribeTopic = "itsPowerMeter02/set/#",
            publishTopic   = "itsPowerMeter02/get")
    itsPowerMeterIOC.client.user_data_set(itsPowerMeterIOC.powerMeter)

    itsPowerMeterIOC.run()
