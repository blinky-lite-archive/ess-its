package se.esss.litterbox.icecube.ioc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import se.esss.litterbox.icecube.bytegearbox.ByteGearBox;

public abstract class ItsByteGearBoxIoc extends IceCubePeriodicPollIoc
{
	private ByteGearBox byteGearBox;
	private Socket socket = null;
	private Socket getSocket() {return socket;}
	private ArrayList<byte[]> writeMessageBuffer = new ArrayList<byte[]>();
	private String byteGearBoxFilePath;

	public ByteGearBox getByteGearBox() {return byteGearBox;}

	public ItsByteGearBoxIoc(String clientId, int periodicPollPeriodmillis, String byteGearBoxFilePath, String gizmoInetAddress, int gizmoPortNumber, String mqttBrokerInfoFilePath, int keepAliveInterval) throws Exception 
	{
		super(clientId, mqttBrokerInfoFilePath, keepAliveInterval);
		byteGearBox = new ByteGearBox(byteGearBoxFilePath);
		writeMessageBuffer.add(byteGearBox.getWriteData());
		this.socket = new Socket( gizmoInetAddress, gizmoPortNumber);
		setPeriodicPollPeriodmillis(periodicPollPeriodmillis);
		startIoc(byteGearBox.getTopic() + "/set", byteGearBox.getTopic() + "/get");
    	subscribe(byteGearBox.getTopic() + "/getJson", getSubscribeQos());
    	this.byteGearBoxFilePath = byteGearBoxFilePath;
	}
	private void sendBytes(byte[] myByteArray)  throws Exception
	{
	    byteGearBox.setWriteData(myByteArray);
	    OutputStream out = getSocket().getOutputStream(); 
	    DataOutputStream dos = new DataOutputStream(out);
	    dos.write(myByteArray, 0, myByteArray.length);
	}
	private byte[] receiveBytes(int len) throws Exception
	{
	    InputStream in = getSocket().getInputStream();
	    DataInputStream dis = new DataInputStream(in);
	    byte[] data = new byte[len];
	    for (int ii = 0; ii < len; ++ii) data[ii] = 0;
	    int icount = 0;
//		setStatus(icount + "\tlen" + len);
	    try
	    {
		    while (icount < len)
		    {
		    	data[icount] = dis.readByte();
//				setStatus(icount + "\t" + data[icount]);
		    	++icount;
		    }
	    } catch (Exception e)
	    {
	    	setStatus(e.getMessage());
	    } 	    
	    byteGearBox.setReadData(data);
//	    printReadData(data);
	    return data;
	    
/*	    byte[] data = new byte[len];
	    int numBytes = dis.read(data);
	    byte[] readData = new byte[numBytes];
	    for (int ii = 0; ii < numBytes; ++ii) 
	    {
	    	readData[ii] = data[ii];
	    }
	    byteGearBox.setReadData(readData);
	    return readData;
*/
	}
	public void printReadData(byte[] data) throws Exception
	{
//		PrintWriter pw = new PrintWriter(this.getClientId() + "ReadData.out");
		PrintWriter pw = new PrintWriter(System.out);
		for (int ii = 0; ii < data.length; ++ii)
		{
			pw.println("(" + ii + ",\t" + data[ii] + ")\t");
		}
		pw.println();
		byteGearBox.setReadData(data);
		String[] readDataStringArray = byteGearBox.printReadData();
		for (int ii = 0; ii < readDataStringArray.length; ++ii)
		{
			pw.println(readDataStringArray[ii]);
		}
		
		pw.close();
	}

	@Override
	public byte[] getDataFromGizmo() 
	{
		try 
		{
			setStatus("messageBufferSiz = " + writeMessageBuffer.size());
			sendBytes(writeMessageBuffer.get(0));
//			setStatus("Sent Bytes");
			if (writeMessageBuffer.size() > 1) writeMessageBuffer.remove(0);
		} 
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
		if (topic.equals(byteGearBox.getTopic() + "/set"))
		{
			int numWriteMessages = message.length / byteGearBox.getWriteByteLength();
			for (int ii = 0; ii < numWriteMessages; ++ii)
			{
				byte[] newMessage = new byte[byteGearBox.getWriteByteLength()];
				for (int ij = 0; ij < byteGearBox.getWriteByteLength(); ++ij)
				{
					newMessage[ij] = message[ij + byteGearBox.getWriteByteLength() * ii];
				}
				writeMessageBuffer.add(newMessage);
			}
		    byteGearBox.setWriteData(writeMessageBuffer.get(writeMessageBuffer.size() - 1));
			try {byteGearBox.writeToFile(byteGearBoxFilePath, false);} catch (Exception e) {}

		}
		if (topic.equals(byteGearBox.getTopic() + "/getJson"))
		{
			try {publishMessage(byteGearBox.getTopic() + "/json", byteGearBox.getJsonObject().toJSONString().getBytes(), getPublishQos(), true);} catch (Exception e) {}
		}
	}
}
