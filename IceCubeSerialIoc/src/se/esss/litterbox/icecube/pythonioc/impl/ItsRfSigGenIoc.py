import paho.mqtt.client as mqtt
import time
import json
import sys
import usbtmc

#should be unique for each Ioc
clientId = "itsRfSigGen01Ioc"
subscribeTopic = "itsRfSigGen01/set/#"
publishtopic = "itsRfSigGen01/get"
periodicPollPeriodSecs = 1

# Power meter initialization
usbInst =  usbtmc.Instrument(2733, 72)
usbCommand = "*RST"
print "Sending " + usbCommand + " to device"
usbInst.write(usbCommand)
time.sleep(2)
usbCommand = "FREQ 352.21MHZ"
print "Sending " + usbCommand + " to device"
usbInst.write(usbCommand)
usbCommand = "POW -30"
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

# usually leave this alone
subscribeQos = 0
publishQos = 0
brokerAddress = "broker.shiftr.io"
brokerPort = 1883
brokertimeout = 60

def getDataFromDevice():
# code here to be executed in periodic poll and set to local device
    return ""
def handleIncomingMessage(topic, message):
# handle messages from broker
    if "/set/rf" in topic:
        data = json.loads(str(message))
        usbCommand = "OUTP " + data['rfPowOn']
        print "Sending " + usbCommand + " to device"
        usbInst.write(usbCommand)
        usbCommand = "FREQ " + data['rfFreq'] + "MHZ"
        print "Sending " + usbCommand + " to device"
        usbInst.write(usbCommand)
        usbCommand = "POW " + data['rfPowLvl']
        print "Sending " + usbCommand + " to device"
        usbInst.write(usbCommand)
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
    