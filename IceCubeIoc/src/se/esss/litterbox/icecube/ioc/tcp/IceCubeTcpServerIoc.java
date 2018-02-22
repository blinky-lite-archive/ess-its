package se.esss.litterbox.icecube.ioc.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import se.esss.litterbox.icecube.ioc.IceCubePeriodicPollIoc;

public abstract class IceCubeTcpServerIoc extends IceCubePeriodicPollIoc 
{
	ServerSocket serverSocket;
	Socket clientSocket;

	public IceCubeTcpServerIoc(String clientId, String mqttBrokerInfoFilePath, int portNumber, int keepAliveInterval) throws Exception 
	{
		super(clientId, mqttBrokerInfoFilePath, keepAliveInterval);
		setStatus("Waiting for client to accept...");
		serverSocket = new ServerSocket(portNumber);
		clientSocket = serverSocket.accept();
		setStatus("...Client accepted");
	}
	public void sendBytes(byte[] myByteArray)  throws Exception
	{
	    OutputStream out = clientSocket.getOutputStream(); 
	    DataOutputStream dos = new DataOutputStream(out);
	    dos.write(myByteArray, 0, myByteArray.length);
	}
	public byte[] receiveBytesFully(int len) 
	{
		try
		{
			InputStream in = clientSocket.getInputStream();
			DataInputStream dis = new DataInputStream(in);

			byte[] data = new byte[len];
			dis.readFully(data);
			return data;
		} catch (Exception e)
		{
			 return null;
		}
	}
	public byte[] receiveBytes(int len) throws Exception
	{
	    InputStream in = clientSocket.getInputStream();
	    DataInputStream dis = new DataInputStream(in);

	    byte[] data = new byte[len];
	    int numBytes = dis.read(data);
	    byte[] readData = new byte[numBytes];
	    for (int ii = 0; ii < numBytes; ++ii) readData[ii] = data[ii];
	    return readData;
	}
}
