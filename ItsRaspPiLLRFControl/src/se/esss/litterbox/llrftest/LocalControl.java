package se.esss.litterbox.llrftest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import se.esss.litterbox.simplemqttclient.SimpleMqttClient;

public class LocalControl extends SimpleMqttClient
{

	public LocalControl(String brokerUrl, String brokerKey, String brokerSecret) 
	{
		super(brokerUrl, brokerKey, brokerSecret);
	}
	@Override
	public void newMessage(String topic, MqttMessage mqttMessage) throws Exception
	{
		System.out.println("| Topic:" + topic);
		System.out.println("| Message: " + new String(mqttMessage.getPayload()));
		if (topic.equals("raspPiLLrf/pulser/onOff"))
		{
			String cmd = "python /home/pi/llrfTestUsb/pulserOnOff.py " + new String(mqttMessage.getPayload());
			System.out.println(cmd);
			String[] info  = runExternalProcess(cmd, true, true);
			for (int ii = 0; ii < info.length; ++ii) System.out.println(info[ii]);
		}
		if (topic.equals("raspPiLLrf/pulser/setup"))
		{
			String cmd = "python /home/pi/llrfTestUsb/setupPulser.py " + new String(mqttMessage.getPayload());
			System.out.println(cmd);
			String[] info  = runExternalProcess(cmd, true, true);
			for (int ii = 0; ii < info.length; ++ii) System.out.println(info[ii]);
		}
		if (topic.equals("raspPiLLrf/pulser/ask/status"))
		{
			String cmd = "python /home/pi/llrfTestUsb/readPulser.py ";
			System.out.println(cmd);
			String[] info  = runExternalProcess(cmd, true, true);
			for (int ii = 0; ii < info.length; ++ii) System.out.println(info[ii]);
			this.publishMessage("raspPiLLrf", "pulser/send/status", "localControlPulser", info[0], 0);
		}
		
	}
	public static String[] runExternalProcess(String command, boolean linux, boolean getInfo) throws Exception 
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
	public static void main(String[] args) throws Exception 
	{
		LocalControl localControl = new LocalControl("tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5");
		localControl.subscribe("raspPiLLrf", "pulser/#", "localControlLLrf", 0);
//		localControl.subscribe("raspPiLLrf", "setup", "localControlLLrf", 0);
//		localControl.subscribe("raspPiLLrf", "onOff", "localControlLLrf", 0);
//		localControl.subscribe("raspPiLLrf", "status", "localControlLLrf", 0);
	}

}
