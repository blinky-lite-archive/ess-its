package se.esss.litterbox.its.icecube.example.radtemphumid;

import java.util.Timer;
import java.util.TimerTask;

import se.esss.litterbox.serialreadwrite.SerialReadWrite;
import se.esss.litterbox.simplemqttclient.SimpleMqttSubscriber;

public class ItsRadTempHumidIoc extends SimpleMqttSubscriber implements Runnable
{
	public final String appName = "ItsRadTempHumidIoc";
	public final  String appAuthor = "Dave McGinnis";
	public final  String appVersion = "v01";
	public final  String appDate = "18-Dec-2016 18:26";
	
	private int subscribeQos = 0;
	private int publishQos = 0;
	private boolean cleanSession = false;
	private boolean runPeriodicPoll = false;
	private int periodicPollPeriodmillis = 1000;
	private SerialReadWrite serialReadWrite;

	public int getPublishQos() {return publishQos;}
	public int getSubscribeQos() {return subscribeQos;}
	public boolean isCleanSession() {return cleanSession;}

	public void setSubscribeQos(int subscribeQos) {this.subscribeQos = subscribeQos;}
	public void setPublishQos(int publishQos) {this.publishQos = publishQos;}
	public void setCleanSession(boolean cleanSession) {this.cleanSession = cleanSession;}

	public ItsRadTempHumidIoc(String clientIdBase, String brokerUrl, String brokerKey, String brokerSecret, String serialPortName) throws Exception 
	{
		super(clientIdBase, brokerUrl, brokerKey, brokerSecret);
		setStatus(appName + " " + appVersion + " " + appDate + " " + appAuthor);

		
		serialReadWrite = new SerialReadWrite(serialPortName);

    	subscribe("its", "geiger01/set/#", getSubscribeQos(), cleanSession);
		setStatus("Ready for messages");
		new Thread(this).start();
		runPeriodicPoll = true;
	}
	private void publishSerialData()
	{
		try 
		{
			String readData = serialReadWrite.writeReadStringData("cpmGet", 10);
			int ispot = readData.indexOf("cpmGet");
			String data = "0";
			if (ispot >= 0)
			{
				data = readData.substring(6, readData.length());
				data = data.trim();
			}
			try {publishMessage("its", "geiger01/get/cpm", data.getBytes(), publishQos, true);} catch (Exception e) {}

			setStatus(data);
			
		} catch (Exception e) 
		{
//			e.printStackTrace();
			setStatus("Unable to read serial port.");
		}
	}
	@Override
	public void connectionLost(Throwable arg0) 
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void newMessage(String arg0, String arg1, byte[] arg2) 
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void run() 
	{
		while(runPeriodicPoll)
		{
			try {Thread.sleep((long)periodicPollPeriodmillis);} catch (InterruptedException e) {runPeriodicPoll = false;}
			publishSerialData();
		}
	}
	public static void main(String[] args) throws Exception 
	{
		new ItsRadTempHumidIoc("ItsRadTempHumidIoc", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5", "/dev/rfcomm1");

	}
}
