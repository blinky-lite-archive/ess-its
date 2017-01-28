package se.esss.litterbox.its.cernrfgwt.client.contentpanels;


public class CernModulatorWaveForm 
{
	private double[] timeStamp;
	private double[] vprim;
	private double[] iprim;
	private double[] vcath;
	private double[] icath;

	public double[] getTimeStamp() {return timeStamp;}
	public double[] getVprim() {return vprim;}
	public double[] getIprim() {return iprim;}
	public double[] getVcath() {return vcath;}
	public double[] getIcath() {return icath;}

	public CernModulatorWaveForm(byte[] waveformByteData)
	{
		timeStamp = new double[300];
		vprim = new double[300];
		iprim = new double[300];
		vcath = new double[300];
		icath = new double[300];
		
		CernModulatorWaveFormDataSet waveFormPt;
		for (int ii = 0; ii < 300; ++ii)
		{
			waveFormPt = new CernModulatorWaveFormDataSet(ii, waveformByteData);
			timeStamp[ii] = waveFormPt.getTimeStamp();
			vprim[ii] = waveFormPt.getVprim();
			iprim[ii] = waveFormPt.getIprim();
			vcath[ii] = waveFormPt.getVcath();
			icath[ii] = waveFormPt.getIcath();
		}
		double startTimeOffset = timeStamp[0];
		for (int ii = 0; ii < 300; ++ii)  timeStamp[ii] = timeStamp[ii] - startTimeOffset;
//		fakeData();
	}
/*	private void fakeData()
	{
		double phase = Math.random() * 6.283185308;
		for (int ii = 0; ii < 300; ++ii)
		{
			timeStamp[ii] = ((double) ii) * 10.0;
			double theta = phase + 6.283185308 * timeStamp[ii] / 3000.0;
			vprim[ii] = 1.0 * Math.cos(theta * 1.0);
			iprim[ii] = 2.0 * Math.cos(theta * 2.0);
			vcath[ii] = 3.0 * Math.cos(theta * 3.0);
			icath[ii] = 4.0 * Math.cos(theta * 4.0);
		}
	}
*/
}
