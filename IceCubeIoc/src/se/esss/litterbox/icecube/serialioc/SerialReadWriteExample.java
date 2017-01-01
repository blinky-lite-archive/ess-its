package se.esss.litterbox.icecube.serialioc;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class SerialReadWriteExample 
{

	public SerialReadWriteExample() 
	{
		// TODO Auto-generated constructor stub
	}
	private static class PortReader implements SerialPortEventListener 
	{
		SerialPort serialPort;
		PortReader(SerialPort serialPort)
		{
			this.serialPort = serialPort;
		}
	    @Override
	    public void serialEvent(SerialPortEvent event) 
	    {
	        if(event.isRXCHAR() && event.getEventValue() > 0) 
	        {
	            try 
	            {
	                String receivedData = serialPort.readString(event.getEventValue());
	                System.out.println("Received response: " + receivedData);
	            }
	            catch (SerialPortException ex) 
	            {
	                System.out.println("Error in receiving string from COM-port: " + ex);
	            }
	        }
	    }

	}

	public static void main(String[] args) 
	{
		SerialPort serialPort = new SerialPort("/dev/rfcomm1");
		try 
		{
		    serialPort.openPort();

		    serialPort.setParams(SerialPort.BAUDRATE_9600,
		                         SerialPort.DATABITS_8,
		                         SerialPort.STOPBITS_1,
		                         SerialPort.PARITY_NONE);

		    serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | 
		                                  SerialPort.FLOWCONTROL_RTSCTS_OUT);

		    serialPort.addEventListener(new PortReader(serialPort), SerialPort.MASK_RXCHAR);

		    serialPort.writeString("aboutGet");
		}
		catch (SerialPortException ex) {
		    System.out.println("There are an error on writing string to port: " + ex);
		}
	}

}
