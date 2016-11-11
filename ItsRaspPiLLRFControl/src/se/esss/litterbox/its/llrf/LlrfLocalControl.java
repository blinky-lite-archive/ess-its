package se.esss.litterbox.its.llrf;

import se.esss.litterbox.its.llrfremotecontrol.LlrfDataJson;
import se.esss.litterbox.simplemqttclient.SimpleMqttSubscriber;

public class LlrfLocalControl extends SimpleMqttSubscriber
{
	private LlrfDataJson llrfDataJson;

	private UsbtmcDevice sigGen     = new UsbtmcDevice("Pulse Gen     ", 2391, 11271);
	private UsbtmcDevice tekScope   = new UsbtmcDevice("Tek Scope     ", 1689, 927);
	private UsbtmcDevice rfSigGen   = new UsbtmcDevice("RF Sig Gen    ", 2733, 72);
	private UsbtmcDevice rfPowMeter = new UsbtmcDevice("RF Power Meter", 2733, 27);

	private double modPulseWidth = 0.100;
	private double modPulseVolt = 3.0;
	private double rfPulseVolt = 1.0;
	
	
	public LlrfDataJson getLlrfData() {return llrfDataJson;}

	public LlrfLocalControl(String clientIdBase, String brokerUrl, String brokerKey, String brokerSecret) 
	{
		super(clientIdBase, brokerUrl, brokerKey, brokerSecret);
		llrfDataJson = new LlrfDataJson();
		sigGen.setEchoCommand(true);
		tekScope.setEchoCommand(true);
		rfSigGen.setEchoCommand(true);
		rfPowMeter.setEchoCommand(true);
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
				subscribe("its", "llrf/#", 0);
			} catch (Exception e) {setStatus("Error: " + e.getMessage());}
		}
	}
	@Override
	public void newMessage(String domain, String topic, byte[] message) 
	{
		setStatus(getClientId() + "  on domain: " + domain + " recieved message on topic: " + topic);
		if (domain.equals("its"))
		{
			if (topic.equals("llrf/setup"))
			{
				try 
				{
					llrfDataJson.setDataFromJsonString(new String(message));
					setupLLRF();
					
				} catch (Exception e) {setStatus("Error: " + e.getMessage());}
			}
			if (topic.equals("llrf/change"))
			{
				try 
				{
					LlrfDataJson newLlrfData = new LlrfDataJson(new String(message));
					changeLLRF(newLlrfData);
				} catch (Exception e) {setStatus("Error: " + e.getMessage());}
			}
			if (topic.equals("llrf/ask/status"))
			{
				try 
				{
					getRfPowReading();
					publishMessage(domain, "llrf/send/status", llrfDataJson.writeJsonString().getBytes(), 0);
				} catch (Exception e) {setStatus("Error: " + e.getMessage());}
			}
		}		
	}
	private void getRfPowReading() throws Exception
	{
		String[] powerData = rfPowMeter.read("FETC1:SCAL:POW:AVG?");
		llrfDataJson.setRfPowRead(Double.parseDouble(powerData[0]));
	}

	private void setupLLRF() throws Exception
	{
		double horScale10 = Math.round(Math.log10((modPulseWidth + llrfDataJson.getModRiseTime() + llrfDataJson.getRfPulseWidth())/ 8.0));
		horScale10 = Math.pow(10.0,horScale10);
		double horScale = ((modPulseWidth + llrfDataJson.getModRiseTime() + llrfDataJson.getRfPulseWidth()) / 8.0) / horScale10;
		double horScale2 = 2.0;
		if (horScale > 4.0)
		{
			horScale2 = 10.0;
		}
		else if(horScale2 > 2)
		{
			horScale2 = 4.0;
		}
		horScale2 = 0.001 * horScale2 * horScale10;
		double phase = -0.36 * (llrfDataJson.getModRiseTime() + modPulseWidth) * llrfDataJson.getModRepRate();
		
		sigGen.write("OUTPUT1 0");
		sigGen.write("OUTPUT2 0");
		tekScope.write("*RST");
		sigGen.write("*RST");
		rfSigGen.write("*RST");
		rfPowMeter.write("SYST:PRES");
		
		tekScope.write("CH1:PROBE:GAIN 1");
		tekScope.write("CH1:VOLTS " + Double.toString(modPulseVolt * 0.5));
		tekScope.write("CH1:OFFSET " + Double.toString(modPulseVolt));
		tekScope.write("CH2:PROBE:GAIN 1");
		tekScope.write("CH2:VOLTS " + Double.toString(rfPulseVolt * 0.5));
		tekScope.write("CH2:OFFSET " + Double.toString(rfPulseVolt));
		tekScope.write("CH3:PROBE:GAIN 1");
		tekScope.write("CH3:VOLTS 0.005");
		tekScope.write("TRIGGER:A:EDGE:SOURCE CH1");
		tekScope.write("TRIGGER:A:LEVEL:CH1 " + Double.toString(rfPulseVolt * 0.25));
		tekScope.write("TRIGGER:A:MODE NORMAL");
		tekScope.write("SELECT:CH1 ON");
		tekScope.write("SELECT:CH2 ON");
		tekScope.write("SELECT:CH3 ON");
		tekScope.write("HORIZONTAL:SCALE " + Double.toString(horScale2));
		tekScope.write("HORIZONTAL:DELAY:TIME " + Double.toString(horScale2 * 4.0));

		sigGen.write("SOURCE1:APPLY:PULSE " + Double.toString(llrfDataJson.getModRepRate()) + "," + Double.toString(modPulseVolt) + "," + Double.toString(0.5 * modPulseVolt));
		sigGen.write("SOURCE1:FUNCTION:PULSE:WIDTH " + Double.toString(modPulseWidth * 0.001));
		sigGen.write("SOURCE2:APPLY:PULSE " + Double.toString(llrfDataJson.getModRepRate()) + "," + Double.toString(rfPulseVolt) + "," + Double.toString(0.5 * rfPulseVolt));
		sigGen.write("SOURCE2:FUNCTION:PULSE:WIDTH " + Double.toString(llrfDataJson.getRfPulseWidth() * 0.001));
		sigGen.write("SOURCE2:PHASE:SYNCHRONIZE");
		sigGen.write("SOURCE2:PHASE " + Double.toString(phase));
		if (llrfDataJson.isModPulseOn())
		{
			sigGen.write("OUTPUT1 1");
		}
		else
		{
			sigGen.write("OUTPUT1 0");
		}
		if (llrfDataJson.isRfPulseOn())
		{
			sigGen.write("OUTPUT2 1");
		}
		else
		{
			sigGen.write("OUTPUT2 0");
		}
		tekScope.write("TRIGGER FORCE");
		rfSigGen.write("PULM:SOUR EXT");
		rfSigGen.write("PULM:STAT ON");
		rfSigGen.write("FREQ " + Double.toString(llrfDataJson.getRfFreq()) + "MHZ");
		rfSigGen.write("POW " + Double.toString(llrfDataJson.getRfPowLvl()));
		if (llrfDataJson.isRfPowOn())
		{
			rfSigGen.write("OUTP ON");
		}
		else
		{
			rfSigGen.write("OUTP OFF");
		}
		double dutcycle = Math.round(100.0 * llrfDataJson.getModRepRate() * llrfDataJson.getRfPulseWidth() * 0.1) / 100.0;
		rfPowMeter.write("SENS1:FREQ " + Double.toString(llrfDataJson.getRfFreq()) + " MHZ");
		rfPowMeter.write("SENS1:CORR:DCYC " + Double.toString(dutcycle));
		rfPowMeter.write("SENS1:CORR:DCYC:STAT ON");
		rfPowMeter.write("SENS1:POW:AVG:APER " + Double.toString(1.0 / llrfDataJson.getModRepRate()));
	}
	private void changeLLRF(LlrfDataJson newLlrfDataJson) throws Exception
	{
		if ((llrfDataJson.getModRepRate() != newLlrfDataJson.getModRepRate()) || (llrfDataJson.getRfPulseWidth() != newLlrfDataJson.getRfPulseWidth()) || (llrfDataJson.getModRiseTime() != newLlrfDataJson.getModRiseTime()))
		{
			double phase = -0.36 * (newLlrfDataJson.getModRiseTime() + modPulseWidth) * newLlrfDataJson.getModRepRate();
			sigGen.write("OUTPUT1 0");
			sigGen.write("OUTPUT2 0");
			
			sigGen.write("SOURCE1:APPLY:PULSE " + Double.toString(newLlrfDataJson.getModRepRate()) + "," + Double.toString(modPulseVolt) + "," + Double.toString(0.5 * modPulseVolt));
			sigGen.write("SOURCE2:APPLY:PULSE " + Double.toString(newLlrfDataJson.getModRepRate()) + "," + Double.toString(rfPulseVolt) + "," + Double.toString(0.5 * rfPulseVolt));
			sigGen.write("SOURCE2:FUNCTION:PULSE:WIDTH " + Double.toString(newLlrfDataJson.getRfPulseWidth() * 0.001));
			sigGen.write("SOURCE2:PHASE:SYNCHRONIZE");
			sigGen.write("SOURCE2:PHASE " + Double.toString(phase));
			if (newLlrfDataJson.isModPulseOn())
			{
				sigGen.write("OUTPUT1 1");
			}
			else
			{
				sigGen.write("OUTPUT1 0");
			}
			if (newLlrfDataJson.isRfPulseOn())
			{
				sigGen.write("OUTPUT2 1");
			}
			else
			{
				sigGen.write("OUTPUT2 0");
			}
		}
		if (llrfDataJson.isModPulseOn() != newLlrfDataJson.isModPulseOn() )
		{
			if (newLlrfDataJson.isModPulseOn())
			{
				sigGen.write("OUTPUT1 1");
			}
			else
			{
				sigGen.write("OUTPUT1 0");
			}
		}
		if (llrfDataJson.isRfPulseOn() != newLlrfDataJson.isRfPulseOn() )
		{
			if (newLlrfDataJson.isRfPulseOn())
			{
				sigGen.write("OUTPUT2 1");
			}
			else
			{
				sigGen.write("OUTPUT2 0");
			}
		}
		if (llrfDataJson.isRfPowOn() != newLlrfDataJson.isRfPowOn() )
		{
			if (newLlrfDataJson.isRfPowOn())
			{
				rfSigGen.write("OUTP ON");
			}
			else
			{
				rfSigGen.write("OUTP OFF");
			}
		}
		if (llrfDataJson.getRfPowLvl() != newLlrfDataJson.getRfPowLvl() )
		{
			rfSigGen.write("POW " + Double.toString(newLlrfDataJson.getRfPowLvl()));
		}		
		if (llrfDataJson.getRfFreq() != newLlrfDataJson.getRfFreq() )
		{
			rfSigGen.write("FREQ " + Double.toString(newLlrfDataJson.getRfFreq()) + "MHZ");
			rfPowMeter.write("SENS1:FREQ " + Double.toString(newLlrfDataJson.getRfFreq()) + " MHZ");
		}		
		if ((llrfDataJson.getModRepRate() != newLlrfDataJson.getModRepRate()) || (llrfDataJson.getRfPulseWidth() != newLlrfDataJson.getRfPulseWidth()) )
		{
			double dutcycle = Math.round(100.0 * newLlrfDataJson.getModRepRate() * newLlrfDataJson.getRfPulseWidth() * 0.1) / 100.0;
			rfPowMeter.write("SENS1:CORR:DCYC " + Double.toString(dutcycle));
		}
		if (llrfDataJson.getModRepRate() != newLlrfDataJson.getModRepRate() )
		{
			rfPowMeter.write("SENS1:POW:AVG:APER " + Double.toString(1.0 / newLlrfDataJson.getModRepRate()));
		}

		llrfDataJson.setDataFromJsonString(newLlrfDataJson.writeJsonString());
	}
	public static void main(String[] args) throws Exception 
	{
		System.out.println("Integration Test Stand LlrfLocalControl ver 2.0 David McGinnis 10-Nov-2016 09:34");
		LlrfLocalControl llrfLocalControl = new LlrfLocalControl("llrfLocalControl", "tcp://broker.shiftr.io:1883", "c8ac7600", "1e45295ac35335a5");
		llrfLocalControl.subscribe("its", "llrf/#", 0);
		llrfLocalControl.setStatus("Ready for messages");
	}


}
