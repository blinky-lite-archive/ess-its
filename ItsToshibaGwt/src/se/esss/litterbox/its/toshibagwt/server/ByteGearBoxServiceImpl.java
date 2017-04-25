package se.esss.litterbox.its.toshibagwt.server;

import java.io.File;
import java.net.URL;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import se.esss.litterbox.icecube.bytegearbox.ByteGear;
import se.esss.litterbox.icecube.bytegearbox.ByteGearBox;
import se.esss.litterbox.icecube.bytegearbox.ByteTooth;
import se.esss.litterbox.icecube.simplemqtt.SimpleMqttClient;
import se.esss.litterbox.its.toshibagwt.client.bytegearboxservice.ByteGearBoxService;
import se.esss.litterbox.its.toshibagwt.shared.bytegearboxgwt.ByteGearBoxGwt;
import se.esss.litterbox.its.toshibagwt.shared.bytegearboxgwt.ByteGearGwt;
import se.esss.litterbox.its.toshibagwt.shared.bytegearboxgwt.ByteToothGwt;


@SuppressWarnings("serial")
public class ByteGearBoxServiceImpl extends RemoteServiceServlet implements ByteGearBoxService
{
	ByteGearBoxServiceImpClient byteGearBoxServiceImpClient;
	ByteGearBox[] byteGearBox;
	String[] gearBoxUrls = {
			"https://aig.esss.lu.se:8443/ItsByteGearBoxServer/gearbox/klyPlcProtoCpu.json",
			"https://aig.esss.lu.se:8443/ItsByteGearBoxServer/gearbox/klyPlcProtoAio.json",
			"https://aig.esss.lu.se:8443/ItsByteGearBoxServer/gearbox/klyPlcProtoDio.json",
			"https://aig.esss.lu.se:8443/ItsByteGearBoxServer/gearbox/klyPlcProtoPsu.json"};
	
/*	String[] gearBoxUrls = {
			"https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/gearbox/klyPlcProtoCpu.json",
			"https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/gearbox/klyPlcProtoAio.json",
			"https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/gearbox/klyPlcProtoDio.json",
			"https://aig.esss.lu.se:8443/IceCubeDeviceProtocols/gearbox/klyPlcProtoPsu.json"};
*/
	
	public void init()
	{
		byteGearBox = new ByteGearBox[gearBoxUrls.length];
		try 
		{
			boolean cleanSession = false;
			int subscribeQos = 0;
			byteGearBoxServiceImpClient = new ByteGearBoxServiceImpClient(this, "ItsToshibaPlcWebApp", getMqttDataPath(), cleanSession);
			String itsnetWebLoginInfo = getItsnetWebLoginInfoPath();
			for (int ii = 0; ii < gearBoxUrls.length; ++ii)
			{
//				byteGearBox[ii] = new ByteGearBox(new URL(gearBoxUrls[ii]));
				byteGearBox[ii] = new ByteGearBox(new URL(gearBoxUrls[ii]), itsnetWebLoginInfo);
				byteGearBoxServiceImpClient.subscribe(byteGearBox[ii].getTopic() + "/set", subscribeQos);
				byteGearBoxServiceImpClient.subscribe(byteGearBox[ii].getTopic() + "/get", subscribeQos);
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	public void destroy()
	{
		
	}
	private String getMqttDataPath() throws Exception
	{
		File tmpFile = new File(getServletContext().getRealPath("./"));
		tmpFile = new File(tmpFile.getParent());
		tmpFile = new File(tmpFile.getParent());
		return tmpFile.getPath() + "/itsmqttbroker.dat";
		
	}
	public String getItsnetWebLoginInfoPath() throws Exception
	{
		File tmpFile = new File(getServletContext().getRealPath("./"));
		tmpFile = new File(tmpFile.getParent());
		tmpFile = new File(tmpFile.getParent());
		return tmpFile.getPath() + "/itsnetWebLoginInfo.dat";
		
	}
	public void setMessage(String topic, byte[] message)
	{
		for (int ii = 0; ii < byteGearBox.length; ++ii)
		{
			if (topic.indexOf(byteGearBox[ii].getTopic()) >= 0)
			{
				if (topic.indexOf("/set") >= 0)
				{
					byteGearBox[ii].setWriteData(message);
					return;
				}
				if (topic.indexOf("/get") >= 0)
				{
					byteGearBox[ii].setReadData(message);
					return;
				}
			}
		}
	}
	static class ByteGearBoxServiceImpClient extends SimpleMqttClient
	{
		ByteGearBoxServiceImpl byteGearBoxServiceImpl;
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
