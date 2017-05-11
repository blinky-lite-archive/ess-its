package se.esss.litterbox.icecube.ioc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import se.esss.litterbox.icecube.simplemqtt.SimpleMqttClient;

public abstract class IceCubePeriodicPollIoc extends SimpleMqttClient implements Runnable
{
	private int subscribeQos = 0;
	private int publishQos = 0;
	private boolean cleanSession = false;
	private boolean runPeriodicPoll = true;
	private int periodicPollPeriodmillis = 1000;
	private String publishTopic;
	private boolean newIncomingMessage = false;
	private String incomingMessageTopic;
	private byte[] incomingMessage;
	private Thread pollThread;

	public int getPublishQos() {return publishQos;}
	public int getSubscribeQos() {return subscribeQos;}
	public boolean isCleanSession() {return cleanSession;}

	public void setSubscribeQos(int subscribeQos) {this.subscribeQos = subscribeQos;}
	public void setPublishQos(int publishQos) {this.publishQos = publishQos;}
	public void setCleanSession(boolean cleanSession) {this.cleanSession = cleanSession;}
	public void setPublishTopic(String publishTopic) {this.publishTopic = publishTopic;}
	public void setPeriodicPollPeriodmillis(int periodicPollPeriodmillis) {this.periodicPollPeriodmillis = periodicPollPeriodmillis;}

	public IceCubePeriodicPollIoc(String clientId, String mqttBrokerInfoFilePath, int keepAliveInterval) throws Exception 
	{
		super(clientId, mqttBrokerInfoFilePath, false, keepAliveInterval);
		cleanSession = false;
	}
	public void startIoc(String subscribeTopic, String publishTopic) throws Exception
	{
		this.setPublishTopic(publishTopic);
    	subscribe(subscribeTopic, getSubscribeQos());
		setStatus("Ready for messages");
		runPeriodicPoll = true;
		pollThread = new Thread(this);
		pollThread.start();
	}
	public abstract byte[] getDataFromGizmo();
	public abstract void handleBrokerMqttMessage(String topic, byte[] message);
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
		while(true)
		{
			try {Thread.sleep((long)periodicPollPeriodmillis);} catch (InterruptedException e) {}
			if (runPeriodicPoll)
			{
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
	@Override
	public void lostMqttConnection(Throwable arg0) 
	{
		try 
		{
			setStatus("Stopping periodic poll...");
			runPeriodicPoll = false;
			reconnect();
			runPeriodicPoll = true;
			setStatus("Starting periodic poll...");
		} catch (Exception e) {setStatus("Error on reconnect: " + arg0.getMessage());}
	}
	public String[] runExternalProcess(String command, boolean linux, boolean getInfo) throws Exception 
	{
    	Process p = null;
		String[] status = null;
    	String[] cmd = null;
    	if (!linux)
    	{ 
    		cmd = new String[3];
    		cmd[0] = command;
    		cmd[1] = "";
    		cmd[2] = "";
    	}
    	else
    	{
    		cmd = new String[3];
    		cmd[0] = "/bin/sh";
    		cmd[1] = "-c";
    		cmd[2] = command;
    	}
		try 
		{
			p = Runtime.getRuntime().exec(cmd);
			if (!getInfo)
			{
				status  = new String[1];
				status[0] = "";
				return status;
			}
			InputStream iserr = p.getErrorStream();
			InputStreamReader isrerr = new InputStreamReader(iserr);
	    	BufferedReader err = new BufferedReader(isrerr);  
	    	String errline = null;  
    		errline = err.readLine();
			err.close();
			isrerr.close();
			iserr.close();
			if (errline != null)
			{
				throw new Exception(errline);
			}
			InputStream is = p.getInputStream();
			if (is == null)
			{
				status  = new String[1];
				status[0] = "";
				return status;
			}
			InputStreamReader isr = new InputStreamReader(is);
	    	BufferedReader in = new BufferedReader(isr);  
	    	String line = null;  
	    	ArrayList<String> outputBuffer = new ArrayList<String>();
			while ((line = in.readLine()) != null) 
			{  
				outputBuffer.add(line);
			}
			int nlines = outputBuffer.size();
			if (nlines < 1) 
			{
				status  = new String[1];
				status[0] = "";
			}
			else
			{
				status = new String[nlines];
				for (int il = 0; il < nlines; ++il)
				{
					status[il] = outputBuffer.get(il);
				}
			}
			in.close();
			isr.close();
			is.close();
			return status;
		} 
		catch (IOException e) { throw new Exception(e.getMessage());} 

	}


}
