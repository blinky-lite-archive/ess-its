from IceCubePyClassIoc import GenericIOC
import serial
import json
import time

class SingleRelayIOC(GenericIOC):
    def initialiseDevice(self):
        self.serialCon = serial.Serial('/dev/cu.usbmodem1421',
            baudrate = 9600,
            timeout = 5.0)

    def getDataFromDevice(self):
        return None

if __name__ == "__main__":
    def handleIncomingMessage(client, serialCon, msg):
        # handle messages from broker
        if "/set/lamp" in msg.topic:
            data = json.loads(str(msg.payload))

            serialCommand = "P" + str(data['period']) + "\n"
            print "Sending " + serialCommand + " to device"
            serialCon.write(serialCommand)
            serialCon.readline()

            time.sleep(0.1)

            serialCommand = "T" + str(data['onTime']) + "\n"
            print "Sending " + serialCommand + " to device"
            serialCon.write(serialCommand)
            serialCon.readline()

    homeSingleRelayIOC = SingleRelayIOC(brokerFile = 'itsmqttbroker.dat')

    homeSingleRelayIOC.handleIncomingMessage = handleIncomingMessage
    homeSingleRelayIOC.periodicPollPeriodSecs = 1
    homeSingleRelayIOC.mqttStart(
            clientId       = "homeSingleRelayIOC",
            subscribeTopic = "homeSingleRelayIOC/set/#",
            publishTopic   = "homeSingleRelayIOC/get")
    homeSingleRelayIOC.client.user_data_set(homeSingleRelayIOC.serialCon)

    homeSingleRelayIOC.run()
