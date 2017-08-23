const mqtt = require('mqtt');  
const client = mqtt.connect('tcp://broker.shiftr.io:1883',{username:'06005fd6',password: new Buffer('2c8c91273f654381'), clientId: 'beagleBoneTester'});

client.on ('connect', function () {subscribeToTopics();})
client.on('message', function (topic, message) {newMessage(topic, message);})

function subscribeToTopics()
{
    client.subscribe("itsbeagleBone01/get"); 
    client.subscribe("itsbeagleBone01/set"); 
}
function newMessage(topic, message) 
{
  console.log("Topic = " + topic + " Message = " + message.toString());
}


var reflPowLvlObj = {"reflPowLvl":0.143};
var resetObj = {"reset":"TRUE"};
var pinSwitchObj = {"pinSwitch":"ON"};
var bbStatusTestObj = {"reflPowLvl":"0.143", "pinSwitch":"ON", "trip":"TRUE", "tripType":"arcDet"};

//client.publish("itsbeagleBone01/set", JSON.stringify(reflPowLvlObj));
//client.publish("itsbeagleBone01/set", JSON.stringify(resetObj));
//client.publish("itsbeagleBone01/set", JSON.stringify(pinSwitchObj));

setInterval(testPublish, 10000);
function testPublish() {
	client.publish("itsbeagleBone01/get", JSON.stringify(bbStatusTestObj));
	}
