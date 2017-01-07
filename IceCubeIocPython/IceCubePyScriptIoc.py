import paho.mqtt.client as mqtt
import time
import json
import sys

#should be unique for each Ioc
clientId = "itsPythonTestIoc"
subscribeTopic = "itsPythonTest/set/#"
publishtopic = "itsPythonTest/get"
periodicPollPeriodSecs = 1

# usually leave this alone
subscribeQos = 0
publishQos = 0
brokerAddress = "broker.shiftr.io"
brokerPort = 1883
brokertimeout = 60

def getDataFromDevice():
# code here to be executed in periodic poll and set to local device
    data = {"key1": time.strftime("%H:%M:%S"), "key2": "Hej"}
    return json.dumps(data)
def handleIncomingMessage(topic, message):
# handle messages from broker
    if "/set/rf" in topic:
        data = json.loads(str(message))
        print "rf power level = " + data['rfPowLvl']
    return

userName = sys.argv[1]
userKey = sys.argv[2]
incomingMessageTopic = ""
incomingMessage = None
newIncomingMessage = True
def on_connect(client, userdata, rc):
    global brokerAddress
    global subscribeTopic
    print("Connected to: " + brokerAddress + " with result code "+str(rc))
    client.subscribe(subscribeTopic)
    print("Subscribing to: " + subscribeTopic)

# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
    print clientId + " received message on topic: " + msg.topic
    global incomingMessageTopic
    global incomingMessage
    global newIncomingMessage
    incomingMessageTopic = msg.topic
    incomingMessage = msg.payload
    newIncomingMessage = True

client = mqtt.Client(client_id=clientId, clean_session=False, userdata=None)
client.on_connect = on_connect
client.on_message = on_message

client.username_pw_set(userName, userKey)
client.connect(brokerAddress, brokerPort, brokertimeout)

client.loop_start()

while True:
    time.sleep(periodicPollPeriodSecs)
    dataFromDevice = getDataFromDevice()
    if len(dataFromDevice) > 0:
        client.publish(publishtopic, dataFromDevice, publishQos, True)
    if newIncomingMessage:
        handleIncomingMessage(incomingMessageTopic, incomingMessage)
        newIncomingMessage = False
    