package se.esss.litterbox.itsllrfgwt.shared;

import java.io.Serializable;

public class LlrfData implements Serializable
{
	private static final long serialVersionUID = 2494964406588874148L;
	private double rfFreq = 352.21;
	private double rfPowLvl = -50;
	private boolean rfPowOn  = false;
	private double rfPulseWidth = 0.01;
	private boolean rfPulseOn  = false;
	private double modRiseTime = 0.6;
	private double modRepRate = 0.1;
	private boolean modPulseOn  = false;
	private double rfPowRead1  = -70.0;
	private double rfPowRead2  = -70.0;
	
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
	
	public LlrfData()
	{
		
	}

}