package se.esss.litterbox.its.bytegearbox;

import se.esss.litterbox.icecube.bytegearbox.ByteGear;
import se.esss.litterbox.icecube.bytegearbox.ByteGearBox;
import se.esss.litterbox.icecube.bytegearbox.ByteTooth;

public class KlyPlcProtoCpuGearBox {

	public static void main(String[] args) throws Exception 
	{
		ByteTooth[] byteToothRead = new ByteTooth[17];
		byteToothRead[0]		= new ByteTooth("RESET",				"BOOLEAN",		0, 		0, 		false, 		"0", 	"CPU reset command");
		byteToothRead[1]		= new ByteTooth("LATCH_MODE",			"BOOLEAN",		0, 		1, 		false, 		"0", 	"CPU latching mode");
		byteToothRead[2]		= new ByteTooth("INTERLOCK",			"BOOLEAN",		0, 		2, 		false, 		"0", 	"CPU Interlock/Alarm");
		byteToothRead[3]		= new ByteTooth("ACK_REQ",				"BOOLEAN",		0, 		3, 		false, 		"0", 	"CPU Warning - Acknowledge is required");
		byteToothRead[4]		= new ByteTooth("AUX_MISS_PRECOND",		"BOOLEAN",		0, 		4, 		false, 		"0", 	"AUXILIARY missed pre-condition");
		byteToothRead[5]		= new ByteTooth("FIL_MISS_PRECOND",		"BOOLEAN",		0, 		5, 		false, 		"0", 	"FILAMENT missed pre-condition");
		byteToothRead[6]		= new ByteTooth("STBY_MISS_PRECOND",	"BOOLEAN",		0, 		6, 		false, 		"0", 	"STAND BY missed pre-condition");
		byteToothRead[7]		= new ByteTooth("HV_ENA_MISS_PRECOND",	"BOOLEAN",		0, 		7, 		false, 		"0", 	"HV Enable missed pre-condition");
		byteToothRead[8]		= new ByteTooth("RF_ENA_MISS_PRECOND",	"BOOLEAN",		1, 		0, 		false, 		"0", 	"RF Enable missed pre-condition");
		byteToothRead[9]		= new ByteTooth("AUX_MISS_SUPCOND",		"BOOLEAN",		1, 		1, 		false, 		"0", 	"AUXILIARY missed sup-condition");
		byteToothRead[10]		= new ByteTooth("FIL_MISS_SUPCOND",		"BOOLEAN",		1, 		2, 		false, 		"0", 	"FILAMENT missed sup-condition");
		byteToothRead[11]		= new ByteTooth("STBY_MISS_SUPCOND",	"BOOLEAN",		1, 		3, 		false, 		"0", 	"STAND BY missed sup-condition");
		byteToothRead[12]		= new ByteTooth("HV_ENA_MISS_SUPCOND",	"BOOLEAN",		1, 		4, 		false, 		"0", 	"HV Enable missed sup-condition");
		byteToothRead[13]		= new ByteTooth("RF_ENA_MISS_SUPCOND",	"BOOLEAN",		1, 		5, 		false, 		"0", 	"RF Enable missed sup-condition");
		byteToothRead[14]		= new ByteTooth("ACT_STATE",			"BYTE",			2, 		0, 		false, 		"0", 	"Actual State - see below the code numbers");
		byteToothRead[15]		= new ByteTooth("SET_STATE",			"SHORT",		4, 		0, 		false, 		"0", 	"Set new command to the target 0=OFF; 1=AUX; 2=FIL; 3=STDBY; 4=HVON; 5=RFON");
		byteToothRead[16]		= new ByteTooth("OP_MODE",				"BYTE",			6, 		0, 		false, 		"0", 	"1 = Bypass mode, 2 = Maintenance/testing mode, 3 = Normal operation, 4 = Wire test - NOT IMPLEMENTED YET");

		ByteTooth[] byteToothWrite = new ByteTooth[9];
		byteToothWrite[0]	= new ByteTooth("RESET",		"BOOLEAN",		0, 		0, 		true, 		"0", 	"CPU reset command");
		byteToothWrite[1]	= new ByteTooth("OFF_CMD",		"BOOLEAN",		0, 		1, 		true, 		"0", 	"OFF command");
		byteToothWrite[2]	= new ByteTooth("AUX_CMD",		"BOOLEAN",		0, 		2, 		true, 		"0", 	"Aux command");
		byteToothWrite[3]	= new ByteTooth("FIL_CMD",		"BOOLEAN",		0, 		3, 		true, 		"0", 	"FILAMENT command");
		byteToothWrite[4]	= new ByteTooth("STBY_CMD",		"BOOLEAN",		0, 		4, 		true, 		"0", 	"Standby command");
		byteToothWrite[5]	= new ByteTooth("HV_CMD",		"BOOLEAN",		0, 		5, 		true, 		"0", 	"High-voltage command");
		byteToothWrite[6]	= new ByteTooth("RF_CMD",		"BOOLEAN",		0, 		6, 		true, 		"0", 	"RF Enable command");
		byteToothWrite[7]	= new ByteTooth("TEST_ALL_AD",	"BOOLEAN",		0, 		7, 		true, 		"0", 	"Test all arc detector");
		byteToothWrite[8]	= new ByteTooth("OP_MODE",		"BYTE",			1, 		0, 		true, 		"0", 	"1 = Bypass mode, 2 = Maintenance/testing mode, 3 = Normal operation, 4 = Wire test");

		ByteGear[] byteGearE = new 	ByteGear[1];
		byteGearE[0] = new ByteGear("CPU_CONF", 		0, 			0);

		for (int ii = 0; ii < byteGearE.length; ++ii)
		{
			for (int ir = 0; ir < byteToothRead.length; ++ir)
			{
				byteGearE[ii].getReadToothList().add(new ByteTooth(byteToothRead[ir].getJsonObject()));
			}
			for (int iw = 0; iw < byteToothWrite.length; ++iw)
			{
				byteGearE[ii].getWriteToothList().add(new ByteTooth(byteToothWrite[iw].getJsonObject()));
			}
		}
		
		ByteGearBox byteGearBox = new ByteGearBox("tcp://broker.shiftr.io", 1883, "klyPlcProtoCpu", 8, 2);
		for (int ii = 0; ii < byteGearE.length; ++ii)
		{
			byteGearBox.getByteGearList().add(byteGearE[ii]);
		}
		
		byteGearBox.writeToFile("klyPlcProtoCpu.json",false);
	
	}

}
