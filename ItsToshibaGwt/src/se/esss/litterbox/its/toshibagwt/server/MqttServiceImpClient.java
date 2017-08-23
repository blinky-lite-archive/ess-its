package se.esss.litterbox.its.toshibagwt.server;

import se.esss.litterbox.icecube.simplemqtt.SimpleMqttClient;

public class MqttServiceImpClient extends SimpleMqttClient
{

	MqttServiceImpl mqttServiceImpl;
	boolean reconnectOk = true;
	
	public MqttServiceImpClient(MqttServiceImpl mqttServiceImpl, String clientIdBase, String mqttBrokerInfoFilePath, boolean cleanSession) throws Exception 
	{
		super(clientIdBase, mqttBrokerInfoFilePath, cleanSession, 30);
		this.mqttServiceImpl = mqttServiceImpl;
	}
	public MqttServiceImpClient(MqttServiceImpl mqttServiceImpl, String clientId, String brokerUrl, String brokerKey, String brokerSecret, boolean cleanSession) throws Exception 
	{
		super(clientId, brokerUrl, brokerKey,brokerSecret, cleanSession, 30);
		this.mqttServiceImpl = mqttServiceImpl;
	}

	@Override
	public void newMessage(String topic, byte[] message) 
	{
		mqttServiceImpl.setMessage(topic, message);
	}
	@Override
	public void lostMqttConnection(Throwable arg0) 
	{
		if (!reconnectOk)
		{
			try 
			{
				unsubscribeAll();
				disconnect();
				return;
			} catch (Exception e) {setStatus("Error on disconnect: " + arg0.getMessage());}
		}
		try {reconnect();} catch (Exception e) {setStatus("Error on reconnect: " + arg0.getMessage());}
	}

}
