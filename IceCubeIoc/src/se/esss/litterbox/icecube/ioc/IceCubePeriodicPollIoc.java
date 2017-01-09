package se.esss.litterbox.icecube.ioc;

import se.esss.litterbox.icecube.simplemqtt.SimpleMqttClient;

public abstract class IceCubePeriodicPollIoc extends SimpleMqttClient implements Runnable
{
	private int subscribeQos = 0;
	private int publishQos = 0;
	private boolean cleanSession = false;
	private boolean runPeriodicPoll = false;
	private int periodicPollPeriodmillis = 1000;
	private String publishTopic;
	private boolean newIncomingMessage = false;
	private String incomingMessageTopic;
	private byte[] incomingMessage;

	public int getPublishQos() {return publishQos;}
	public int getSubscribeQos() {return subscribeQos;}
	public boolean isCleanSession() {return cleanSession;}

	public void setSubscribeQos(int subscribeQos) {this.subscribeQos = subscribeQos;}
	public void setPublishQos(int publishQos) {this.publishQos = publishQos;}
	public void setCleanSession(boolean cleanSession) {this.cleanSession = cleanSession;}
	public void setPublishTopic(String publishTopic) {this.publishTopic = publishTopic;}
	public void setPeriodicPollPeriodmillis(int periodicPollPeriodmillis) {this.periodicPollPeriodmillis = periodicPollPeriodmillis;}

	public IceCubePeriodicPollIoc(String clientId, String mqttBrokerInfoFilePath) throws Exception 
	{
		super(clientId, mqttBrokerInfoFilePath, false);
		cleanSession = false;
	}
	public void startIoc(String subscribeTopic, String publishTopic) throws Exception
	{
		this.setPublishTopic(publishTopic);
    	subscribe(subscribeTopic, getSubscribeQos());
		setStatus("Ready for messages");
		new Thread(this).start();
		runPeriodicPoll = true;
	}
	public abstract byte[] getDataFromGizmo();
	public abstract void handleBrokerMqttMessage(String topic, byte[] message);
	@Override
	public void connectionLost(Throwable arg0) 
	{
		while (!isConnected())
		{
			try
			{
				Thread.sleep(5000);
				setStatus("Lost connection. Trying to reconnect." );
				reconnect();
			} catch (Exception e) {setStatus("Error: " + e.getMessage());}
		}
	}
	@Override
	public void newMessage(String topic, byte[] message) 
	{
		setStatus(getClientId() + " recieved message on topic: " + topic);
		incomingMessageTopic = topic;
		incomingMessage = message;
		newIncomingMessage = true;
	}
	@Override
	public void run() 
	{
		while(runPeriodicPoll)
		{
			try {Thread.sleep((long)periodicPollPeriodmillis);} catch (InterruptedException e) {}
			byte[] gizmoData = getDataFromGizmo();
			if (gizmoData != null)
				try {publishMessage(publishTopic, gizmoData, publishQos, true);} catch (Exception e) {}
			if (newIncomingMessage)
			{
				handleBrokerMqttMessage(incomingMessageTopic, incomingMessage);
				newIncomingMessage = false;
			}
		}
	}

}
