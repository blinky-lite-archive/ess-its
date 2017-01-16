package se.esss.litterbox.icecube.bytedevice;

import java.io.Serializable;

public class ByteDevice implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String name = null;
	private int byteStart = -1;
	private int bitLocation = -1;
	private String type = null;
	private int multiplier = 0;
	private String value;
	private String min;
	private String max;
	private String comment = null;
	
	public String getName() {return name;}
	public int getByteStart() {return byteStart;}
	public int getBitLocation() {return bitLocation;}
	public String getType() {return type;}
	public int getMultiplier() {return multiplier;}
	public String getValue() {return value;}
	public String getMin() {return min;}
	public String getMax() {return max;}
	public String getComment() {return comment;}
	
	public void setName(String name) {this.name = name;}
	public void setByteStart(int byteStart) {this.byteStart = byteStart;}
	public void setBitLocation(int bitLocation) {this.bitLocation = bitLocation;}
	public void setType(String type) {this.type = type;}
	public void setMultiplier(int multiplier) {this.multiplier = multiplier;}
	public void setValue(String value) {this.value = value;}
	public void setMin(String min) {this.min = min;}
	public void setMax(String max) {this.max = max;}
	public void setComment(String comment) {this.comment = comment;}
	
	public ByteDevice()
	{
		
	}
	public ByteDevice(String line)
	{
		String[] data = line.split(",");
		name = data[0];
		byteStart = Integer.parseInt(data[1]);
		bitLocation = Integer.parseInt(data[2]);
		type = data[3];
		multiplier = Integer.parseInt(data[4]);
		value = data[5]; 
		min = data[6];
		max = data[7];
		comment = data[8];
//		System.out.println(csvLine());
	}
	public String csvLine()
	{
		String line = getName() + ",";
		line = line + Integer.toString(getByteStart()) + ",";
		line = line + Integer.toString(getBitLocation()) + ",";
		line = line + getType() + ",";
		line = line + Integer.toString(getMultiplier()) + ",";
		line = line + getValue() + ",";
		line = line + getMin() + ",";
		line = line + getMax() + ",";
		line = line + getComment();
		return line;
	}
}
