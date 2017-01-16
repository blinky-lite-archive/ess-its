import paho.mqtt.client as mqtt
import time
import json
import sys

class GenericIOC(object):
    def __init__(self, brokerFile):
        jsonBrokerObj = json.load( open(brokerFile, 'r') )
        # usually leave this alone
        self.subscribeQos = 0
        self.publishQos   = 0
        self.brokerAddress = jsonBrokerObj['broker'].encode().replace('tcp://', '')
        self.brokerPort    = jsonBrokerObj['brokerport'].encode()
        self.brokertimeout = 60

        self.userName = jsonBrokerObj['key'].encode()
        self.userKey  = jsonBrokerObj['secret'].encode()
        self.periodicPollPeriodSecs = None
        self.handleIncomingMessage = lambda client, userdata, msg: msg.topic
        self.initialiseDevice()

    def initialiseDevice(self):
        pass

    def getDataFromDevice(self):
        return None

    def mqttStart(self, clientId, subscribeTopic, publishTopic):
        self.clientId = clientId
        self.subscribeTopic = subscribeTopic
        self.publishTopic = publishTopic
        def on_connect(client, userdata, flags, rc):
            if rc == 0:
                print "Connected!"
                client.subscribe(subscribeTopic)
                print("Subscribed to: " + subscribeTopic)
            else:
                print "Connection failed"

        client = mqtt.Client(
            client_id = clientId,
            clean_session = False,
            userdata = None)
        client.on_connect = on_connect
        client.on_message = self.handleIncomingMessage

        client.username_pw_set(self.userName, self.userKey)
        client.connect(
                self.brokerAddress,
                self.brokerPort,
                self.brokertimeout)

        self.client = client
        self.client.loop_start()

    def run(self):
        while True:
            time.sleep(self.periodicPollPeriodSecs)
            dataFromDevice = self.getDataFromDevice()
            if self.getDataFromDevice():
                self.client.publish(
                    self.publishTopic,
                    dataFromDevice,
                    self.publishQos,
                    True)
