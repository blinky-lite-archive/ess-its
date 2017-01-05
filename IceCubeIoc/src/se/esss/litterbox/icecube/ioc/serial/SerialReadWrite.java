package se.esss.litterbox.icecube.ioc.serial;


import java.util.concurrent.CountDownLatch;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import java.util.concurrent.TimeUnit;

public class SerialReadWrite extends SerialPort
{
	PortReader portReader;
	String receivedString = "";
	boolean waitingForData = false;
	CountDownLatch countDownLatch = null;
	public SerialReadWrite(String portName) throws Exception 
	{
		super(portName);
	    openPort();
	    setParams(SerialPort.BAUDRATE_9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);

	    setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
	    portReader = new PortReader(this);
	    addEventListener(portReader, SerialPort.MASK_RXCHAR);
	}
	public void writeStringData(String data) throws Exception
	{
		waitingForData = false;
		writeString(data);
	}
	public String writeReadStringData(String data, int secsToWait) throws Exception
	{
		if (waitingForData) throw new Exception("Already waiting for data!");
		waitingForData = true;
		receivedString = "";
		countDownLatch = new CountDownLatch(1);
		writeString(data);
		countDownLatch.await((long) secsToWait, TimeUnit.SECONDS); 
		waitingForData = false;
		return receivedString;
	}
	private void addStringData(int numBytes)
	{
		if (!waitingForData) return;
		String incomingString = null;
		try {incomingString = readString(numBytes);} catch (SerialPortException e) {e.printStackTrace();}
		int newLinePos = incomingString.indexOf('\n');
		if (newLinePos < 0)
		{
			receivedString = receivedString + incomingString;
		}
		else
		{
			receivedString = receivedString + incomingString.substring(0, newLinePos);
			receivedString = receivedString.trim();
			countDownLatch.countDown();
		}
	}

	private static class PortReader implements SerialPortEventListener 
	{
		SerialReadWrite serialReadWrite;
		PortReader(SerialReadWrite serialReadWrite)
		{
			this.serialReadWrite = serialReadWrite;
		}
		@Override
		public void serialEvent(SerialPortEvent event) 
		{
	        if(event.isRXCHAR() && event.getEventValue() > 0) serialReadWrite.addStringData(event.getEventValue());
		}

	}
	public static void main(String[] args) throws Exception
	{
		if (args.length < 3)
		{
			System.out.println("Usage: port data timeout");
			System.exit(0);
		}
		String dev = args[0];
		String command = args[1];
		int timeout = Integer.parseInt(args[2]);
		SerialReadWrite jssc = null;
		try
		{
			jssc = new SerialReadWrite("/dev/" + dev);
			String readData = jssc.writeReadStringData(command, timeout);
			System.out.println(readData);
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}
		jssc.closePort();
	}
}
