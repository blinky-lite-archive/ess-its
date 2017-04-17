package se.esss.litterbox.its.bytegearbox;

import se.esss.litterbox.icecube.bytegearbox.ByteGear;
import se.esss.litterbox.icecube.bytegearbox.ByteGearBox;
import se.esss.litterbox.icecube.bytegearbox.ByteTooth;

public class KlyPlcProtoPsuGearBox 
{

	public static void main(String[] args) throws Exception 
	{
		ByteTooth[] filByteToothRead = new ByteTooth[59];
		filByteToothRead[0]		= new ByteTooth("ON_STATUS",		"BOOLEAN",		0, 		0, 		false, 		"0", 	"PSU is Operating normally");
		filByteToothRead[1]		= new ByteTooth("STARTED",			"BOOLEAN",		0, 		1, 		false, 		"0", 	"PSU has received the ON Command");
		filByteToothRead[2]		= new ByteTooth("STOPPED",			"BOOLEAN",		0, 		2, 		false, 		"0", 	"PSU is NOT operating");
		filByteToothRead[3]		= new ByteTooth("WARMUP",			"BOOLEAN",		0, 		3, 		false, 		"0", 	"PSU start procedure is running");
		filByteToothRead[4]		= new ByteTooth("TEMP_REACHED",		"BOOLEAN",		0, 		4, 		false, 		"0", 	"PSU is at operating temperature - Warmup Done");
		filByteToothRead[5]		= new ByteTooth("I_REG",			"BOOLEAN",		0, 		5, 		false, 		"0", 	"HIGH= the PSU is in constant cuttent mode");
		filByteToothRead[6]		= new ByteTooth("LOC_REM_ST",		"BOOLEAN",		0, 		6, 		false, 		"0", 	"HIGH=REMOTE,FALSE=LOCAL");
		filByteToothRead[7]		= new ByteTooth("N_LATCH",			"BOOLEAN",		0, 		7, 		false, 		"0", 	"Disable Latching from PLC code");
		filByteToothRead[8]		= new ByteTooth("N_FORCE",			"BOOLEAN",		1, 		0, 		false, 		"0", 	"Disable forcing from PLC code");
		filByteToothRead[9]		= new ByteTooth("IsFIRST",			"BOOLEAN",		1, 		1, 		false, 		"0", 	"Is the PSU Interlock the first Interlock?");
		filByteToothRead[10]	= new ByteTooth("WARNING",			"BOOLEAN",		1, 		2, 		false, 		"0", 	"PSU Warning Bit");
		filByteToothRead[11]	= new ByteTooth("INTERLOCK",		"BOOLEAN",		1, 		3, 		false, 		"0", 	"PSU Interlock Bit");
		filByteToothRead[12]	= new ByteTooth("FORCE_ON",			"BOOLEAN",		1, 		4, 		false, 		"0", 	"Feedback ForceON");
		filByteToothRead[13]	= new ByteTooth("S_ON_STATUS",		"BOOLEAN",		1, 		5, 		false, 		"0", 	"Feedback ON_STATUS input");
		filByteToothRead[14]	= new ByteTooth("S_I_REG",			"BOOLEAN",		1, 		6, 		false, 		"0", 	"Feedback I_REG input");
		filByteToothRead[15]	= new ByteTooth("S_FAULT",			"BOOLEAN",		1, 		7, 		false, 		"0", 	"Feedback FAULT input");
		filByteToothRead[16]	= new ByteTooth("S_LOC_REM",		"BOOLEAN",		2, 		0, 		false, 		"0", 	"Feedback LOC_REM input");
		filByteToothRead[17]	= new ByteTooth("I_RAMP_ON",		"BOOLEAN",		2, 		1, 		false, 		"0", 	"Feedback for Current Ramp ON");
		filByteToothRead[18]	= new ByteTooth("V_RAMP_ON",		"BOOLEAN",		2, 		2, 		false, 		"0", 	"Feedback for Voltage Ramp ON");
		filByteToothRead[19]	= new ByteTooth("DTYP",				"BYTE",			3, 		0, 		false, 		"0", 	"PSU Type");
		filByteToothRead[20]	= new ByteTooth("ERR",				"SHORT",		4, 		0, 		false, 		"0", 	"Different error bits stored in the word descibed in the code");
		filByteToothRead[21]	= new ByteTooth("TOTAL_WARMUP_LEFT","FLOAT",		6, 		0, 		false, 		"0", 	"How much time is left before operation");
		filByteToothRead[22]	= new ByteTooth("WARMUP_LEFT_SP",	"FLOAT",		10, 	0, 		false, 		"0", 	"How much time is left before actual SetPoint reached");
		filByteToothRead[23]	= new ByteTooth("UPTIME_NO_HV",		"FLOAT",		14, 	0, 		false, 		"0", 	"PSU uptime without HighVoltage");
		filByteToothRead[24]	= new ByteTooth("TIME_COLD_SP1",	"FLOAT",		18, 	0, 		false, 		"0", 	"Feedback Setpoint1 Time");
		filByteToothRead[25]	= new ByteTooth("TIME_COLD_SP2",	"FLOAT",		22, 	0, 		false, 		"0", 	"Feedback Setpoint2 Time");
		filByteToothRead[26]	= new ByteTooth("TIME_COLD_SP3",	"FLOAT",		26, 	0, 		false, 		"0", 	"Feedback Setpoint3 Time");
		filByteToothRead[27]	= new ByteTooth("TIME_WARM_SP1",	"FLOAT",		30, 	0, 		false, 		"0", 	"Feedback Setpoint1 Time warm");
		filByteToothRead[28]	= new ByteTooth("TIME_WARM_SP2",	"FLOAT",		34, 	0, 		false, 		"0", 	"Feedback Setpoint2 Time warm");
		filByteToothRead[29]	= new ByteTooth("TIME_WARM_SP3",	"FLOAT",		38, 	0, 		false, 		"0", 	"Feedback Setpoint3 Time warm");
		filByteToothRead[30]	= new ByteTooth("ACT_SP",			"FLOAT",		42, 	0, 		false, 		"0", 	"Actual Setpoint number");
		filByteToothRead[31]	= new ByteTooth("I_RAMP_RATE",		"FLOAT",		46, 	0, 		false, 		"0", 	"Feedback Calculated Current Ramp Rate");
		filByteToothRead[32]	= new ByteTooth("V_RAMP_RATE",		"FLOAT",		50, 	0, 		false, 		"0", 	"Feedback Calculated Voltage Ramp Rate");
		filByteToothRead[33]	= new ByteTooth("VMON",				"FLOAT",		54, 	0, 		false, 		"0", 	"Actual Voltage (V)");
		filByteToothRead[34]	= new ByteTooth("IMON",				"FLOAT",		58, 	0, 		false, 		"0", 	"Actual Current (I)");
		filByteToothRead[35]	= new ByteTooth("WMON",				"FLOAT",		62, 	0, 		false, 		"0", 	"Actual Power (W)");
		filByteToothRead[36]	= new ByteTooth("S_VMON",			"FLOAT",		66, 	0, 		false, 		"0", 	"Feedback VMON input");
		filByteToothRead[37]	= new ByteTooth("S_IMON",			"FLOAT",		70, 	0, 		false, 		"0", 	"Feedback IMON input");
		filByteToothRead[38]	= new ByteTooth("S_WMON",			"FLOAT",		74, 	0, 		false, 		"0", 	"Feedback WMON input");
		filByteToothRead[39]	= new ByteTooth("I_SP1",			"FLOAT",		78, 	0, 		false, 		"0", 	"Feedback Setpoint1 Current");
		filByteToothRead[40]	= new ByteTooth("I_SP2",			"FLOAT",		82, 	0, 		false, 		"0", 	"Feedback Setpoint2 Current");
		filByteToothRead[41]	= new ByteTooth("I_SP3",			"FLOAT",		86, 	0, 		false, 		"0", 	"Feedback Setpoint3 Current");
		filByteToothRead[42]	= new ByteTooth("V_SP1",			"FLOAT",		90, 	0, 		false, 		"0", 	"Feedback Setpoint1 Voltage");
		filByteToothRead[43]	= new ByteTooth("V_SP2",			"FLOAT",		94, 	0, 		false, 		"0", 	"Feedback Setpoint2 Voltage");
		filByteToothRead[44]	= new ByteTooth("V_SP3",			"FLOAT",		98, 	0, 		false, 		"0", 	"Feedback Setpoint3 Voltage");
		filByteToothRead[45]	= new ByteTooth("I_HIHI",			"FLOAT",		102, 	0, 		false, 		"0", 	"Feedback Current Alarm High");
		filByteToothRead[46]	= new ByteTooth("I_LOLO",			"FLOAT",		106, 	0, 		false, 		"0", 	"Feedback Current Alarm Low");
		filByteToothRead[47]	= new ByteTooth("I_HIGH",			"FLOAT",		110, 	0, 		false, 		"0", 	"Feedback Current Warning High");
		filByteToothRead[48]	= new ByteTooth("I_LOW",			"FLOAT",		114, 	0, 		false, 		"0", 	"Feedback Current Warning Low");
		filByteToothRead[49]	= new ByteTooth("W_HIHI",			"FLOAT",		118, 	0, 		false, 		"0", 	"Feedback Power Alarm High");
		filByteToothRead[50]	= new ByteTooth("W_LOLO",			"FLOAT",		122, 	0, 		false, 		"0", 	"Feedback Power Alarm Low");
		filByteToothRead[51]	= new ByteTooth("W_HIGH",			"FLOAT",		126, 	0, 		false, 		"0", 	"Feedback Power Warning High");
		filByteToothRead[52]	= new ByteTooth("W_LOW",			"FLOAT",		130, 	0, 		false, 		"0", 	"Feedback Power Warning Low");
		filByteToothRead[53]	= new ByteTooth("I_EGU_SP",			"FLOAT",		134, 	0, 		false, 		"0", 	"CurrentSetpoint in engunit");
		filByteToothRead[54]	= new ByteTooth("FIL_MAX_I",		"FLOAT",		138, 	0, 		false, 		"0", 	"The maximum allowed Filament current hardwired in the PSU code");
		filByteToothRead[55]	= new ByteTooth("FIL_MAX_W",		"FLOAT",		142, 	0, 		false, 		"0", 	"The maximum allowed Filament power hardwired in the PSU code");
		filByteToothRead[56]	= new ByteTooth("TOL_InWARMUP",		"FLOAT",		146, 	0, 		false, 		"0", 	"Feedback for Tolareance during Warmup between IMON and IPGM");
		filByteToothRead[57]	= new ByteTooth("TMPST",			"LONG",			150, 	0, 		false, 		"0", 	"Last Interlock or Warning Timestamp");
		filByteToothRead[58]	= new ByteTooth("TOTAL_WARMPUP_T",	"FLOAT",		158, 	0, 		false, 		"0", 	"Sum of the SetPoints time");

		ByteTooth[] filByteToothWrite = new ByteTooth[36];
		filByteToothWrite[0]		= new ByteTooth("START",		"BOOLEAN",		0, 		0, 		true, 		"0", 	"Start PSU operation. TRUE=ENABLED");
		filByteToothWrite[1]		= new ByteTooth("STOP",			"BOOLEAN",		0, 		1, 		true, 		"0", 	"Stop PSU and reset Timers TRUE = STOP");
		filByteToothWrite[2]		= new ByteTooth("FORCE",		"BOOLEAN",		0, 		2, 		true, 		"0", 	"Force PSU block TRUE = FORCE all inputs");
		filByteToothWrite[3]		= new ByteTooth("S_ON_STATUS",	"BOOLEAN",		0, 		3, 		true, 		"0", 	"Force ON_STATUS input");
		filByteToothWrite[4]		= new ByteTooth("S_IVAL",		"BOOLEAN",		0, 		4, 		true, 		"0", 	"Force I_REG input");
		filByteToothWrite[5]		= new ByteTooth("S_LOC_REM",	"BOOLEAN",		0, 		5, 		true, 		"0", 	"Force LOC_REM input");
		filByteToothWrite[6]		= new ByteTooth("S_FAULT",		"BOOLEAN",		0, 		6, 		true, 		"0", 	"Force FAULT input");
		filByteToothWrite[7]		= new ByteTooth("I_RAMP_ON",	"BOOLEAN",		0, 		7, 		true, 		"0", 	"Enable ramping for Current SP");
		filByteToothWrite[8]		= new ByteTooth("V_RAMP_ON",	"BOOLEAN",		1, 		0, 		true, 		"0", 	"Enable ramping for Voltage SP");
		filByteToothWrite[9]		= new ByteTooth("RESET",		"BOOLEAN",		1, 		1, 		true, 		"0", 	"Reset PSU");
		filByteToothWrite[10]		= new ByteTooth("TIME_COLD_SP1","FLOAT",		2, 		0, 		true, 		"0", 	"Setpoint1 Time");
		filByteToothWrite[11]		= new ByteTooth("TIME_COLD_SP2","FLOAT",		6, 		0, 		true, 		"0", 	"Setpoint2 Time");
		filByteToothWrite[12]		= new ByteTooth("TIME_COLD_SP3","FLOAT",		10, 	0, 		true, 		"0", 	"Setpoint3 Time");
		filByteToothWrite[13]		= new ByteTooth("TIME_WARM_SP1","FLOAT",		14, 	0, 		true, 		"0", 	"Setpoint1 Time if the PSU is already warm");
		filByteToothWrite[14]		= new ByteTooth("TIME_WARM_SP2","FLOAT",		18, 	0, 		true, 		"0", 	"Setpoint2 Time if the PSU is already warm");
		filByteToothWrite[15]		= new ByteTooth("TIME_WARM_SP3","FLOAT",		22, 	0, 		true, 		"0", 	"Setpoint3 Time if the PSU is already warm");
		filByteToothWrite[16]		= new ByteTooth("I_SP1",		"FLOAT",		26, 	0, 		true, 		"0", 	"Setpoint1 Current");
		filByteToothWrite[17]		= new ByteTooth("I_SP2",		"FLOAT",		30, 	0, 		true, 		"0", 	"Setpoint2 Current");
		filByteToothWrite[18]		= new ByteTooth("I_SP3",		"FLOAT",		34, 	0, 		true, 		"0", 	"Setpoint3 Current");
		filByteToothWrite[19]		= new ByteTooth("V_SP1",		"FLOAT",		38, 	0, 		true, 		"0", 	"Setpoint1 Voltage");
		filByteToothWrite[20]		= new ByteTooth("V_SP2",		"FLOAT",		42, 	0, 		true, 		"0", 	"Setpoint2 Voltage");
		filByteToothWrite[21]		= new ByteTooth("V_SP3",		"FLOAT",		46, 	0, 		true, 		"0", 	"Setpoint3 Voltage");
		filByteToothWrite[22]		= new ByteTooth("S_VMON",		"FLOAT",		50, 	0, 		true, 		"0", 	"Force VMON input");
		filByteToothWrite[23]		= new ByteTooth("S_IMON",		"FLOAT",		54, 	0, 		true, 		"0", 	"Force IMON input");
		filByteToothWrite[24]		= new ByteTooth("S_WMON",		"FLOAT",		58, 	0, 		true, 		"0", 	"Force WMON input");
		filByteToothWrite[25]		= new ByteTooth("V_RAMP_RATE",	"FLOAT",		62, 	0, 		true, 		"0", 	"Voltage Ramp rate unit/sec (not used now)");
		filByteToothWrite[26]		= new ByteTooth("I_RAMP_RATE",	"FLOAT",		66, 	0, 		true, 		"0", 	"Current Ramp rate unit/sec (not used now)");
		filByteToothWrite[27]		= new ByteTooth("I_HIHI",		"FLOAT",		70, 	0, 		true, 		"0", 	"Current Alarm High");
		filByteToothWrite[28]		= new ByteTooth("I_LOLO",		"FLOAT",		74, 	0, 		true, 		"0", 	"Current Alarm Low");
		filByteToothWrite[29]		= new ByteTooth("I_HIGH",		"FLOAT",		78, 	0, 		true, 		"0", 	"Current Warning High");
		filByteToothWrite[30]		= new ByteTooth("I_LOW",		"FLOAT",		82, 	0, 		true, 		"0", 	"Current Warning Low");
		filByteToothWrite[31]		= new ByteTooth("W_HIHI",		"FLOAT",		86, 	0, 		true, 		"0", 	"Power Alarm High");
		filByteToothWrite[32]		= new ByteTooth("W_LOLO",		"FLOAT",		90, 	0, 		true, 		"0", 	"Power Alarm Low");
		filByteToothWrite[33]		= new ByteTooth("W_HIGH",		"FLOAT",		94, 	0, 		true, 		"0", 	"Power Warning High");
		filByteToothWrite[34]		= new ByteTooth("W_LOW",		"FLOAT",		98, 	0, 		true, 		"0", 	"Power Warning Low");
		filByteToothWrite[35]		= new ByteTooth("TOL_InWARMUP_SP",	"FLOAT",		102, 	0, 		true, 		"0", 	"Maximum allowed difference between IMON and IPGM");

		ByteTooth[] solByteToothRead = new ByteTooth[37];
		solByteToothRead[0]		= new ByteTooth("ON_STATUS",	"BOOLEAN",		0, 		0, 		false, 		"0", 	"PSU is Operating normally");
		solByteToothRead[1]		= new ByteTooth("STARTED",		"BOOLEAN",		0, 		1, 		false, 		"0", 	"PSU has received the ON Command");
		solByteToothRead[2]		= new ByteTooth("STOPPED",		"BOOLEAN",		0, 		2, 		false, 		"0", 	"PSU is NOT operating");
		solByteToothRead[3]		= new ByteTooth("I_REG",		"BOOLEAN",		0, 		3, 		false, 		"0", 	"HIGH= the PSU is in constant cuttent mode");
		solByteToothRead[4]		= new ByteTooth("LOC_REM_ST",	"BOOLEAN",		0, 		4, 		false, 		"0", 	"HIGH=REMOTE,FALSE=LOCAL");
		solByteToothRead[5]		= new ByteTooth("N_LATCH",		"BOOLEAN",		0, 		5, 		false, 		"0", 	"Disable Latching from PLC code");
		solByteToothRead[6]		= new ByteTooth("N_FORCE",		"BOOLEAN",		0, 		6, 		false, 		"0", 	"Disable forcing from PLC code");
		solByteToothRead[7]		= new ByteTooth("IsFIRST",		"BOOLEAN",		0, 		7, 		false, 		"0", 	"Is the PSU Interlock the first Interlock?");
		solByteToothRead[8]		= new ByteTooth("WARNING",		"BOOLEAN",		1, 		0, 		false, 		"0", 	"PSU Warning Bit");
		solByteToothRead[9]		= new ByteTooth("INTERLOCK",	"BOOLEAN",		1, 		1, 		false, 		"0", 	"PSU Interlock Bit");
		solByteToothRead[10]	= new ByteTooth("FORCE_ON",		"BOOLEAN",		1, 		2, 		false, 		"0", 	"Feedback ForceON");
		solByteToothRead[11]	= new ByteTooth("S_ON_STATUS",	"BOOLEAN",		1, 		3, 		false, 		"0", 	"Feedback ON_STATUS input");
		solByteToothRead[12]	= new ByteTooth("S_IREG",		"BOOLEAN",		1, 		4, 		false, 		"0", 	"Feedback I_REG input");
		solByteToothRead[13]	= new ByteTooth("S_FAULT",		"BOOLEAN",		1, 		5, 		false, 		"0", 	"Feedback FAULT input");
		solByteToothRead[14]	= new ByteTooth("S_LOC_REM",	"BOOLEAN",		1, 		6, 		false, 		"0", 	"Feedback LOC_REM input");
		solByteToothRead[15]	= new ByteTooth("DTYP",			"BYTE",			2, 		0, 		false, 		"0", 	"PSU Type");
		solByteToothRead[16]	= new ByteTooth("ERR",			"SHORT",		4, 		0, 		false, 		"0", 	"Different error bits stored in the word descibed in the code");
		solByteToothRead[17]	= new ByteTooth("UPTIME_NO_HV",	"INT",			6, 		0, 		false, 		"0", 	"PSU uptime without HighVoltage");
		solByteToothRead[18]	= new ByteTooth("VMON",			"FLOAT",		10, 		0, 		false, 		"0", 	"Actual Voltage (V)");
		solByteToothRead[19]	= new ByteTooth("IMON",			"FLOAT",		14, 		0, 		false, 		"0", 	"Actual Current (I)");
		solByteToothRead[20]	= new ByteTooth("S_VMON",		"FLOAT",		18, 		0, 		false, 		"0", 	"Feedback VMON input");
		solByteToothRead[21]	= new ByteTooth("S_IMON",		"FLOAT",		22, 		0, 		false, 		"0", 	"Feedback IMON input");
		solByteToothRead[22]	= new ByteTooth("I_SP",			"FLOAT",		26, 		0, 		false, 		"0", 	"Feedback Setpoint Current");
		solByteToothRead[23]	= new ByteTooth("V_SP",			"FLOAT",		30, 		0, 		false, 		"0", 	"Feedback Setpoint Voltage");
		solByteToothRead[24]	= new ByteTooth("I_HIHI",		"FLOAT",		34, 		0, 		false, 		"0", 	"Feedback Current Alarm High");
		solByteToothRead[25]	= new ByteTooth("I_LOLO",		"FLOAT",		38, 		0, 		false, 		"0", 	"Feedback Current Alarm Low");
		solByteToothRead[26]	= new ByteTooth("I_HIGH",		"FLOAT",		42, 		0, 		false, 		"0", 	"Feedback Current Warning High");
		solByteToothRead[27]	= new ByteTooth("I_LOW",		"FLOAT",		46, 		0, 		false, 		"0", 	"Feedback Current Warning Low");
		solByteToothRead[28]	= new ByteTooth("V_HIHI",		"FLOAT",		50, 		0, 		false, 		"0", 	"Feedback Voltage Alarm High");
		solByteToothRead[29]	= new ByteTooth("V_LOLO",		"FLOAT",		54, 		0, 		false, 		"0", 	"Feedback Voltage Alarm Low");
		solByteToothRead[30]	= new ByteTooth("V_HIGH",		"FLOAT",		58, 		0, 		false, 		"0", 	"Feedback Voltage Warning High");
		solByteToothRead[31]	= new ByteTooth("V_LOW",		"FLOAT",		62, 		0, 		false, 		"0", 	"Feedback Voltage Warning Low");
		solByteToothRead[32]	= new ByteTooth("V_EGU_SP",		"FLOAT",		66, 		0, 		false, 		"0", 	"Voltage Setpoint in engunit");
		solByteToothRead[33]	= new ByteTooth("I_EGU_SP",		"FLOAT",		70, 		0, 		false, 		"0", 	"CurrentSetpoint in engunit");
		solByteToothRead[34]	= new ByteTooth("TMPST",		"LONG",			74, 		0, 		false, 		"0", 	"Last Interlock or Warning Timestamp");
		solByteToothRead[35]	= new ByteTooth("SOL_MAX_I",	"FLOAT",		82, 		0, 		false, 		"0", 	"The maximum allowed Solenoid current hardwired in the PSU code");
		solByteToothRead[36]	= new ByteTooth("SOL_MAX_V",	"FLOAT",		86, 		0, 		false, 		"0", 	"The maximum allowed Solenoid voltage hardwired in the PSU code");

		ByteTooth[] solByteToothWrite = new ByteTooth[20];
		solByteToothWrite[0]		= new ByteTooth("START",		"BOOLEAN",		0, 		0, 		true, 		"0", 	"Start PSU operation. TRUE=ENABLED");
		solByteToothWrite[1]		= new ByteTooth("STOP",			"BOOLEAN",		0, 		1, 		true, 		"0", 	"Stop PSU and reset Timers TRUE = STOP");
		solByteToothWrite[2]		= new ByteTooth("FORCE",		"BOOLEAN",		0, 		2, 		true, 		"0", 	"Force PSU block TRUE = FORCE all inputs");
		solByteToothWrite[3]		= new ByteTooth("S_ON_STATUS",	"BOOLEAN",		0, 		3, 		true, 		"0", 	"Force ON_STATUS input");
		solByteToothWrite[4]		= new ByteTooth("S_IREG",		"BOOLEAN",		0, 		4, 		true, 		"0", 	"Force I_REG input");
		solByteToothWrite[5]		= new ByteTooth("S_LOC_REM",	"BOOLEAN",		0, 		5, 		true, 		"0", 	"Force LOC_REM input");
		solByteToothWrite[6]		= new ByteTooth("S_FAULT",		"BOOLEAN",		0, 		6, 		true, 		"0", 	"Force FAULT input");
		solByteToothWrite[7]		= new ByteTooth("RESET",		"BOOLEAN",		0, 		7, 		true, 		"0", 	"Reset PSU");
		solByteToothWrite[8]		= new ByteTooth("I_SP",			"FLOAT",		2, 		0, 		true, 		"0", 	"Setpoint Current");
		solByteToothWrite[9]		= new ByteTooth("V_SP",			"FLOAT",		6, 		0, 		true, 		"0", 	"Setpoint Voltage");
		solByteToothWrite[10]		= new ByteTooth("S_VMON",		"FLOAT",		10, 	0, 		true, 		"0", 	"Force VMON input");
		solByteToothWrite[11]		= new ByteTooth("S_IMON",		"FLOAT",		14, 	0, 		true, 		"0", 	"Force IMON input");
		solByteToothWrite[12]		= new ByteTooth("I_HIHI",		"FLOAT",		18, 	0, 		true, 		"0", 	"Current Alarm High");
		solByteToothWrite[13]		= new ByteTooth("I_LOLO",		"FLOAT",		22, 	0, 		true, 		"0", 	"Current Alarm Low");
		solByteToothWrite[14]		= new ByteTooth("I_HIGH",		"FLOAT",		26, 	0, 		true, 		"0", 	"Current Warning High");
		solByteToothWrite[15]		= new ByteTooth("I_LOW",		"FLOAT",		30, 	0, 		true, 		"0", 	"Current Warning Low");
		solByteToothWrite[16]		= new ByteTooth("V_HIHI",		"FLOAT",		34, 	0, 		true, 		"0", 	"Voltage Alarm High");
		solByteToothWrite[17]		= new ByteTooth("V_LOLO",		"FLOAT",		38, 	0, 		true, 		"0", 	"Voltage Alarm Low");
		solByteToothWrite[18]		= new ByteTooth("V_HIGH",		"FLOAT",		42, 	0, 		true, 		"0", 	"Voltage Warning High");
		solByteToothWrite[19]		= new ByteTooth("V_LOW",		"FLOAT",		46, 	0, 		true, 		"0", 	"Voltage Warning Low");
		
		ByteGear[] byteGearF = new 	ByteGear[1];
		byteGearF[0] = new ByteGear("FILAMENT", 		0, 			0);

		for (int ii = 0; ii < byteGearF.length; ++ii)
		{
			for (int ir = 0; ir < filByteToothRead.length; ++ir)
			{
				byteGearF[ii].getReadToothList().add(new ByteTooth(filByteToothRead[ir].getJsonObject()));
			}
			for (int iw = 0; iw < filByteToothWrite.length; ++iw)
			{
				byteGearF[ii].getWriteToothList().add(new ByteTooth(filByteToothWrite[iw].getJsonObject()));
			}
		}

		ByteGear[] byteGearS = new 	ByteGear[3];
		byteGearS[0] = new ByteGear("SOLENOID1", 		160, 			106);
		byteGearS[1] = new ByteGear("SOLENOID2", 		250, 			156);
		byteGearS[2] = new ByteGear("SOLENOID3", 		340, 			206);

		for (int ii = 0; ii < byteGearF.length; ++ii)
		{
			for (int ir = 0; ir < solByteToothRead.length; ++ir)
			{
				byteGearS[ii].getReadToothList().add(new ByteTooth(solByteToothRead[ir].getJsonObject()));
			}
			for (int iw = 0; iw < solByteToothWrite.length; ++iw)
			{
				byteGearS[ii].getWriteToothList().add(new ByteTooth(solByteToothWrite[iw].getJsonObject()));
			}
		}
		ByteGearBox byteGearBox = new ByteGearBox("tcp://broker.shiftr.io", 1883, "klyPlcProtoPsu", 430, 256);
		for (int ii = 0; ii < byteGearF.length; ++ii)
		{
			byteGearBox.getByteGearList().add(byteGearF[ii]);
		}
		for (int ii = 0; ii < byteGearS.length; ++ii)
		{
			byteGearBox.getByteGearList().add(byteGearS[ii]);
		}
		
		byteGearBox.writeToFile("klyPlcProtoPsu.json",false);
		
	}

}
