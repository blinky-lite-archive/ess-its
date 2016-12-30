package se.esss.litterbox.icecube.usbtmcioc;

import org.json.simple.JSONObject;

import se.esss.litterbox.icecube.simplemqtt.SimpleMqttClient;

public abstract class IceCubeUsbtmcIoc extends SimpleMqttClient implements Runnable
{
	private int subscribeQos = 0;
	private int publishQos = 0;
	private boolean cleanSession = false;
	private boolean runPeriodicPoll = false;
	private int periodicPollPeriodmillis = 1000;
	private String publishTopic;
	private UsbtmcDevice usbtmcDevice;
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

	public IceCubeUsbtmcIoc(String clientId, String brokerUrl, String brokerKey, String brokerSecret, String devNickName, int vendorId, int productId) throws Exception 
	{
		super(clientId, brokerUrl, brokerKey, brokerSecret, false);
		cleanSession = false;
		usbtmcDevice = new UsbtmcDevice(devNickName, vendorId, productId);
	}
	public void startIoc(String subscribeTopic, String publishTopic) throws Exception
	{
		this.setPublishTopic(publishTopic);
    	subscribe(subscribeTopic, getSubscribeQos());
		setStatus("Ready for messages");
		new Thread(this).start();
		runPeriodicPoll = true;
	}
	public  void writeUsbtmcData(String command) 
	{
		try 
		{
			usbtmcDevice.write(command);
			setStatus("Wrote: " + command + " to " + usbtmcDevice.devNickName);
		} catch (Exception e) 
		{
//			e.printStackTrace();
			setStatus("Error writing: " + command + " to "+ usbtmcDevice.devNickName + ": " + e.getMessage());
		}
	}
	public abstract byte[] getUsbtmcData();
	public abstract void handleIncomingMessage(String topic, byte[] message);
	@SuppressWarnings("unchecked")
	public void readResponseStringFromUsbtmc(String command, String commandJsonKey, JSONObject outputData)
	{
		try 
		{
			String readData = usbtmcDevice.read(command)[0];
			outputData.put(commandJsonKey, readData);
			setStatus("Wrote: " + command + " to " + usbtmcDevice.devNickName);
			setStatus("Recieved: " + readData);
			return;
		} catch (Exception e) 
		{
//			e.printStackTrace();
			setStatus("Error writing/reading: " + command + " to "+ usbtmcDevice.devNickName + ": " + e.getMessage());
		}
	}
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
			byte[] usbtmcData = getUsbtmcData();
			if (usbtmcData != null)
				try {publishMessage(publishTopic, usbtmcData, publishQos, true);} catch (Exception e) {}
			if (newIncomingMessage)
			{
				handleIncomingMessage(incomingMessageTopic, incomingMessage);
				newIncomingMessage = false;
			}
		}
	}
}
