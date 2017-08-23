var b = require('bonescript');
const mqtt = require('mqtt');  
const client = mqtt.connect('tcp://broker.shiftr.io:1883',{username:'06005fd6',password: new Buffer('2c8c91273f654381'), clientId: 'beagleBoneIoc'});

var subscribeTopics = ["beagleBoneIoc/setLed0", "beagleBoneIoc/setLed1", "beagleBoneIoc/setLed2", "beagleBoneIoc/setLed3"];
var publishTopics   = ["beagleBoneIoc/getLed0", "beagleBoneIoc/getLed1", "beagleBoneIoc/getLed2", "beagleBoneIoc/getLed3"];

client.on ('connect', function () {subscribeToTopics();})
client.on('message', function (topic, message) {newMessage(topic, message);})

function subscribeToTopics()
{
    for(var i in subscribeTopics) 
    {
        client.subscribe(subscribeTopics[i]); 
    }
    client.publish(subscribeTopics[0], "ON");
}

var leds = ["USR0", "USR1", "USR2", "USR3", "P9_14"];
for(var i in leds) 
{
    b.pinMode(leds[i], b.OUTPUT);
}

var state = b.LOW;
for(var i in leds) 
{
    b.digitalWrite(leds[i], state);
}

function newMessage(topic, message) 
{
  console.log("Topic = " + topic + " Message = " + message.toString());
    for(var i in subscribeTopics) 
    {
       if (subscribeTopics[i].localeCompare(topic) == 0)
       {
           if (message.toString().localeCompare("ON") == 0)
           {
                b.digitalWrite(leds[i], b.HIGH);
                client.publish(publishTopics[i], "ON");
           }
           if (message.toString().localeCompare("OFF") == 0)
           {
                b.digitalWrite(leds[i], b.LOW);
                client.publish(publishTopics[i], "OFF");
           }
       }
    }
  
}
