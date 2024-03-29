from IceCubePyClassIoc import GenericIOC
import serial
import json
import time
import threading

class LockableSerial(serial.Serial):
    lock = threading.Lock()

class NWaterSystemIOC(GenericIOC):
    def initialiseDevice(self):
        self.serialCon = LockableSerial('/dev/cu.usbmodem1421',
            baudrate = 9600,
            timeout = 5.0)

    def getDataFromDevice(self):
        reqCommand = "?\n"
        with self.serialCon.lock:
            self.serialCon.write(reqCommand)
            dataDump = self.serialCon.readline()

        data = str.split(dataDump, " ")
        jsonData = {'power': data[0][1:].rstrip()}

        return json.dumps(jsonData)

if __name__ == "__main__":
    def handleIncomingMessage(client, serialCon, msg):
        # handle messages from broker
        if "/set/power" in msg.topic:
            data = json.loads(str(msg.payload))

            serialCommand = "P" + str(data['power']) + "\n"
            print "Sending " + serialCommand + " to device"
            with serialCon.lock:
                serialCon.write(serialCommand)
                serialCon.readline()

    homeSingleRelayIOC = NWaterSystemIOC(brokerFile = 'itsmqttbroker.dat')

    homeSingleRelayIOC.handleIncomingMessage = handleIncomingMessage
    homeSingleRelayIOC.periodicPollPeriodSecs = 1
    homeSingleRelayIOC.mqttStart(
            clientId       = "homeSingleRelayIOC",
            subscribeTopic = "homeSingleRelayIOC/set/#",
            publishTopic   = "homeSingleRelayIOC/get")
    homeSingleRelayIOC.client.user_data_set(homeSingleRelayIOC.serialCon)

    homeSingleRelayIOC.run()
