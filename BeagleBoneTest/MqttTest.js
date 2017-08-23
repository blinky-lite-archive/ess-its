const mqtt = require('mqtt');  
const client = mqtt.connect('tcp://broker.shiftr.io:1883',{username:'06005fd6',password: new Buffer('2c8c91273f654381'), clientId: 'beagleBoneIoc'});

var subscribeTopics = ["beagleBoneIoc/setLed0", "beagleBoneIoc/setLed1", "beagleBoneIoc/setLed2", "beagleBoneIoc/setLed3"];
var publishTopics   = ["beagleBoneIoc/getLed0", "beagleBoneIoc/getLed1", "beagleBoneIoc/getLed2", "beagleBoneIoc/getLed3"];

client.on ('connect', function () {subscribeToTopics();})
client.on('message', function (topic, message) {newMessage(topic, message);})

function subscribeToTopics()
{
    for(var i in publishTopics) 
    {
        client.subscribe(publishTopics[i]); 
    }
}
function newMessage(topic, message) 
{
    console.log("Topic = " + topic + " Message = " + message.toString());
}
client.publish(subscribeTopics[0], "ON");
client.publish(subscribeTopics[1], "OFF");
client.publish(subscribeTopics[2], "OFF");
client.publish(subscribeTopics[3], "OFF");

