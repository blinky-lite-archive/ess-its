package se.esss.litterbox.icecube.serialioc;

import se.esss.litterbox.serialreadwrite.SerialReadWrite;
import se.esss.litterbox.simplemqttclient.SimpleMqttSubscriber;

public abstract class IceCubeSerialIoc extends SimpleMqttSubscriber implements Runnable
{
	private int subscribeQos = 0;
	private int publishQos = 0;
	private boolean cleanSession = false;
	private boolean runPeriodicPoll = false;
	private int periodicPollPeriodmillis = 1000;
	private String publishTopic;
	private String subscribeTopic;
	private String domain;
	private SerialReadWrite serialReadWrite;

	public int getPublishQos() {return publishQos;}
	public int getSubscribeQos() {return subscribeQos;}
	public boolean isCleanSession() {return cleanSession;}

	public void setSubscribeQos(int subscribeQos) {this.subscribeQos = subscribeQos;}
	public void setPublishQos(int publishQos) {this.publishQos = publishQos;}
	public void setCleanSession(boolean cleanSession) {this.cleanSession = cleanSession;}
	public void setPublishTopic(String publishTopic) {this.publishTopic = publishTopic;}

	public IceCubeSerialIoc(String domain, String clientIdBase, String brokerUrl, String brokerKey, String brokerSecret, String serialPortName) throws Exception 
	{
		super(clientIdBase, brokerUrl, brokerKey, brokerSecret);
		this.domain = domain;
		serialReadWrite = new SerialReadWrite(serialPortName);
	}
	public void startIoc(String subscribeTopic, String publishTopic) throws Exception
	{
		this.setPublishTopic(publishTopic);
		this.subscribeTopic = subscribeTopic;
    	subscribe(domain, subscribeTopic, getSubscribeQos(), cleanSession);
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
	@Override
	public void connectionLost(Throwable arg0) 
	{
		while (!isConnected())
		{
			try
			{
				Thread.sleep(5000);
				setStatus("Lost connection. Trying to reconnect." );
				boolean cleanSession = false;
				subscribe(domain, subscribeTopic, subscribeQos, cleanSession);
			} catch (Exception e) {setStatus("Error: " + e.getMessage());}
		}
	}
	@Override
	public void newMessage(String domain, String topic, byte[] message) 
	{
		setStatus(getClientId() + "  on domain: " + domain + " recieved message on topic: " + topic);
		if (domain.equals(this.domain)) 
		{
			if (runPeriodicPoll) //guard against another set coming in while program is handling set
			{
				runPeriodicPoll = false;
				try {Thread.sleep((long)periodicPollPeriodmillis);} catch (InterruptedException e) {}
				handleIncomingMessage(topic, message);
				try {Thread.sleep((long)periodicPollPeriodmillis);} catch (InterruptedException e) {}
				runPeriodicPoll = true;
			}
		}
	}
	@Override
	public void run() 
	{
		while(runPeriodicPoll)
		{
			try {Thread.sleep((long)periodicPollPeriodmillis);} catch (InterruptedException e) {runPeriodicPoll = false;}
			byte[] serialData = getSerialData();
			try {publishMessage(domain, publishTopic, serialData, publishQos, true);} catch (Exception e) {}
		}
	}
}
