package se.esss.litterbox.its.archivergwt.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ArchiveJsonData  implements Serializable
{
	private String topic;
	private String deviceName;
	private long startTime;
	private long stopTime;
	private double[] timeData = null;
	private double[] deviceData = null;
	private double multiplier = 1.0;
	private int trace = -1;
	
	public String getTopic() {return topic;}
	public String getDeviceName() {return deviceName;}
	public long getStartTime() {return startTime;}
	public long getStopTime() {return stopTime;}
	public double[] getTimeData() {return timeData;}
	public double[] getDeviceData() {return deviceData;}
	public double getMultiplier() {return multiplier;}
	public int getTrace() {return trace;}

	public void setTopic(String topic) {this.topic = topic;}
	public void setDeviceName(String deviceName) {this.deviceName = deviceName;}
	public void setStartTime(long startTime) {this.startTime = startTime;}
	public void setStopTime(long stoptime) {this.stopTime = stoptime;}
	public void setTimeData(double[] timeData) {this.timeData = timeData;}
	public void setDeviceData(double[] deviceData) {this.deviceData = deviceData;}
	public void setMultiplier(double multiplier) {this.multiplier = multiplier;}
	public void setTrace(int trace) {this.trace = trace;}
	public ArchiveJsonData()
	{
		
	}
	public double[] getMultipliedDeviceData()
	{
		double[] multData = new double[deviceData.length];
		for (int ii = 0; ii < deviceData.length; ++ii) multData[ii] = deviceData[ii] * multiplier;
		return multData;
	}

}
