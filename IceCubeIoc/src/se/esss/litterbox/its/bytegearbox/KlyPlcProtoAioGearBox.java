package se.esss.litterbox.its.bytegearbox;

import se.esss.litterbox.icecube.bytegearbox.ByteGear;
import se.esss.litterbox.icecube.bytegearbox.ByteGearBox;
import se.esss.litterbox.icecube.bytegearbox.ByteTooth;

public class KlyPlcProtoAioGearBox 
{

	public static void main(String[] args) throws Exception 
	{
		ByteTooth[] byteToothRead = new ByteTooth[20];
		byteToothRead[0] = new ByteTooth("WARNING",		"BOOLEAN",		0, 		0, 		false, 		"false", 	"Warning flag");
		byteToothRead[1] = new ByteTooth("INTERLOCK",	"BOOLEAN",		0, 		1, 		false, 		"false", 	"Alarm/Interlock");
		byteToothRead[2] = new ByteTooth("DISABLE",		"BOOLEAN",		0, 		2, 		false, 		"false", 	"Interlocks disabled");
		byteToothRead[3] = new ByteTooth("FORCE",		"BOOLEAN",		0, 		3, 		false, 		"false", 	"Force the variable to the simulation value");
		byteToothRead[4] = new ByteTooth("N_FORCE",		"BOOLEAN",		0, 		4, 		false, 		"false", 	"Cannot force");
		byteToothRead[5] = new ByteTooth("N_LATCH",		"BOOLEAN",		0, 		5, 		false, 		"false", 	"Never Latch");
		byteToothRead[6] = new ByteTooth("IsFIRST",		"BOOLEAN",		0, 		6, 		false, 		"false", 	"First variable interlocked before the CPU alarm trips");
		byteToothRead[7] = new ByteTooth("LOW_ALARM",	"BOOLEAN",		0, 		7, 		false, 		"false", 	"Low alarm detection");
		byteToothRead[8] = new ByteTooth("HIGH_ALARM",	"BOOLEAN",		1, 		0, 		false, 		"false", 	"High alarm detection");
		byteToothRead[9] = new ByteTooth("TMPST",		"S7DT",			2, 		0, 		false, 		"0", 	"Time-stamp");
		byteToothRead[10] = new ByteTooth("DTYP",		"BYTE",			10, 	0, 		false, 		"0", 	"Device type");
		byteToothRead[11] = new ByteTooth("ERR",		"SHORT",		12, 	0, 		false, 		"0", 	"Error code");
		byteToothRead[12] = new ByteTooth("HIHI",		"FLOAT",		14, 	0, 		false, 		"0", 	"Hihi Alarm limit");
		byteToothRead[13] = new ByteTooth("HIGH",		"FLOAT",		18, 	0, 		false, 		"0", 	"High Alarm limit");
		byteToothRead[14] = new ByteTooth("LOW",		"FLOAT",		22, 	0, 		false, 		"0", 	"Low Alarm limit");
		byteToothRead[15] = new ByteTooth("LOLO",		"FLOAT",		26, 	0, 		false, 		"0", 	"Lolo Alarm limit");
		byteToothRead[16] = new ByteTooth("AOFF",		"FLOAT",		30, 	0, 		false, 		"0", 	"Adjustment Offset");
		byteToothRead[17] = new ByteTooth("SVAL",		"FLOAT",		34, 	0, 		false, 		"0", 	"Simulation Value");
		byteToothRead[18] = new ByteTooth("EGU",		"FLOAT",		38, 	0, 		false, 		"0", 	"Engineering Field Unit");
		byteToothRead[19] = new ByteTooth("VAL",		"SHORT",		42, 	0, 		false, 		"0", 	"Field Value");
		
		ByteTooth[] byteToothWrite = new ByteTooth[9];
		byteToothWrite[0]	= new ByteTooth("DISABLE",	"BOOLEAN",		0, 		0, 		true, 		"false", 	"Variable disable");
		byteToothWrite[1]	= new ByteTooth("FORCE",	"BOOLEAN",		0, 		1, 		true, 		"false", 	"Force the variable to the simulation value");
		byteToothWrite[2]	= new ByteTooth("WR_DATA",	"BOOLEAN",		0, 		2, 		true, 		"false", 	"Write Data");
		byteToothWrite[3]	= new ByteTooth("HIHI",		"FLOAT",		2, 		0, 		true, 		"0", 	"Hihi Alarm limit");
		byteToothWrite[4]	= new ByteTooth("HIGH",		"FLOAT",		6, 		0, 		true, 		"0", 	"High Alarm limit");
		byteToothWrite[5]	= new ByteTooth("LOW",		"FLOAT",		10, 	0, 		true, 		"0", 	"Low Alarm limit");
		byteToothWrite[6]	= new ByteTooth("LOLO",		"FLOAT",		14, 	0, 		true, 		"0", 	"Lolo Alarm limit");
		byteToothWrite[7]	= new ByteTooth("AOFF",		"FLOAT",		18, 	0, 		true, 		"0", 	"Adjustment Offset");
		byteToothWrite[8]	= new ByteTooth("SVAL",		"FLOAT",		22, 	0, 		true, 		"0", 	"Simulation Value");
		
		ByteGear[] byteGearE = new 	ByteGear[28];
		byteGearE[0] = new ByteGear("KLY_IP_ISn_Current", 				0, 		0);
		byteGearE[1] = new ByteGear("KLY_Oil_TSn_SurfTemp", 			44, 	26);
		byteGearE[2] = new ByteGear("KLY_Oil_FSn_WatOutletFlow", 		88, 	52);
		byteGearE[3] = new ByteGear("KLY_Oil_TSn_WatInletTemp", 		132, 	78);
		byteGearE[4] = new ByteGear("KLY_Sol_TSn_WatInletTemp", 		176, 	104);
		byteGearE[5] = new ByteGear("KLY_Sol_TSn_WatInletTempMan", 		220, 	130);
		byteGearE[6] = new ByteGear("KLY_Sol_TSn_WatInletTemp", 		264, 	156);
		byteGearE[7] = new ByteGear("KLY_Sol_FSn_WatOutletFlow", 		308, 	182);
		byteGearE[8] = new ByteGear("KLY_Sol_TSn_SurfTemp", 			352, 	208);
		byteGearE[9] = new ByteGear("KLY_Win_FSn_WatOutletFlow", 		396, 	234);
		byteGearE[10] = new ByteGear("KLY_Win_TSn_WatOutletTemp", 		440, 	260);
		byteGearE[11] = new ByteGear("KLY_Win_TSn_WatOutletTempMan", 	484, 	286);
		byteGearE[12] = new ByteGear("KLY_Win_TSn_WatOutletSleeveTemp",	528, 	312);
		byteGearE[13] = new ByteGear("KLY_Coll_TSn_TopTemp", 			572, 	338);
		byteGearE[14] = new ByteGear("KLY_Coll_TSn_EdgeTemp", 			616, 	364);
		byteGearE[15] = new ByteGear("KLY_Coll_TSn_WatInletTemp", 		660, 	390);
		byteGearE[16] = new ByteGear("KLY_Coll_TSn_WatOutletTemp", 		704, 	416);
		byteGearE[17] = new ByteGear("KLY_Coll_FSn_WatOutletFlow", 		748, 	442);
		byteGearE[18] = new ByteGear("KLY_Body_TSn_WatInletTemp", 		792, 	468);
		byteGearE[19] = new ByteGear("KLY_Body_TSn_WatOutletTemp", 		836, 	494);
		byteGearE[20] = new ByteGear("KLY_Body_FSn_WatOutletFlow", 		880, 	520);
		byteGearE[21] = new ByteGear("KLY_Coll_CollDissipation", 		924, 	546);
		byteGearE[22] = new ByteGear("KLY_Body_BodyDisssipation", 		968, 	572);
		byteGearE[23] = new ByteGear("RFDS_Circ_FSn_WatOutletFlow", 	1012, 	598);
		byteGearE[24] = new ByteGear("RFDS_Circ_TSn_WatInletTemp", 		1056, 	624);
		byteGearE[25] = new ByteGear("RFDS_Load_TSn_WatInletTemp", 		1100, 	650);
		byteGearE[26] = new ByteGear("RFDS_Load_FSn_WatOutletFlow", 	1144, 	676);
		byteGearE[27] = new ByteGear("RFDS_Load_TSn_WatOutletTemp", 	1188, 	702);
		
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
		
		ByteGearBox byteGearBox = new ByteGearBox("tcp://broker.shiftr.io", 1883, "klyPlcProtoAio", 1232, 728);
		for (int ii = 0; ii < byteGearE.length; ++ii)
		{
			byteGearBox.getByteGearList().add(byteGearE[ii]);
		}
		
		byteGearBox.writeToFile("klyPlcProtoAio.json",false);
	}

}
