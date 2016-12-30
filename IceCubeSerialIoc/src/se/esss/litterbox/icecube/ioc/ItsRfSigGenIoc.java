package se.esss.litterbox.icecube.ioc;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import se.esss.litterbox.icecube.usbtmcioc.IceCubeUsbtmcIoc;

public class ItsRfSigGenIoc extends IceCubeUsbtmcIoc
{

	public ItsRfSigGenIoc(String clientId, String brokerUrl, String brokerKey, String brokerSecret, String devNickName, int vendorId, int productId) throws Exception 
	{
		super(clientId, brokerUrl, brokerKey, brokerSecret, devNickName, vendorId, productId);
		writeUsbtmcData("*RST");
		try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
		writeUsbtmcData("FREQ 352.21MHZ");
		writeUsbtmcData("POW -30");
		writeUsbtmcData("OUTP OFF");
		writeUsbtmcData("PULM:SOUR EXT");
		writeUsbtmcData("SOUR:PULM:TRIG:EXT:IMP G10K");
		writeUsbtmcData("PULM:STAT ON");
	}
	@Override
	public byte[] getUsbtmcData() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void handleIncomingMessage(String topic, byte[] message) 
	{
		if (topic.indexOf("/set/rf") >= 0)
		{
			try
			{
				JSONParser parser = new JSONParser();		
				JSONObject jsonData = (JSONObject) parser.parse(new String(message));
				writeUsbtmcData("OUTP " + (String) jsonData.get("rfPowOn"));
				writeUsbtmcData("FREQ " + (String) jsonData.get("rfFreq") + "MHZ");
				writeUsbtmcData("POW " + (String) jsonData.get("rfPowLvl"));
			}
			catch (ParseException nfe) {}
		}
	}
	public static void main(String[] args) throws Exception 
	{
		String userName = args[0];
		String password = args[1];
		String broker = "tcp://broker.shiftr.io:1883";
		ItsRfSigGenIoc ioc = new ItsRfSigGenIoc("itsRfSigGen01Ioc", broker, userName, password, "RF Sig Gen", 2733, 72);
		ioc.setPeriodicPollPeriodmillis(2000);
		ioc.startIoc("itsRfSigGen01/set/#", "itsRfSigGen01/get");
	}


}
