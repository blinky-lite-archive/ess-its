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
        self.powerMeter =  PowerMeter(2733, 27)
        self.powerMeter.connect()
        self.powerMeter.preset()
        time.sleep(2)
        #self.powerMeter.recall_state(1)
        self.powerMeter.turn_on_measurements()

    def getDataFromDevice(self):
        power1 = self.powerMeter.getPower(1) + 43.9
        power2 = self.powerMeter.getPower(2) + 59.5 + 7.2

        data = {"power1": str(power1), "power2": str(power2)}
        print data
        return json.dumps(data)

if __name__ == "__main__":
    def handleIncomingMessage(client, powerMeter, msg):
        pass

    itsPowerMeterIOC = PowerMeterIOC(brokerFile = 'itsmqttbroker.dat')

    itsPowerMeterIOC.handleIncomingMessage = handleIncomingMessage
    itsPowerMeterIOC.periodicPollPeriodSecs = 0
    itsPowerMeterIOC.mqttStart(
            clientId       = "itsPowerMeter02Ioc",
            subscribeTopic = "itsPowerMeter02/set/#",
            publishTopic   = "itsPowerMeter02/get")
    itsPowerMeterIOC.client.user_data_set(itsPowerMeterIOC.powerMeter)

    itsPowerMeterIOC.run()
