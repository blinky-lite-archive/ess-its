package se.esss.litterbox.its.llrfremotecontrol;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class LlrfDataJson 
{
	private double rfFreq = 352.21;
	private double rfPowLvl = -10;
	private boolean rfPowOn  = true;
	private double rfPulseWidth = 0.3;
	private boolean rfPulseOn  = true;
	private double modRiseTime = 0.3;
	private double modRepRate = 1;
	private boolean modPulseOn  = true;
	private double rfPowRead1  = -70.0;
	private double rfPowRead2  = -70.0;
	private long setupState = -1;
	
	public double getRfFreq() {return rfFreq;}
	public double getRfPowLvl() {return rfPowLvl;}
	public boolean isRfPowOn() {return rfPowOn;}
	public double getRfPulseWidth() {return rfPulseWidth;}
	public boolean isRfPulseOn() {return rfPulseOn;}
	public double getModRiseTime() {return modRiseTime;}
	public double getModRepRate() {return modRepRate;}
	public boolean isModPulseOn() {return modPulseOn;}
	public double getRfPowRead1() {return rfPowRead1;}
	public double getRfPowRead2() {return rfPowRead2;}
	public long getSetupState() {return setupState;}
	
	public void setRfFreq(double rfFreq) {this.rfFreq = rfFreq;}
	public void setRfPowLvl(double rfPowLvl) {this.rfPowLvl = rfPowLvl;}
	public void setRfPowOn(boolean rfPowOn) {this.rfPowOn = rfPowOn;}
	public void setRfPulseWidth(double rfPulseWidth) {this.rfPulseWidth = rfPulseWidth;}
	public void setRfPulseOn(boolean rfPulseOn) {this.rfPulseOn = rfPulseOn;}
	public void setModRiseTime(double modRiseTime) {this.modRiseTime = modRiseTime;}
	public void setModRepRate(double modRepRate) {this.modRepRate = modRepRate;}
	public void setModPulseOn(boolean modPulseOn) {this.modPulseOn = modPulseOn;}
	public void setRfPowRead1(double rfPowRead1) {this.rfPowRead1 = rfPowRead1;}
	public void setRfPowRead2(double rfPowRead2) {this.rfPowRead2 = rfPowRead2;}
	public void setSetupState(long setupState) {this.setupState = setupState;}

	public LlrfDataJson()
	{
	}
	public LlrfDataJson(String jsonString) throws Exception
	{
		setDataFromJsonString(jsonString);
	}
	public void setDataFromJsonString(String jsonString) throws Exception
	{
		JSONParser parser = new JSONParser();		
		JSONObject jsonData = (JSONObject) parser.parse(jsonString);
		setRfFreq((Double) jsonData.get("rfFreq") );
		setRfPowLvl((Double) jsonData.get("rfPowLvl")) ;
		setRfPowOn((Boolean) jsonData.get("rfPowOn")) ;
		setRfPulseWidth((Double) jsonData.get("rfPulseWidth")) ;
		setRfPulseOn((Boolean) jsonData.get("rfPulseOn")) ;
		setModRiseTime((Double) jsonData.get("modRiseTime")) ;
		setModRepRate((Double) jsonData.get("modRepRate")) ;
		setModPulseOn((Boolean) jsonData.get("modPulseOn")) ;
		setRfPowRead1((Double) jsonData.get("rfPowRead1")) ;
		setRfPowRead2((Double) jsonData.get("rfPowRead2")) ;
		setSetupState((Long) jsonData.get("setupState")) ;
	}
	@SuppressWarnings("unchecked")
	public String writeJsonString()
	{
		JSONObject setupData = new JSONObject();	
		setupData.put("rfFreq", new Double(getRfFreq())); 
		setupData.put("rfPowLvl", new Double(getRfPowLvl())); 
		setupData.put("rfPowOn", new Boolean(isRfPowOn())); 
		setupData.put("rfPulseWidth", new Double(getRfPulseWidth())); 
		setupData.put("rfPulseOn", new Boolean(isRfPulseOn())); 
		setupData.put("modRiseTime", new Double(getModRiseTime())); 
		setupData.put("modRepRate", new Double(getModRepRate())); 
		setupData.put("modPulseOn", new Boolean(isModPulseOn())); 
		setupData.put("rfPowRead1", new Double(getRfPowRead1())); 
		setupData.put("rfPowRead2", new Double(getRfPowRead2())); 
		setupData.put("setupState", new Long(getSetupState())); 
		return setupData.toJSONString();
	}
}
