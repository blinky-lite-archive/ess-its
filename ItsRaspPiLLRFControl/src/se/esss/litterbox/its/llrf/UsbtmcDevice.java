package se.esss.litterbox.its.llrf;

import java.util.Date;

import se.esss.litterbox.its.utilities.Utilities;

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
		return Utilities.runExternalProcess(cmd, true, true);
	}
	public String[] read(String command) throws Exception
	{
		if (echoCommand) System.out.println(new Date().toString() + " Reading to " + devNickName + ": " + command);
		String cmd = "python usbtmcRead.py " + Integer.toString(vendorId) + " " + Integer.toString(productId) + " \"" + command + "\"";
		return Utilities.runExternalProcess(cmd, true, true);
	}

	public static void main(String[] args) throws Exception 
	{
		UsbtmcDevice usbtmcDevice = new UsbtmcDevice("Tek Scope", 1689, 927);
		System.out.println(usbtmcDevice.read("*IDN?")[0]);
		usbtmcDevice.write("*RST");

	}

}
