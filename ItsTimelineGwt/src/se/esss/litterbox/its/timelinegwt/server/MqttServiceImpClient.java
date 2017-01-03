package se.esss.litterbox.its.timelinegwt.server;

import se.esss.litterbox.icecube.simplemqtt.SimpleMqttClient;

public class MqttServiceImpClient extends SimpleMqttClient
{

	MqttServiceImpl mqttServiceImpl;
	
	public MqttServiceImpClient(MqttServiceImpl mqttServiceImpl, String clientIdBase, String mqttBrokerInfoFilePath, boolean cleanSession) throws Exception 
	{
		super(clientIdBase, mqttBrokerInfoFilePath, cleanSession);
		this.mqttServiceImpl = mqttServiceImpl;
	}

	@Override
	public void newMessage(String topic, byte[] message) 
	{
		mqttServiceImpl.setMessage(topic, message);
	}

}
