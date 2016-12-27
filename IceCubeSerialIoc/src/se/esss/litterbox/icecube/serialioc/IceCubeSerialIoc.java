package se.esss.litterbox.icecube.serialioc;

import org.json.simple.JSONObject;

import se.esss.litterbox.serialreadwrite.SerialReadWrite;
import se.esss.litterbox.simplemqttclient.SimpleMqttClient;

public abstract class IceCubeSerialIoc extends SimpleMqttClient implements Runnable
{
	private int subscribeQos = 0;
	private int publishQos = 0;
	private boolean cleanSession = false;
	private boolean runPeriodicPoll = false;
	private int periodicPollPeriodmillis = 1000;
	private String publishTopic;
	private SerialReadWrite serialReadWrite;
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

	public IceCubeSerialIoc(String clientId, String brokerUrl, String brokerKey, String brokerSecret, String serialPortName) throws Exception 
	{
		super(clientId, brokerUrl, brokerKey, brokerSecret, false);
		cleanSession = false;
		serialReadWrite = new SerialReadWrite(serialPortName);
	}
	public void startIoc(String subscribeTopic, String publishTopic) throws Exception
	{
		this.setPublishTopic(publishTopic);
    	subscribe(subscribeTopic, getSubscribeQos());
		setStatus("Ready for messages");
		new Thread(this).start();
		runPeriodicPoll = true;
	}
	public String writeReadSerialData(String command, int timeoutsecs)
	{
		String data = "";
		try 
		{
			data =  serialReadWrite.writeReadStringData(command, timeoutsecs);
			setStatus("Recieved from Serialport: " + data);
		} catch (Exception e) 
		{
//			e.printStackTrace();
			setStatus("Unable to read serial port.");
		}
		return data;
	}
	public abstract byte[] getSerialData();
	public abstract void handleIncomingMessage(String topic, byte[] message);
	@SuppressWarnings("unchecked")
	public void readResponseStringFromSerial(String command, int timeOutSec, JSONObject outputData)
	{
		String readData = writeReadSerialData(command, 10);
		int ispot = readData.indexOf(command);
		String data = "0";
		if (ispot >= 0)
		{
			data = readData.substring(command.length(), readData.length());
			data = data.trim();
		}
		outputData.put(command, data);
		return;
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
			byte[] serialData = getSerialData();
			try {publishMessage(publishTopic, serialData, publishQos, true);} catch (Exception e) {}
			if (newIncomingMessage)
			{
				handleIncomingMessage(incomingMessageTopic, incomingMessage);
				newIncomingMessage = false;
			}
		}
	}
}
