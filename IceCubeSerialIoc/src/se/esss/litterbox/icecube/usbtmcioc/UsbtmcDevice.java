package se.esss.litterbox.icecube.usbtmcioc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

public class UsbtmcDevice 
{
	private int vendorId;
	private int productId;
	private boolean echoCommand = false;
	String devNickName;

	public boolean isEchoCommand() {return echoCommand;}
	public void setEchoCommand(boolean echoCommand) {this.echoCommand = echoCommand;}
	
	public UsbtmcDevice(String devNickName, int vendorId, int productId) 
	{
		this.productId = productId;
		this.vendorId = vendorId;
		this.devNickName = devNickName;
	}
	public String[] write(String command) throws Exception
	{
		if (echoCommand) System.out.println(new Date().toString() + " Writing to " + devNickName + ": " + command);
		String cmd = "python usbtmcWrite.py " + Integer.toString(vendorId) + " " + Integer.toString(productId) + " \"" + command + "\"";
		return runExternalProcess(cmd, true, true);
	}
	public String[] read(String command) throws Exception
	{
		if (echoCommand) System.out.println(new Date().toString() + " Reading to " + devNickName + ": " + command);
		String cmd = "python usbtmcRead.py " + Integer.toString(vendorId) + " " + Integer.toString(productId) + " \"" + command + "\"";
		return runExternalProcess(cmd, true, true);
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
		UsbtmcDevice usbtmcDevice = new UsbtmcDevice("Tek Scope", 1689, 927);
		System.out.println(usbtmcDevice.read("*IDN?")[0]);
		usbtmcDevice.write("*RST");

	}

}
