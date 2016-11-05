package se.esss.litterbox.its.utilities;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Utilities 
{
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
	public static void sendBytes(byte[] myByteArray, Socket socket) throws Exception 
	{
	    OutputStream out = socket.getOutputStream(); 
	    DataOutputStream dos = new DataOutputStream(out);
	    dos.write(myByteArray, 0, myByteArray.length);
	}
	public static byte[] receiveBytes(Socket socket, int len) throws Exception
	{
	    InputStream in = socket.getInputStream();
	    DataInputStream dis = new DataInputStream(in);

	    byte[] data = new byte[len];
	    dis.readFully(data);
	    return data;
	}

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

	}

}
