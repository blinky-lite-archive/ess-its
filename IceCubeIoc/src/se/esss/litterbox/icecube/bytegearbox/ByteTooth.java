package se.esss.litterbox.icecube.bytegearbox;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.json.simple.JSONObject;

public class ByteTooth 
{
	
	private static ByteOrder byteOrder =  ByteOrder.BIG_ENDIAN;

	private String name = "";
	private String description = "";
	private String toothType = "";
	private int byteOffsetFromGear = 0;
	private byte bitOffsetFromGear = 0;
	private String value = "";
	private boolean writeable = false;
	
	public String getName() {return name;}
	public String getDescription() {return description;}
	public String getType() {return toothType;}
	public int getByteOffsetFromGear() {return byteOffsetFromGear;}
	public int getBitOffsetFromGear() {return bitOffsetFromGear;}
	public String getValue() {return value;}
	public boolean isWriteable() {return writeable;}

	public void setValue(String value) {this.value = value;}
	
	public ByteTooth(String name, String toothType, int byteOffsetFromGear, int bitOffsetFromGear, boolean writeable, String value, String description)
	{
		this.name = name;
		this.description = description;
		this.toothType = toothType.toUpperCase();
		this.byteOffsetFromGear = byteOffsetFromGear;
		this.bitOffsetFromGear = (byte) bitOffsetFromGear;
		this.writeable = writeable;
		this.value = value;
	}
	public ByteTooth(JSONObject jsonObject)
	{
		this.name = (String) jsonObject.get("name");
		this.description = (String) jsonObject.get("description");
		this.toothType = (String) jsonObject.get("toothType");
		this.byteOffsetFromGear = (int) ((long) jsonObject.get("byteOff"));
		this.bitOffsetFromGear = (byte) ((long) jsonObject.get("bitOff"));
		this.writeable = (boolean) jsonObject.get("writeable");
		this.value = (String) jsonObject.get("value");
	}
	public void loadDataFromByteArray(byte[] byteArray, int byteGearOffset)
	{
		int offset = byteGearOffset + byteOffsetFromGear;
		if (toothType.equals("BOOLEAN"))
		{
			value = Boolean.toString(false);
			if (((byteArray[offset] >> bitOffsetFromGear) & 1) > 0) value = Boolean.toString(true);
		}
		if (toothType.equals("FLOAT"))
		{
			byte[] byteChunk = new byte[4];
			for (int ii = 0; ii < 4; ++ii) byteChunk[ii] = byteArray[offset + ii];
			ByteBuffer bb = ByteBuffer.wrap(byteArray);
			bb.order(byteOrder);
			value = Float.toString(bb.getFloat());
		}
		if (toothType.equals("BYTE"))
		{
			value = Byte.toString(byteArray[offset]);
		}
		if (toothType.equals("LONG"))
		{
			byte[] byteChunk = new byte[8];
			for (int ii = 0; ii < 8; ++ii) byteChunk[ii] = byteArray[offset + ii];
			ByteBuffer bb = ByteBuffer.wrap(byteArray);
			bb.order(byteOrder);
			value = Long.toString(bb.getLong());
		}
		if (toothType.equals("SHORT"))
		{
			byte[] byteChunk = new byte[2];
			for (int ii = 0; ii < 2; ++ii) byteChunk[ii] = byteArray[offset + ii];
			ByteBuffer bb = ByteBuffer.wrap(byteArray);
			bb.order(byteOrder);
			value = Short.toString(bb.getShort());
		}
		if (toothType.equals("INT"))
		{
			byte[] byteChunk = new byte[2];
			for (int ii = 0; ii < 4; ++ii) byteChunk[ii] = byteArray[offset + ii];
			ByteBuffer bb = ByteBuffer.wrap(byteArray);
			bb.order(byteOrder);
			value = Integer.toString(bb.getInt());
		}
		if (toothType.equals("DOUBLE"))
		{
			byte[] byteChunk = new byte[8];
			for (int ii = 0; ii < 8; ++ii) byteChunk[ii] = byteArray[offset + ii];
			ByteBuffer bb = ByteBuffer.wrap(byteArray);
			bb.order(byteOrder);
			value = Double.toString(bb.getDouble());
		}
	}
	public void loadDataIntoByteArray(byte[] byteArray, int byteGearWriteOffset) 
	{
		int offset = byteGearWriteOffset + byteOffsetFromGear;
		if (toothType.equals("BOOLEAN"))
		{
			if (Boolean.parseBoolean(value))
			{
				byteArray[offset] |= 1 << bitOffsetFromGear;
			}
			else
			{
				byteArray[offset] &= ~(1 << bitOffsetFromGear);
			}
		}
		if (toothType.equals("FLOAT"))
		{
			ByteBuffer bb = ByteBuffer.allocate(4);
			bb.order(byteOrder);
			byte[] byteChunk = bb.putFloat(Float.parseFloat(value)).array();
			for (int ii = 0; ii < 4; ++ii) byteArray[offset + ii] = byteChunk[ii];
		}
		if (toothType.equals("BYTE"))
		{
			byteArray[offset] = Byte.parseByte(value);
		}
		if (toothType.equals("LONG"))
		{
			ByteBuffer bb = ByteBuffer.allocate(8);
			bb.order(byteOrder);
			byte[] byteChunk = bb.putLong(Long.parseLong(value)).array();
			for (int ii = 0; ii < 8; ++ii) byteArray[offset + ii] = byteChunk[ii];
		}
		if (toothType.equals("SHORT"))
		{
			ByteBuffer bb = ByteBuffer.allocate(2);
			bb.order(byteOrder);
			byte[] byteChunk = bb.putShort(Short.parseShort(value)).array();
			for (int ii = 0; ii < 2; ++ii) byteArray[offset + ii] = byteChunk[ii];
		}
		if (toothType.equals("INT"))
		{
			ByteBuffer bb = ByteBuffer.allocate(4);
			bb.order(byteOrder);
			byte[] byteChunk = bb.putInt(Integer.parseInt(value)).array();
			for (int ii = 0; ii < 4; ++ii) byteArray[offset + ii] = byteChunk[ii];
		}
		if (toothType.equals("DOUBLE"))
		{
			ByteBuffer bb = ByteBuffer.allocate(8);
			bb.order(byteOrder);
			byte[] byteChunk = bb.putDouble(Double.parseDouble(value)).array();
			for (int ii = 0; ii < 8; ++ii) byteArray[offset + ii] = byteChunk[ii];
		}
	}
	@SuppressWarnings("unchecked")
	public JSONObject getJsonObject()
	{
		JSONObject outputData = new JSONObject();
		outputData.put("name", name);
		outputData.put("description", description);
		outputData.put("toothType", toothType);
		outputData.put("byteOff", byteOffsetFromGear);
		outputData.put("bitOff", bitOffsetFromGear);
		outputData.put("value", value);
		outputData.put("writeable", writeable);
		return outputData;
	}

}
