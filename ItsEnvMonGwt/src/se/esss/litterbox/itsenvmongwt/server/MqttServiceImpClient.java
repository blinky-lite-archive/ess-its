package se.esss.litterbox.itsenvmongwt.server;

import se.esss.litterbox.simplemqttclient.SimpleMqttClient;

public class MqttServiceImpClient extends SimpleMqttClient
{

	MqttServiceImpl mqttServiceImpl;
	
	public MqttServiceImpClient(MqttServiceImpl mqttServiceImpl, String clientIdBase, String brokerUrl, String brokerKey, String brokerSecret, boolean cleanSession) throws Exception 
	{
		super(clientIdBase, brokerUrl, brokerKey, brokerSecret, cleanSession);
		this.mqttServiceImpl = mqttServiceImpl;
	}

	@Override
	public void newMessage(String domain, String topic, byte[] message) 
	{
		mqttServiceImpl.setMessage(domain, topic, message);
	}

}
