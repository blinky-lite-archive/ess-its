package se.esss.litterbox.its.mobileskeletongwt.shared.bytegearboxgwt;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ByteToothGwt implements Serializable 
{
	
	private static boolean littleEndian =  true;

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
	
	public ByteToothGwt() {}
	public ByteToothGwt(String name, String toothType, int byteOffsetFromGear, int bitOffsetFromGear, boolean writeable, String value, String description)
	{
		this.name = name;
		this.description = description;
		this.toothType = toothType.toUpperCase();
		this.byteOffsetFromGear = byteOffsetFromGear;
		this.bitOffsetFromGear = (byte) bitOffsetFromGear;
		this.writeable = writeable;
		this.value = value;
	}
	public void setData(byte[] byteArray, int byteGearOffset)
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
			value = bytesToFloat(byteChunk, littleEndian);
		}
		if (toothType.equals("BYTE"))
		{
			value = Byte.toString(byteArray[offset]);
		}
		if (toothType.equals("LONG"))
		{
			byte[] byteChunk = new byte[8];
			for (int ii = 0; ii < 8; ++ii) byteChunk[ii] = byteArray[offset + ii];
			value = bytesToLong(byteChunk, littleEndian);
		}
		if (toothType.equals("SHORT"))
		{
			byte[] byteChunk = new byte[2];
			for (int ii = 0; ii < 2; ++ii) byteChunk[ii] = byteArray[offset + ii];
			value = bytesToShort(byteChunk, littleEndian);
		}
		if (toothType.equals("INT"))
		{
			byte[] byteChunk = new byte[2];
			for (int ii = 0; ii < 4; ++ii) byteChunk[ii] = byteArray[offset + ii];
			value = bytesToInt(byteChunk, littleEndian);
		}
		if (toothType.equals("DOUBLE"))
		{
			byte[] byteChunk = new byte[8];
			for (int ii = 0; ii < 8; ++ii) byteChunk[ii] = byteArray[offset + ii];
			value = bytesToDouble(byteChunk, littleEndian);
		}
	}
	public void getData(byte[] byteArray, int byteGearWriteOffset) 
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
			byte[] byteChunk = floatToBytes(value, littleEndian);
			for (int ii = 0; ii < 4; ++ii) byteArray[offset + ii] = byteChunk[ii];
		}
		if (toothType.equals("BYTE"))
		{
			byteArray[offset] = Byte.parseByte(value);
		}
		if (toothType.equals("LONG"))
		{
			byte[] byteChunk = longToBytes(value, littleEndian);
			for (int ii = 0; ii < 8; ++ii) byteArray[offset + ii] = byteChunk[ii];
		}
		if (toothType.equals("SHORT"))
		{
			byte[] byteChunk = shortToBytes(value, littleEndian);;
			for (int ii = 0; ii < 2; ++ii) byteArray[offset + ii] = byteChunk[ii];
		}
		if (toothType.equals("INT"))
		{
			byte[] byteChunk = intToBytes(value, littleEndian);;
			for (int ii = 0; ii < 4; ++ii) byteArray[offset + ii] = byteChunk[ii];
		}
		if (toothType.equals("DOUBLE"))
		{
			byte[] byteChunk = doubleToBytes(value, littleEndian);;
			for (int ii = 0; ii < 8; ++ii) byteArray[offset + ii] = byteChunk[ii];
		}
	}
	private static byte[] floatToBytes(String val, boolean littleEndian)
	{
		int bits = Float.floatToIntBits(Float.parseFloat(val));
		byte[] bytes = new byte[4];
		if (littleEndian)
		{
			bytes[0] = (byte)(bits & 0xff);
			bytes[1] = (byte)((bits >> 8) & 0xff);
			bytes[2] = (byte)((bits >> 16) & 0xff);
			bytes[3] = (byte)((bits >> 24) & 0xff);
		}
		else
		{
			bytes[3] = (byte)(bits & 0xff);
			bytes[2] = (byte)((bits >> 8) & 0xff);
			bytes[1] = (byte)((bits >> 16) & 0xff);
			bytes[0] = (byte)((bits >> 24) & 0xff);
		}
		return bytes;
	}
	private static String bytesToFloat(byte[] bytes, boolean littleEndian)
	{
		int asInt;
		if (littleEndian)
		{
			asInt = (bytes[0] & 0xFF) 
		            | ((bytes[1] & 0xFF) << 8) 
		            | ((bytes[2] & 0xFF) << 16) 
		            | ((bytes[3] & 0xFF) << 24);
		}
		else
		{
			asInt = (bytes[3] & 0xFF) 
		            | ((bytes[2] & 0xFF) << 8) 
		            | ((bytes[1] & 0xFF) << 16) 
		            | ((bytes[0] & 0xFF) << 24);
		}
		return Float.toString(Float.intBitsToFloat(asInt));
	}
	private static byte[] intToBytes(String val, boolean littleEndian)
	{
		int bits = Integer.parseInt(val);
		byte[] bytes = new byte[4];
		if (littleEndian)
		{
			bytes[0] = (byte)(bits & 0xff);
			bytes[1] = (byte)((bits >> 8) & 0xff);
			bytes[2] = (byte)((bits >> 16) & 0xff);
			bytes[3] = (byte)((bits >> 24) & 0xff);
		}
		else
		{
			bytes[3] = (byte)(bits & 0xff);
			bytes[2] = (byte)((bits >> 8) & 0xff);
			bytes[1] = (byte)((bits >> 16) & 0xff);
			bytes[0] = (byte)((bits >> 24) & 0xff);
		}
		return bytes;
	}
	private static String bytesToInt(byte[] bytes, boolean littleEndian)
	{
		int asInt;
		if (littleEndian)
		{
			asInt = (bytes[0] & 0xFF) 
		            | ((bytes[1] & 0xFF) << 8) 
		            | ((bytes[2] & 0xFF) << 16) 
		            | ((bytes[3] & 0xFF) << 24);
		}
		else
		{
			asInt = (bytes[3] & 0xFF) 
		            | ((bytes[2] & 0xFF) << 8) 
		            | ((bytes[1] & 0xFF) << 16) 
		            | ((bytes[0] & 0xFF) << 24);
		}
		return Integer.toString(asInt);
	}
	private static byte[] shortToBytes(String val, boolean littleEndian)
	{
		short bits = Short.parseShort(val);
		byte[] bytes = new byte[2];
		if (littleEndian)
		{
			bytes[0] = (byte)(bits & 0xff);
			bytes[1] = (byte)((bits >> 8) & 0xff);
		}
		else
		{
			bytes[1] = (byte)(bits & 0xff);
			bytes[0] = (byte)((bits >> 8) & 0xff);
		}
		return bytes;
	}
	private static String bytesToShort(byte[] bytes, boolean littleEndian)
	{
		int asInt = 0;
		if (littleEndian)
		{
			asInt = (bytes[0] & 0xFF) 
		            | ((bytes[1] & 0xFF) << 8);
		}
		else
		{
			asInt = (bytes[1] & 0xFF) 
		            | ((bytes[0] & 0xFF) << 8); 
		}
		return Short.toString((short) asInt);
	}
	private static byte[] longToBytes(String val, boolean littleEndian)
	{
		long bits = Long.parseLong(val);
		byte[] bytes = new byte[8];
		if (littleEndian)
		{
			bytes[0] = (byte)(bits & 0xff);
			bytes[1] = (byte)((bits >> 8) & 0xff);
			bytes[2] = (byte)((bits >> 16) & 0xff);
			bytes[3] = (byte)((bits >> 24) & 0xff);
			bytes[4] = (byte)((bits >> 32) & 0xff);
			bytes[5] = (byte)((bits >> 40) & 0xff);
			bytes[6] = (byte)((bits >> 48) & 0xff);
			bytes[7] = (byte)((bits >> 56) & 0xff);
		}
		else
		{
			bytes[7] = (byte)(bits & 0xff);
			bytes[6] = (byte)((bits >> 8) & 0xff);
			bytes[5] = (byte)((bits >> 16) & 0xff);
			bytes[4] = (byte)((bits >> 24) & 0xff);
			bytes[3] = (byte)((bits >> 32) & 0xff);
			bytes[2] = (byte)((bits >> 40) & 0xff);
			bytes[1] = (byte)((bits >> 48) & 0xff);
			bytes[0] = (byte)((bits >> 56) & 0xff);
		}
		return bytes;
	}
	private static String bytesToLong(byte[] bytes, boolean littleEndian)
	{
		long asLong = 0;
		if (littleEndian)
		{
			asLong = (bytes[0] & 0xFFL) 
		            | ((bytes[1] & 0xFFL) << 8) 
		            | ((bytes[2] & 0xFFL) << 16) 
		            | ((bytes[3] & 0xFFL) << 24)
		            | ((bytes[4] & 0xFFL) << 32)
		            | ((bytes[5] & 0xFFL) << 40)
		            | ((bytes[6] & 0xFFL) << 48)
		            | ((bytes[7] & 0xFFL) << 56);
		}
		else
		{
			asLong = (bytes[7] & 0xFFL) 
		            | ((bytes[6] & 0xFFL) << 8) 
		            | ((bytes[5] & 0xFFL) << 16) 
		            | ((bytes[4] & 0xFFL) << 24)
		            | ((bytes[3] & 0xFFL) << 32)
		            | ((bytes[2] & 0xFFL) << 40)
		            | ((bytes[1] & 0xFFL) << 48)
		            | ((bytes[0] & 0xFFL) << 56);
		}
		return Long.toString(asLong);
	}
	private static byte[] doubleToBytes(String val, boolean littleEndian)
	{
		long bits = Double.doubleToLongBits(Double.parseDouble(val));
		byte[] bytes = new byte[8];
		if (littleEndian)
		{
			bytes[0] = (byte)(bits & 0xff);
			bytes[1] = (byte)((bits >> 8) & 0xff);
			bytes[2] = (byte)((bits >> 16) & 0xff);
			bytes[3] = (byte)((bits >> 24) & 0xff);
			bytes[4] = (byte)((bits >> 32) & 0xff);
			bytes[5] = (byte)((bits >> 40) & 0xff);
			bytes[6] = (byte)((bits >> 48) & 0xff);
			bytes[7] = (byte)((bits >> 56) & 0xff);
		}
		else
		{
			bytes[7] = (byte)(bits & 0xff);
			bytes[6] = (byte)((bits >> 8) & 0xff);
			bytes[5] = (byte)((bits >> 16) & 0xff);
			bytes[4] = (byte)((bits >> 24) & 0xff);
			bytes[3] = (byte)((bits >> 32) & 0xff);
			bytes[2] = (byte)((bits >> 40) & 0xff);
			bytes[1] = (byte)((bits >> 48) & 0xff);
			bytes[0] = (byte)((bits >> 56) & 0xff);
		}
		return bytes;
	}
	private static String bytesToDouble(byte[] bytes, boolean littleEndian)
	{
		long asLong = 0;
		if (littleEndian)
		{
			asLong = (bytes[0] & 0xFFL) 
		            | ((bytes[1] & 0xFFL) << 8) 
		            | ((bytes[2] & 0xFFL) << 16) 
		            | ((bytes[3] & 0xFFL) << 24)
		            | ((bytes[4] & 0xFFL) << 32)
		            | ((bytes[5] & 0xFFL) << 40)
		            | ((bytes[6] & 0xFFL) << 48)
		            | ((bytes[7] & 0xFFL) << 56);
		}
		else
		{
			asLong = (bytes[7] & 0xFFL) 
		            | ((bytes[6] & 0xFFL) << 8) 
		            | ((bytes[5] & 0xFFL) << 16) 
		            | ((bytes[4] & 0xFFL) << 24)
		            | ((bytes[3] & 0xFFL) << 32)
		            | ((bytes[2] & 0xFFL) << 40)
		            | ((bytes[1] & 0xFFL) << 48)
		            | ((bytes[0] & 0xFFL) << 56);
		}
		return Double.toString(Double.longBitsToDouble(asLong));
	}

}
