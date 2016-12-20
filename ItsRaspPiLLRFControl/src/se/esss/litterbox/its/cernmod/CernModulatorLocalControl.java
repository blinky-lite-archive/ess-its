package se.esss.litterbox.its.cernmod;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

import se.esss.litterbox.icetray.icecubedevice.FileToStringArray;
import se.esss.litterbox.icetray.icecubedevice.IceCubeDeviceList;
import se.esss.litterbox.its.utilities.Utilities;
import se.esss.litterbox.simplemqttclient.SimpleMqttSubscriber;

public class CernModulatorLocalControl extends SimpleMqttSubscriber  implements Runnable
{
	private Socket clientSocket;
	private IceCubeDeviceList cernModulatorSettingList = null;
	private IceCubeDeviceList cernModulatorReadingList = null;
	private boolean runPeriodicPoll = true;
	private int subscribeQos = 0;
	private int publishQos = 0;

	public int getPublishQos() {return publishQos;}
	public int getSubscribeQos() {return subscribeQos;}
	public Socket getClientSocket() {return clientSocket;}
	public boolean isRunPeriodicPoll() {return runPeriodicPoll;}
	
	public void setClientSocket(Socket clientSocket) {this.clientSocket = clientSocket;}
	public void setRunPeriodicPoll(boolean runPeriodicPoll) {this.runPeriodicPoll = runPeriodicPoll;}
	public void setSubscribeQos(int subscribeQos) {this.subscribeQos = subscribeQos;}
	public void setPublishQos(int publishQos) {this.publishQos = publishQos;}

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
		while (!isConnected())
		{
			try
			{
				Thread.sleep(5000);
				setStatus("Lost connection. Trying to reconnect." );
				boolean cleanSession = true;
				subscribe("its", "cernmodulator/toModulator/#", subscribeQos, cleanSession);
			} catch (Exception e) {setStatus("Error: " + e.getMessage());}
		}
	}
	private void putSettings() throws Exception
	{
			Utilities.sendBytes(cernModulatorSettingList.getByteArray(), clientSocket);
        	setStatus("...data sent");
			setStatus("reading data...");
    		byte[] readData = Utilities.receiveBytes(clientSocket, cernModulatorReadingList.numberOfBytes());
    		cernModulatorReadingList.putByteArray(readData);
			setStatus("...data read.");
        	setStatus("Awaiting command...");
	}
	@Override
	public void newMessage(String domain, String topic, byte[] message) 
	{
		setStatus(getClientId() + "  on domain: " + domain + " recieved message on topic: " + topic);
		if (domain.equals("its"))
		{
			if (topic.equals("cernmodulator/toModulator/put/set"))
			{
				try 
				{
					cernModulatorSettingList.putByteArray(message);
					cernModulatorSettingList.getDevice("send mon values").setValue("1");
				} catch (Exception e) 
				{
					setStatus("Error: " + e.getMessage());
				}
			}
		}
	}
	@Override
	public void run() 
	{
		while (this.isRunPeriodicPoll())
		{
			try 
			{
				putSettings();
				boolean retained = true;
				publishMessage("its",  "cernmodulator/fromModulator/get/set", cernModulatorSettingList.getByteArray(), publishQos, retained);
				publishMessage("its",  "cernmodulator/fromModulator/get/read", cernModulatorReadingList.getByteArray(), publishQos, retained);
				Thread.sleep(1000);
			}
			catch (Exception e) {setStatus("Error: " + e.getMessage());}
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
//		URL cernmodSettingUrl = new URL("https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolSet.csv");
//		URL cernmodReadingUrl = new URL("https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/protocols/CernModulatorProtocolRead.csv");
		cernModulatorLocalControl.setupDeviceLists(cernmodSettingUrl, cernmodReadingUrl);
		cernModulatorLocalControl.setStatus("Waiting for client to accept...");
		ServerSocket serverSocket = new ServerSocket(portNumber, 20, addr);
		Socket clientSocket = serverSocket.accept();
		cernModulatorLocalControl.setStatus("...Client accepted");
		cernModulatorLocalControl.setStatus("Awaiting command...");
		cernModulatorLocalControl.setClientSocket(clientSocket);
		boolean cleanSession = true;
		cernModulatorLocalControl.subscribe("its", "cernmodulator/toModulator/#", cernModulatorLocalControl.getSubscribeQos(), cleanSession);
		new Thread(cernModulatorLocalControl).start();
		
        serverSocket.close();
	}


}
