package se.esss.litterbox.its.cernmod;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

import se.esss.litterbox.icetray.icecubedevice.FileToStringArray;
import se.esss.litterbox.icetray.icecubedevice.IceCubeDeviceList;
import se.esss.litterbox.its.utilities.Utilities;
import se.esss.litterbox.simplemqttclient.SimpleMqttSubscriber;

public class CernModulatorLocalControl extends SimpleMqttSubscriber
{
	private Socket clientSocket;
	private IceCubeDeviceList cernModulatorSettingList = null;
	private IceCubeDeviceList cernModulatorReadingList = null;

	public Socket getClientSocket() {return clientSocket;}
	public void setClientSocket(Socket clientSocket) {this.clientSocket = clientSocket;}

	public CernModulatorLocalControl(String clientIdBase, String brokerUrl, String brokerKey, String brokerSecret) 
	{
		super(clientIdBase, brokerUrl, brokerKey, brokerSecret);
	}
	public void setupDeviceLists(URL cernmodSettingProtocolUrl, URL cernmodReadingProtocolUrl) throws Exception
	{
		cernModulatorSettingList = new IceCubeDeviceList(FileToStringArray.fileToStringArray(cernmodSettingProtocolUrl));
		cernModulatorReadingList = new IceCubeDeviceList(FileToStringArray.fileToStringArray(cernmodReadingProtocolUrl));
	}
	@Override
	public void connectionLost(Throwable arg0) 
	{
		try
		{
			Thread.sleep(5000);
			subscribe("its", "cernmodulator/toModulator/#", 0);
		} catch (Exception e) {setStatus("Error: " + e.getMessage());}
	}
	@Override
	public void newMessage(String domain, String topic, byte[] message) 
	{
		setStatus(getClientId() + "  on domain: " + domain + " recieved message on topic: " + topic);
		if (domain.equals("its"))
		{
			if (topic.equals("cernmodulator/toModulator/set/forget"))
			{
				try
				{
					cernModulatorSettingList.putByteArray(message);
		        	Utilities.sendBytes(cernModulatorSettingList.getByteArray(), clientSocket);
		        	setStatus("...data sent");
		        	setStatus("Awaiting command...");
				} catch (Exception e) {setStatus("Error: " + e.getMessage());}
			}
			if (topic.equals("cernmodulator/toModulator/set/read"))
			{
				try
				{
					cernModulatorSettingList.putByteArray(message);
					Utilities.sendBytes(cernModulatorSettingList.getByteArray(), clientSocket);
					setStatus("...data sent");
					setStatus("reading data...");
		    		byte[] readData = Utilities.receiveBytes(clientSocket, cernModulatorReadingList.numberOfBytes());
		    		cernModulatorReadingList.putByteArray(readData);
					publishMessage(domain,  "cernmodulator/fromModulator/echo/read", cernModulatorReadingList.getByteArray(), 0);
					setStatus("...data read.");
					setStatus("Awaiting command...");
				} catch (Exception e) {setStatus("Error: " + e.getMessage());}
			}
			if (topic.equals("cernmodulator/toModulator/echo/set"))
			{
				try
				{
					publishMessage(domain,  "cernmodulator/fromModulator/echo/set", cernModulatorSettingList.getByteArray(), 0);
				} catch (Exception e) {setStatus("Error: " + e.getMessage());}
			}
			if (topic.equals("cernmodulator/toModulator/echo/read"))
			{
				try
				{
					publishMessage(domain,  "cernmodulator/fromModulator/echo/read", cernModulatorReadingList.getByteArray(), 0);
				} catch (Exception e) {setStatus("Error: " + e.getMessage());}
			}
		}
	}
	public static void main(String[] args) throws Exception 
	{
		System.out.println("Integration Test Stand CernModulatorLocalControl ver 1.1 David McGinnis 05-Nov-2016 14:36");
		int portNumber = 8000;
        InetAddress addr = InetAddress.getByName("192.168.5.4");
        CernModulatorLocalControl cernModulatorLocalControl = new CernModulatorLocalControl("cernModulatorLocalControl", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5");
		URL cernmodSettingUrl = new URL("http://192.168.0.105:8080/IceCubeDeviceProtocols/protocols/CernModulatorProtocolSet.csv");
		URL cernmodReadingUrl = new URL("http://192.168.0.105:8080/IceCubeDeviceProtocols/protocols/CernModulatorProtocolRead.csv");
		cernModulatorLocalControl.setupDeviceLists(cernmodSettingUrl, cernmodReadingUrl);
		cernModulatorLocalControl.setStatus("Waiting for client to accept...");
		ServerSocket serverSocket = new ServerSocket(portNumber, 20, addr);
		Socket clientSocket = serverSocket.accept();
		cernModulatorLocalControl.setStatus("...Client accepted");
		cernModulatorLocalControl.setStatus("Awaiting command...");
		cernModulatorLocalControl.setClientSocket(clientSocket);
		cernModulatorLocalControl.subscribe("its", "cernmodulator/toModulator/#", 0);
		
        serverSocket.close();
	}


}
