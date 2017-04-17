package se.esss.litterbox.its.bytegearbox;

import se.esss.litterbox.icecube.bytegearbox.ByteGear;
import se.esss.litterbox.icecube.bytegearbox.ByteGearBox;
import se.esss.litterbox.icecube.bytegearbox.ByteTooth;

public class KlyPlcProtoDioGearBox 
{

	public static void main(String[] args) throws Exception 
	{
		ByteTooth[] byteToothRead = new ByteTooth[12];
		byteToothRead[0]		= new ByteTooth("VAL",		"BOOLEAN",		0, 		0, 		false, 		"0", 	"Field value");
		byteToothRead[1]		= new ByteTooth("INTERLOCK","BOOLEAN",		0, 		1, 		false, 		"0", 	"Alarm/Interlock");
		byteToothRead[2]		= new ByteTooth("SVAL",		"BOOLEAN",		0, 		2, 		false, 		"0", 	"Simulation value");
		byteToothRead[3]		= new ByteTooth("DISABLE",	"BOOLEAN",		0, 		3, 		false, 		"0", 	"Interlocks disabled");
		byteToothRead[4]		= new ByteTooth("FORCE",	"BOOLEAN",		0, 		4, 		false, 		"0", 	"Force the variable to the simulation value");
		byteToothRead[5]		= new ByteTooth("N_FORCE",	"BOOLEAN",		0, 		5, 		false, 		"0", 	"Cannot force");
		byteToothRead[6]		= new ByteTooth("N_LATCH",	"BOOLEAN",		0, 		6, 		false, 		"0", 	"Never Latch");
		byteToothRead[7]		= new ByteTooth("IsFIRST",	"BOOLEAN",		0, 		7, 		false, 		"0", 	"First variable interlocked before the CPU alarm trips");
		byteToothRead[8]		= new ByteTooth("INVERT_DO","BOOLEAN",		1, 		0, 		false, 		"0", 	"Invert DO field value");
		byteToothRead[9]		= new ByteTooth("TMPST",	"LONG",			3, 		0, 		false, 		"0", 	"Time-stamp");
		byteToothRead[10]		= new ByteTooth("DTYP",		"BYTE",			11, 	0, 		false, 		"0", 	"Device type");
		byteToothRead[11]		= new ByteTooth("ERR",		"BYTE",			12, 	0, 		false, 		"0", 	"Error code");

		ByteTooth[] byteToothWrite = new ByteTooth[3];
		byteToothWrite[0]	= new ByteTooth("DISABLE",	"BOOLEAN",		0, 		0, 		true, 		"0", 	"Interlocks disabled");
		byteToothWrite[1]	= new ByteTooth("FORCE",	"BOOLEAN",		0, 		1, 		true, 		"0", 	"Force the variable to the simulation value");
		byteToothWrite[2]	= new ByteTooth("SVAL",		"BOOLEAN",		0, 		2, 		true, 		"0", 	"Simulation value");

		ByteGear[] byteGearE = new 	ByteGear[14];
		byteGearE[0] = new ByteGear("KLY-RFS-HZ:EmStop", 		0, 			0);
		byteGearE[1] = new ByteGear("KLY-RFS-Q01:BreakerLine",	12, 		2);
		byteGearE[2] = new ByteGear("KLY-RFS-Q02:BreakerLine",	24, 		4);
		byteGearE[3] = new ByteGear("KLY-Oil-GS:LevelDetect", 	36, 		6);
		byteGearE[4] = new ByteGear("KLY-Oil-TS:TempSwitch", 	48, 		8);
		byteGearE[5] = new ByteGear("KLY-Oil-LS:LidSwitch", 	60, 		10);
		byteGearE[6] = new ByteGear("KLY-Oil-LS:OilLeakage", 	72, 		12);
		byteGearE[7] = new ByteGear("RFFIM-RF-En:HVenaStatus",	84, 		14);
		byteGearE[8] = new ByteGear("RFFIM-RF-En:RFenaStatus", 	96, 		16);
		byteGearE[9] = new ByteGear("RFFIM-HV-En:HVenaCmd", 	108, 		18);
		byteGearE[10] = new ByteGear("RFFIM-RF-En:RFenaCmd", 	120, 		20);
		byteGearE[11] = new ByteGear("RFS-Circ-DI:TCUready", 	132, 		22);
		byteGearE[12] = new ByteGear("RFS_KLY_AD1A", 			144, 		24);
		byteGearE[13] = new ByteGear("RFS_KLY_AD1B", 			156, 		26);

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
		
		ByteGearBox byteGearBox = new ByteGearBox("tcp://broker.shiftr.io", 1883, "klyPlcProtoDio", 144, 24);
		for (int ii = 0; ii < byteGearE.length; ++ii)
		{
			byteGearBox.getByteGearList().add(byteGearE[ii]);
		}
		
		byteGearBox.writeToFile("klyPlcProtoDio.json",false);
	
	}
}
