package se.esss.litterbox.icecube.ioc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;

import se.esss.litterbox.icecube.bytegearbox.ByteGearBox;

public class ItsByteGearBoxIoc extends IceCubePeriodicPollIoc
{
	private ByteGearBox byteGearBox;
	private Socket socket = null;
	private Socket getSocket() {return socket;}

	public ItsByteGearBoxIoc(String clientId, int periodicPollPeriodmillis, String byteGearBoxUrl, String gizmoInetAddress, int gizmoPortNumber, String mqttBrokerInfoFilePath, String byteGearBoxWebLoginInfo) throws Exception 
	{
		super(clientId, mqttBrokerInfoFilePath);
		byteGearBox = new ByteGearBox(new URL(byteGearBoxUrl), byteGearBoxWebLoginInfo);
		this.socket = new Socket( gizmoInetAddress, gizmoPortNumber);
		setPeriodicPollPeriodmillis(periodicPollPeriodmillis);
		startIoc(byteGearBox.getTopic() + "/set", byteGearBox.getTopic() + "/get");
	}
	private void sendBytes(byte[] myByteArray)  throws Exception
	{
	    OutputStream out = getSocket().getOutputStream(); 
	    DataOutputStream dos = new DataOutputStream(out);
	    dos.write(myByteArray, 0, myByteArray.length);
	}
	private byte[] receiveBytes(int len) throws Exception
	{
	    InputStream in = getSocket().getInputStream();
	    DataInputStream dis = new DataInputStream(in);

	    byte[] data = new byte[len];
	    int numBytes = dis.read(data);
	    byte[] readData = new byte[numBytes];
	    for (int ii = 0; ii < numBytes; ++ii) readData[ii] = data[ii];
	    return readData;
	}

	@Override
	public byte[] getDataFromGizmo() 
	{
		try {sendBytes(byteGearBox.getWriteData());} 
		catch (Exception e) 
		{
			setStatus("Error in sending data: " + e.getMessage());
			return null;
		}
		try {Thread.sleep(100);} catch (InterruptedException e) {}
		try {return receiveBytes(byteGearBox.getReadByteLength());} 
		catch (Exception e) 
		{
			setStatus("Error in reading data: " + e.getMessage());
			return null;
		}
	}

	@Override
	public void handleBrokerMqttMessage(String topic, byte[] message) 
	{
		byteGearBox.setWriteData(message);
	}
}
