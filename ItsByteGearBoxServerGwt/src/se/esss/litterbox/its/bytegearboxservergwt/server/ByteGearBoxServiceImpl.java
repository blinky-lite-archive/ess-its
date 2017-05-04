package se.esss.litterbox.its.bytegearboxservergwt.server;

import java.io.File;
import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import se.esss.litterbox.icecube.bytegearbox.ByteGear;
import se.esss.litterbox.icecube.bytegearbox.ByteGearBox;
import se.esss.litterbox.icecube.bytegearbox.ByteTooth;
import se.esss.litterbox.icecube.simplemqtt.SimpleMqttClient;
import se.esss.litterbox.its.bytegearboxservergwt.client.bytegearboxservice.ByteGearBoxService;
import se.esss.litterbox.its.bytegearboxservergwt.shared.bytegearboxgwt.ByteGearBoxGwt;
import se.esss.litterbox.its.bytegearboxservergwt.shared.bytegearboxgwt.ByteGearGwt;
import se.esss.litterbox.its.bytegearboxservergwt.shared.bytegearboxgwt.ByteToothGwt;


@SuppressWarnings("serial")
public class ByteGearBoxServiceImpl extends RemoteServiceServlet implements ByteGearBoxService
{
	ByteGearBoxServiceImpClient byteGearBoxServiceImpClient;
	ByteGearBox[] byteGearBox;
	ArrayList<String> byteGearBoxJsonFiles;
	String gearBoxDirPath;
	
	public void init()
	{
		try 
		{
			boolean cleanSession = false;
			int subscribeQos = 0;
			byteGearBoxServiceImpClient = new ByteGearBoxServiceImpClient(this, "ItsByteGearBoxServerWebApp", getMqttDataPath(), cleanSession);
			gearBoxDirPath = getGearBoxPath();
			File folder = new File(gearBoxDirPath);
			File[] listOfFiles = folder.listFiles();
			byteGearBoxJsonFiles = new ArrayList<String>();
			for (int ii = 0; ii < listOfFiles.length; ii++) 
			{
				if (listOfFiles[ii].isFile()) 
				{
					if (listOfFiles[ii].getName().contains(".json"))
						byteGearBoxJsonFiles.add(listOfFiles[ii].getName());
				}
			}
			java.util.Collections.sort(byteGearBoxJsonFiles);
			byteGearBox = new ByteGearBox[byteGearBoxJsonFiles.size()];
			for (int ii = 0; ii < byteGearBoxJsonFiles.size(); ii++) 
			{
				{
					byteGearBox[ii] = new ByteGearBox(gearBoxDirPath  + "/" + byteGearBoxJsonFiles.get(ii));
					byteGearBoxServiceImpClient.subscribe(byteGearBox[ii].getTopic() + "/set", subscribeQos);
					byteGearBoxServiceImpClient.subscribe(byteGearBox[ii].getTopic() + "/get", subscribeQos);
				}
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	public void destroy()
	{
		try 
		{
			byteGearBoxServiceImpClient.reconnectOk = false;
			byteGearBoxServiceImpClient.unsubscribeAll();
			byteGearBoxServiceImpClient.disconnect();
			System.out.println("Ending ItsByteGearBoxServerWebApp");
		} catch (Exception e) 
		{
			System.out.println("Error: " + e.getMessage());

		}
	}
	private String getGearBoxPath() throws Exception
	{
		File tmpFile = new File(getServletContext().getRealPath("./"));
		tmpFile = new File(tmpFile.getParent());
		tmpFile = new File(tmpFile.getParent());
		return tmpFile.getPath() + "/IceCubeDeviceProtocols/gearbox";
		
	}
	private String getMqttDataPath() throws Exception
	{
		File tmpFile = new File(getServletContext().getRealPath("./"));
		tmpFile = new File(tmpFile.getParent());
		tmpFile = new File(tmpFile.getParent());
		return tmpFile.getPath() + "/itsmqttbroker.dat";
		
	}
	static class ByteGearBoxServiceImpClient extends SimpleMqttClient
	{
		ByteGearBoxServiceImpl byteGearBoxServiceImpl;
		boolean reconnectOk = true;
		public ByteGearBoxServiceImpClient(ByteGearBoxServiceImpl byteGearBoxServiceImpl, String clientId, String mqttBrokerInfoFilePath, boolean cleanSession) throws Exception 
		{
			super(clientId, mqttBrokerInfoFilePath, cleanSession);
			this.byteGearBoxServiceImpl = byteGearBoxServiceImpl; 
		}
		@Override
		public void newMessage(String topic, byte[] message) 
		{
			byteGearBoxServiceImpl.setMessage(topic, message);
		}
		@Override
		public void lostMqttConnection(Throwable arg0) 
		{
			if (!reconnectOk)
			{
				try 
				{
					unsubscribeAll();
					disconnect();
					return;
				} catch (Exception e) {setStatus("Error on disconnect: " + arg0.getMessage());}
			}
			try {reconnect();} catch (Exception e) {setStatus("Error on reconnect: " + arg0.getMessage());}
		}
	}
	public void setMessage(String topic, byte[] message)
	{
		for (int ii = 0; ii < byteGearBox.length; ++ii)
		{
			if (topic.indexOf(byteGearBox[ii].getTopic()) >= 0)
			{
				if (topic.indexOf("/set") >= 0)
				{
					int numWriteMessages = message.length / byteGearBox[ii].getWriteByteLength();
					byte[] lastMessage = new byte[byteGearBox[ii].getWriteByteLength()];
					for (int ij = 0; ij < byteGearBox[ii].getWriteByteLength(); ++ij)
					{
						lastMessage[ij] = message[ij + byteGearBox[ii].getWriteByteLength() * (numWriteMessages - 1)];
					}
					byteGearBox[ii].setWriteData(lastMessage);
					try {byteGearBox[ii].writeToFile(gearBoxDirPath  + "/" + byteGearBoxJsonFiles.get(ii), false);} catch (Exception e) {e.printStackTrace();}
					return;
				}
				if (topic.indexOf("/get") >= 0)
				{
					int numReadMessages = message.length / byteGearBox[ii].getReadByteLength();
					byte[] lastMessage = new byte[byteGearBox[ii].getReadByteLength()];
					for (int ij = 0; ij < byteGearBox[ii].getReadByteLength(); ++ij)
					{
						lastMessage[ij] = message[ij + byteGearBox[ii].getReadByteLength() * (numReadMessages - 1)];
					}
					byteGearBox[ii].setReadData(lastMessage);
					return;
				}
			}
		}
	}
	private static ByteToothGwt convertByteTooth(ByteTooth byteTooth)
	{
		ByteToothGwt byteToothGwt = new ByteToothGwt(
				byteTooth.getName(), 
				byteTooth.getType(), 
				byteTooth.getByteOffsetFromGear(), 
				byteTooth.getBitOffsetFromGear(), 
				byteTooth.isWriteable(), 
				byteTooth.getValue(), 
				byteTooth.getDescription());
		return byteToothGwt;
		
	}
	private static ByteGearGwt convertByteGear(ByteGear byteGear)
	{
		ByteGearGwt byteGearGwt = new ByteGearGwt(
				byteGear.getName(), 
				byteGear.getReadOffset(), 
				byteGear.getWriteOffset());
		for (int ii = 0; ii < byteGear.getReadToothList().size(); ++ii)
		{
			byteGearGwt.getReadToothList().add(convertByteTooth(byteGear.getReadToothList().get(ii)));
		}
		for (int ii = 0; ii < byteGear.getWriteToothList().size(); ++ii)
		{
			byteGearGwt.getWriteToothList().add(convertByteTooth(byteGear.getWriteToothList().get(ii)));
		}
		return byteGearGwt;
		
	}
	private static ByteGearBoxGwt convertByteGearBox(ByteGearBox byteGearBox)
	{
		ByteGearBoxGwt byteGearBoxGwt = new ByteGearBoxGwt(
				byteGearBox.getBroker(), 
				byteGearBox.getBrokerPort(), 
				byteGearBox.getTopic(), 
				byteGearBox.getReadByteLength(), 
				byteGearBox.getWriteByteLength());
		for (int ii = 0; ii < byteGearBox.getByteGearList().size(); ++ii)
		{
			byteGearBoxGwt.getByteGearList().add(convertByteGear(byteGearBox.getByteGearList().get(ii)));
		}
		return byteGearBoxGwt;
		
	}
	@Override
	public ByteGearBoxGwt[] getByteGearBoxGwt()
	{
		ByteGearBoxGwt[] byteGearBoxGwt = new ByteGearBoxGwt[byteGearBox.length];
		for (int ii = 0; ii < byteGearBox.length; ++ii)
			byteGearBoxGwt[ii] = convertByteGearBox(byteGearBox[ii]);
		return byteGearBoxGwt;
	}
	@Override
	public byte[][] getReadWriteMessage(String topic) throws Exception
	{
		byte[][] readWriteMessage = new byte[2][];
		for (int ii = 0; ii < byteGearBox.length; ++ii)
		{
			if (topic.indexOf(byteGearBox[ii].getTopic()) >= 0)
			{
				readWriteMessage[0] = byteGearBox[ii].getReadData();
				readWriteMessage[1] = byteGearBox[ii].getWriteData();
				return readWriteMessage;
			}
		}
		throw new Exception("Topic not found");
	}
	@Override
	public byte[] getReadMessage(String topic) throws Exception
	{
		for (int ii = 0; ii < byteGearBox.length; ++ii)
		{
			if (topic.indexOf(byteGearBox[ii].getTopic()) >= 0)
			{
				return byteGearBox[ii].getReadData();
			}
		}
		throw new Exception("Topic not found");
	}
	@Override
	public byte[] getWriteMessage(String topic) throws Exception
	{
		for (int ii = 0; ii < byteGearBox.length; ++ii)
		{
			if (topic.indexOf(byteGearBox[ii].getTopic()) >= 0)
			{
				return byteGearBox[ii].getWriteData();
			}
		}
		throw new Exception("Topic not found");
	}
	@Override
	public Long getlastUpdateDate(String topic) throws Exception
	{
		for (int ii = 0; ii < byteGearBox.length; ++ii)
		{
			if (topic.indexOf(byteGearBox[ii].getTopic()) >= 0)
			{
				return new Long(byteGearBox[ii].getLastDataUpdate().getTime());
			}
		}
		throw new Exception("Topic not found");
	}
	@Override
	public Long getlastReadUpdateDate(String topic) throws Exception
	{
		for (int ii = 0; ii < byteGearBox.length; ++ii)
		{
			if (topic.indexOf(byteGearBox[ii].getTopic()) >= 0)
			{
				return new Long(byteGearBox[ii].getLastReadDataUpdate().getTime());
			}
		}
		throw new Exception("Topic not found");
	}
	@Override
	public Long getlastWriteUpdateDate(String topic) throws Exception
	{
		for (int ii = 0; ii < byteGearBox.length; ++ii)
		{
			if (topic.indexOf(byteGearBox[ii].getTopic()) >= 0)
			{
				return new Long(byteGearBox[ii].getLastWriteDataUpdate().getTime());
			}
		}
		throw new Exception("Topic not found");
	}
	@Override
	public Long[] getlastReadWriteUpdateDate(String topic) throws Exception
	{
		Long[] readWriteUpdate = new Long[2];
		for (int ii = 0; ii < byteGearBox.length; ++ii)
		{
			if (topic.indexOf(byteGearBox[ii].getTopic()) >= 0)
			{
				readWriteUpdate[0] = new Long(byteGearBox[ii].getLastReadDataUpdate().getTime());
				readWriteUpdate[1] = new Long(byteGearBox[ii].getLastWriteDataUpdate().getTime());
				return readWriteUpdate;
			}
		}
		throw new Exception("Topic not found");
	}
	@Override
	public String publishMessage(String topic, byte[] message, boolean settingsEnabled) throws Exception 
	{
		if (!settingsEnabled) throw new Exception("Settings to Mqtt Broker are not permitted");
		byteGearBoxServiceImpClient.publishMessage(topic, message, 0, true);
		return "ok";
	}

}
