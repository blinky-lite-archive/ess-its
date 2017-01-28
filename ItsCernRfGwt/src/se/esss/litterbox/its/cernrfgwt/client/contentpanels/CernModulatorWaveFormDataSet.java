package se.esss.litterbox.its.cernrfgwt.client.contentpanels;

public class CernModulatorWaveFormDataSet 
{
	private double timeStamp;
	private double vprim;
	private double iprim;
	private double vcath;
	private double icath;
	
	public double getTimeStamp() {return timeStamp;}
	public double getVprim() {return vprim;}
	public double getIprim() {return iprim;}
	public double getVcath() {return vcath;}
	public double getIcath() {return icath;}
	
	public CernModulatorWaveFormDataSet(int ipoint, byte[] waveformByteData)
	{
		
		byte[] data = new byte[4];
		for (int ii = 0; ii < 4; ++ii) data[ii] = waveformByteData[12 * ipoint + ii];
		int itimeStamp = bytearray2int(data);
		timeStamp = (double) itimeStamp;
		vprim = (double) sdata(4, ipoint, waveformByteData);
		iprim = (double) sdata(6, ipoint, waveformByteData);
		vcath = (double) sdata(8, ipoint, waveformByteData);
		icath = (double) sdata(10, ipoint, waveformByteData);
		vprim = vprim / 100.0;
		vcath = vcath / 10.0;
		icath = icath / 10.0;
		
	}
	private int sdata(int startByte, int ipoint, byte[] waveformByteData)
	{
		byte[] data = new byte[4];
		data[0] = 0;
		data[1] = 0;
		data[2] = waveformByteData[12 * ipoint + startByte];
		data[3] = waveformByteData[12 * ipoint + startByte + 1];
		int sdata =  bytearray2int(data);
		if (sdata > 32767) sdata = 65535 - sdata;
		return sdata;
	}
	public static int bytearray2int(byte[] barray)
	 {
		int i = (barray[0]<<24)&0xff000000|
			       (barray[1]<<16)&0x00ff0000|
			       (barray[2]<< 8)&0x0000ff00|
			       (barray[3]<< 0)&0x000000ff;	 
		return i;
	}

}
