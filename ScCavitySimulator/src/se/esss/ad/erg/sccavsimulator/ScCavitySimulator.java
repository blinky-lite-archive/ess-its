package se.esss.ad.erg.sccavsimulator;

import java.io.FileNotFoundException;

import se.esss.litterbox.numericalrecipes.Complex;


public class ScCavitySimulator
{
	static final double PI = Math.PI;
	static final double degToRad = PI / 180.0;
	static final double radToDeg = 180.0 / PI;
	static final double TWOPI = 2.0 * PI;
	
	private int numSimPts = 10000;
	private double rfFreqMHz = 704;
	private double r_Q_accel = 477;
	private double cavityVoltageMV = 17;
	private double referenceCavityVoltageMV = 17;
	private double beamCurrentmA = 40.0;
	private double setBeamCurrentmA = 40.0;
	private double referenceBeamCurrentmA = 50;
	private double phiSynchDeg = 20;
	private double setPhiSynchDeg = 20;
	private double referencePhiSynchDeg = 20.0;
	private double phiDdc = 20.0;
	private double setPhiDdc = 20.0;
	private double tBeamStartmS = 0.238623;
	private double setTBeamStartmS = 0.238623;
	private double tBeamPulsemS = 2.8;
	private double setTBeamPulsemS = 2.8;
	private double tEndmS = 4.0;
	
	private Complex[] cavVolts  = null;
	private Complex[] rfbeamCurrent  = null;
	private Complex[] genCurrent  = null;
	private double[] tanPhiD = null;
	

	public ScCavitySimulator(double referenceCavityVoltageMV, double r_Q_accel, double rfFreqMHz, double referenceBeamCurrentmA, double referencePhiSynchDeg, double tEndmS, int numSimPts)
	{
		this.referenceCavityVoltageMV = referenceCavityVoltageMV;
		this.r_Q_accel = r_Q_accel;
		this.rfFreqMHz = rfFreqMHz;
		this.referenceBeamCurrentmA = referenceBeamCurrentmA;
		this.referencePhiSynchDeg = referencePhiSynchDeg;
		this.tEndmS = tEndmS;
		this.numSimPts = numSimPts;
	}
	private void initializeArrays()
	{
		cavVolts = new Complex[numSimPts];
		rfbeamCurrent = new Complex[numSimPts];
		genCurrent = new Complex[numSimPts];
		tanPhiD = new double[numSimPts];
		for (int ii = 0; ii < numSimPts; ++ii)
		{
			cavVolts[ii] = new Complex();
			rfbeamCurrent[ii] = new Complex();
			genCurrent[ii] = new Complex();
			tanPhiD[ii] = 0.0;
		}
	}
	private double deltaSimTime()
	{
		return tEndmS * 0.001 / ((double) numSimPts - 1);
	}
	public double simTime(int ii)
	{
		return deltaSimTime() * ((double) ii);
	}
	private Complex iGenBeamGuess(double beamCurrentmA, double phiSynchDeg, double cavityVoltageMV, double tanPhiDdc)
	{
		Complex ibeam = new Complex(Math.cos(phiSynchDeg * degToRad), Math.sin(phiSynchDeg * degToRad));
		ibeam = ibeam.times(beamCurrentmA * .001 * 2.0);
		Complex stauFill = new Complex(1.0, -tanPhiDdc);
		Complex iGenBeam = new Complex(ibeam);
		iGenBeam = iGenBeam.plus(stauFill.times(cavityVoltageMV * 1.0e+6 / R()));
		return iGenBeam;
	}
	public double tanPhiDdcGuess(double beamCurrentmA, double phiSynchDeg, double cavityVoltageMV)
	{
		double k = beamCurrentmA * 2.0 * .001 * R() * Math.sin(phiSynchDeg * degToRad) / (cavityVoltageMV * 1.0e+06);
//		System.out.println("detuning angle = " + radToDeg * Math.atan(k));

		return k;
	}
	private Complex iGenFillGuess(double cavityVoltageMV, double tanPhiDdc, double tBeamStartmS)
	{
		Complex s = new Complex(1.0, -tanPhiDdc).dividedBy(cavFillTime());
		Complex factor = new Complex(s.times(-tBeamStartmS * 0.001)).exponential();
		factor = new Complex(1.0,0.0).minus(factor);
		factor = factor.dividedBy(s.times(cavFillTime()));
		Complex igenFill = new Complex(cavityVoltageMV * 1.0e+06 / R(), 0.0).dividedBy(factor);
		return igenFill;
	}
	public double optTstart(double tStartmSGuess, double precession, int ntry, double beamCurrentmA, double phiSynchDeg, double cavityVoltageMV, double tanPhiDdc )
	{
		double optTstart = tStartmSGuess;
		double optTstart1;
		double optTstart2;
		double igenBeam = iGenBeamGuess(beamCurrentmA, phiSynchDeg, cavityVoltageMV, tanPhiDdc).magnitude();
		double error1;
		double error2;
		
		for (int ii = 0; ii < ntry; ++ii)
		{
			optTstart1 = optTstart;
			error1 = igenBeam - iGenFillGuess(cavityVoltageMV, tanPhiDdc, optTstart1).magnitude();
			
			optTstart2 = optTstart1 + precession;
			error2 = igenBeam - iGenFillGuess(cavityVoltageMV, tanPhiDdc, optTstart2).magnitude();
			optTstart = optTstart1 - error1 * (optTstart2 - optTstart1) / (error2 - error1);
		}
//		System.out.println("optimum tstartmS = " + optTstart);
		return optTstart;
	}
	private void setupWaveFormGuess()
	{
		Complex iGenBeam = iGenBeamGuess(setBeamCurrentmA, setPhiSynchDeg, cavityVoltageMV, Math.tan(degToRad * setPhiDdc));
		Complex igenFill = iGenFillGuess(cavityVoltageMV, Math.tan(degToRad * setPhiDdc), setTBeamStartmS);
		Complex ibeam = new Complex(Math.cos(phiSynchDeg * degToRad),Math.sin(phiSynchDeg * degToRad));
		ibeam = ibeam.times(beamCurrentmA * .001 * 2.0);
		
		for (int ii = 0; ii < numSimPts; ++ii)
		{
			if (simTime(ii) < setTBeamStartmS * 0.001) 
			{
				genCurrent[ii] = new Complex(igenFill);
			}
			else
			{
				if (simTime(ii) > (setTBeamStartmS + setTBeamPulsemS) * 0.001)  
				{
					genCurrent[ii] = new Complex();
				}
				else
				{
					genCurrent[ii] = new Complex(iGenBeam);
				}
			}
			if (simTime(ii) < tBeamStartmS * 0.001)
			{
				rfbeamCurrent[ii] = new Complex();
				tanPhiD[ii] = Math.tan(degToRad * phiDdc);
			}
			else
			{
				if (simTime(ii) > (tBeamStartmS + tBeamPulsemS) * 0.001)  
				{
					rfbeamCurrent[ii] = new Complex();
					tanPhiD[ii] = Math.tan(degToRad * phiDdc);
				}
				else
				{
					rfbeamCurrent[ii] = new Complex(ibeam);
					tanPhiD[ii] = Math.tan(degToRad * phiDdc);
				}
			}
		}
	}
	private double R()
	{
		return loadedQ() * r_Q_accel * 0.5;
	}
	public Complex gamma(int ii)
	{
		Complex gamma = new Complex(cavVolts[ii]);
		gamma = gamma.dividedBy(genCurrent[ii].times(R() / 2.0));
		gamma = gamma.minus(1.0);
		return gamma;
	}
	public double klystronPower(int ii)
	{
		double klystronPower = genCurrent[ii].magnitudeSquared() * R() / 8.0;
		return klystronPower;
	}
	public double loadedQ() 
	{
		return referenceCavityVoltageMV * 1.0e+06 
				/ (r_Q_accel * 0.5 * 2.0 * referenceBeamCurrentmA * 0.001 * Math.cos(referencePhiSynchDeg * degToRad));
	}
	public double cavFillTime()
	{
		double cavFillTime = loadedQ() / (PI * rfFreqMHz * 1.0e+06);
		return cavFillTime;
	}
	public Complex deltaV(int ii)
	{
		Complex stau = new Complex(1, -tanPhiD[ii]);
		Complex deltaV = genCurrent[ii].minus(rfbeamCurrent[ii]);
		deltaV = deltaV.times(R());
		deltaV = deltaV.minus(cavVolts[ii].times(stau));
		deltaV = deltaV.times(deltaSimTime() / cavFillTime());
		return deltaV;
	}
	public void simpleSimulate() 
	{
		initializeArrays();
		setupWaveFormGuess();
		int ii = 0;
		
		for (ii = 1; ii < numSimPts; ++ii)
		{
			cavVolts[ii] = cavVolts[ii - 1].plus(deltaV(ii - 1));
		}
 	}
	
	public int getNumSimPts() {return numSimPts;}
	public double getRfFreqMHz() {return rfFreqMHz;}
	public double getR_Q_accel() {return r_Q_accel;}
	public double getCavityVoltageMV() {return cavityVoltageMV;}
	public double getReferenceCavityVoltageMV() {return referenceCavityVoltageMV;}
	public double getBeamCurrentmA() {return beamCurrentmA;}
	public double getSetBeamCurrentmA() {return setBeamCurrentmA;}
	public double getReferenceBeamCurrentmA() {return referenceBeamCurrentmA;}
	public double getPhiSynchDeg() {return phiSynchDeg;}
	public double getSetPhiSynchDeg() {return setPhiSynchDeg;}
	public double getReferencePhiSynchDeg() {return referencePhiSynchDeg;}
	public double getPhiDdc() {return phiDdc;}
	public double getSetPhiDdc() {return setPhiDdc;}
	public double gettBeamStartmS() {return tBeamStartmS;}
	public double getSetTBeamStartmS() {return setTBeamStartmS;}
	public double gettBeamPulsemS() {return tBeamPulsemS;}
	public double getSetTBeamPulsemS() {return setTBeamPulsemS;}
	public double gettEndmS() {return tEndmS;}
	
	public void setNumSimPts(int numSimPts) {this.numSimPts = numSimPts;}
	public void setRfFreqMHz(double rfFreqMHz) {this.rfFreqMHz = rfFreqMHz;}
	public void setR_Q_accel(double r_Q_accel) {this.r_Q_accel = r_Q_accel;}
	public void setCavityVoltageMV(double cavityVoltageMV) {this.cavityVoltageMV = cavityVoltageMV;}
	public void setReferenceCavityVoltageMV(double referenceCavityVoltageMV) {this.referenceCavityVoltageMV = referenceCavityVoltageMV;}
	public void setBeamCurrentmA(double beamCurrentmA) {this.beamCurrentmA = beamCurrentmA;}
	public void setSetBeamCurrentmA(double setBeamCurrentmA) {this.setBeamCurrentmA = setBeamCurrentmA;}
	public void setReferenceBeamCurrentmA(double referenceBeamCurrentmA) {this.referenceBeamCurrentmA = referenceBeamCurrentmA;}
	public void setPhiSynchDeg(double phiSynchDeg) {this.phiSynchDeg = phiSynchDeg;}
	public void setSetPhiSynchDeg(double setPhiSynchDeg) {this.setPhiSynchDeg = setPhiSynchDeg;}
	public void setReferencePhiSynchDeg(double referencePhiSynchDeg) {this.referencePhiSynchDeg = referencePhiSynchDeg;}
	public void setPhiDdc(double phiDdc) {this.phiDdc = phiDdc;}
	public void setSetPhiDdc(double setPhiDdc) {this.setPhiDdc = setPhiDdc;}
	public void settBeamStartmS(double tBeamStartmS) {this.tBeamStartmS = tBeamStartmS;}
	public void setSetTBeamStartmS(double setTBeamStartmS) {this.setTBeamStartmS = setTBeamStartmS;}
	public void settBeamPulsemS(double tBeamPulsemS) {this.tBeamPulsemS = tBeamPulsemS;}
	public void setSetTBeamPulsemS(double setTBeamPulsemS) {this.setTBeamPulsemS = setTBeamPulsemS;}
	public void settEndmS(double tEndmS) {this.tEndmS = tEndmS;}
	
	public void setCavVolts(Complex[] cavVolts) {this.cavVolts = cavVolts;}
	public void setRfbeamCurrent(Complex[] rfbeamCurrent) {this.rfbeamCurrent = rfbeamCurrent;}
	public void setGenCurrent(Complex[] genCurrent) {this.genCurrent = genCurrent;}
	public void setTanPhiD(double[] tanPhiD) {this.tanPhiD = tanPhiD;}

	public Complex[] getCavVolts() {return cavVolts;}
	public Complex[] getRfbeamCurrent() {return rfbeamCurrent;}
	public Complex[] getGenCurrent() {return genCurrent;}
	public double[] getTanPhiD() {return tanPhiD;}
	
	public static void main(String[] args) throws FileNotFoundException 
	{
//		ScCavitySimulator scs = new ScCavitySimulator();
		
//		tanPhiDdc = scs.tanPhiDdcGuess(beamCurrentmA, phiSynchDeg, cavityVoltageMV);
//		tBeamStartmS = scs.optTstart(0.2, .01, 5, beamCurrentmA, phiSynchDeg, cavityVoltageMV, tanPhiDdc);
		
		
	}

}
