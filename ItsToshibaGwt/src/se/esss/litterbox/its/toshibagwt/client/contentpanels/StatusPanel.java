package se.esss.litterbox.its.toshibagwt.client.contentpanels;

import java.util.Date;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Grid;

import se.esss.litterbox.its.toshibagwt.client.EntryPointApp;
import se.esss.litterbox.its.toshibagwt.client.gskel.GskelLoadWaiter;

public class StatusPanel extends CaptionPanel
{
	ByteToothTempGaugePlot[] statPanel = new ByteToothTempGaugePlot[15];
	private int plotLoadTimeMs = 100;
	Date startTime = new Date();
	EntryPointApp entryPointApp;
	int ngridRow = 3;
	int ngridCol = 6;
	
	public StatusPanel(EntryPointApp entryPointApp)
	{
		super();
		this.setCaptionHTML("<font size=\"+2\">Status</font>");
		Grid statPanelGrid = new Grid (ngridRow,ngridCol);
		
		statPanel[0] = new ByteToothTempGaugePlot("IP I", 		"klyPlcProtoAio", "KLY_IP_ISn_Current",				"EGU",	"LOLO",		"LOW",	"HIGH",		"HIHI", 	entryPointApp);
		statPanel[1] = new ByteToothTempGaugePlot("Fil I", 		"klyPlcProtoPsu", "FILAMENT",						"IMON",	"I_LOLO",	"I_LOW","I_HIGH",	"I_HIHI",	entryPointApp);
		statPanel[2] = new ByteToothTempGaugePlot("Fil W", 		"klyPlcProtoPsu", "FILAMENT",						"WMON",	"W_LOLO",	"W_LOW","W_HIGH",	"W_HIHI",	entryPointApp);
		statPanel[3] = new ByteToothTempGaugePlot("Sol1 I",		"klyPlcProtoPsu", "SOLENOID1",						"IMON",	"I_LOLO",	"I_LOW","I_HIGH",	"I_HIHI",	entryPointApp);
		statPanel[4] = new ByteToothTempGaugePlot("Sol2 I", 	"klyPlcProtoPsu", "SOLENOID2",						"IMON",	"I_LOLO",	"I_LOW","I_HIGH",	"I_HIHI",	entryPointApp);
		statPanel[5] = new ByteToothTempGaugePlot("Oil Sur T", 	"klyPlcProtoAio", "KLY_Oil_TSn_SurfTemp",			"EGU",	"LOLO",		"LOW",	"HIGH",		"HIHI",		entryPointApp);
		statPanel[6] = new ByteToothTempGaugePlot("Sol In T", 	"klyPlcProtoAio", "KLY_Sol_TSn_WatInletTemp",		"EGU",	"LOLO",		"LOW",	"HIGH",		"HIHI",		entryPointApp);
		statPanel[7] = new ByteToothTempGaugePlot("Sol Out T", 	"klyPlcProtoAio", "KLY_Sol_TSn_WatOutletTemp",		"EGU",	"LOLO",		"LOW",	"HIGH",		"HIHI",		entryPointApp);
		statPanel[8] = new ByteToothTempGaugePlot("Sol Surf T", "klyPlcProtoAio", "KLY_Sol_TSn_SurfTemp",			"EGU",	"LOLO",		"LOW",	"HIGH",		"HIHI",		entryPointApp);
		statPanel[9] = new ByteToothTempGaugePlot("Win Out T", 	"klyPlcProtoAio", "KLY_Win_TSn_WatOutletTemp",		"EGU",	"LOLO",		"LOW",	"HIGH",		"HIHI",		entryPointApp);
		statPanel[10] = new ByteToothTempGaugePlot("Win Slv T", "klyPlcProtoAio", "KLY_Win_TSn_WatOutletSleeveTemp","EGU",	"LOLO",		"LOW",	"HIGH",		"HIHI",		entryPointApp);
		statPanel[11] = new ByteToothTempGaugePlot("Col Top T", "klyPlcProtoAio", "KLY_Coll_TSn_TopTemp",			"EGU",	"LOLO",		"LOW",	"HIGH",		"HIHI",		entryPointApp);
		statPanel[12] = new ByteToothTempGaugePlot("Col Edgp T","klyPlcProtoAio", "KLY_Coll_TSn_EdgeTemp",			"EGU",	"LOLO",		"LOW",	"HIGH",		"HIHI",		entryPointApp);
		statPanel[13] = new ByteToothTempGaugePlot("Body In T",	"klyPlcProtoAio", "KLY_Body_TSn_WatInletTemp",		"EGU",	"LOLO",		"LOW",	"HIGH",		"HIHI",		entryPointApp);
		statPanel[14] = new ByteToothTempGaugePlot("Body Out T","klyPlcProtoAio", "KLY_Body_TSn_WatOutletTemp",		"EGU",	"LOLO",		"LOW",	"HIGH",		"HIHI",		entryPointApp);
		
		int igridRow = 0;
		int igridCol = 0;
		for (int ii = 0; ii < statPanel.length; ++ii)
		{
			statPanelGrid.setWidget(igridRow, igridCol, statPanel[ii]);
			++igridCol;
			if (igridCol == ngridCol)
			{
				igridCol = 0;
				++igridRow;
			}
		}
		
		add(statPanelGrid);
		addStatPanel(0);
	}
	private void addStatPanel(int ipanel)
	{
		statPanel[ipanel].initialize();
		new GaugePlotWaiter(plotLoadTimeMs, ipanel);
	}
	private void updateReadings()
	{
		for (int ii = 0; ii < statPanel.length; ++ii) statPanel[ii].updateReadings();
	}
	class GaugePlotWaiter extends GskelLoadWaiter
	{
		public GaugePlotWaiter(int loopTimeMillis, int itask) 
		{
			super(loopTimeMillis, itask);

		}
		@Override
		public boolean isLoaded() 
		{
			boolean loaded = false;
			for (int ii = 0; ii < statPanel.length; ++ii)
				if (getItask() == ii) loaded = statPanel[ii].isLoaded();
			return loaded;
		}
		@Override
		public void taskAfterLoad() 
		{
			for (int ii = 0; ii < (statPanel.length - 1); ++ii)
				if (getItask() == ii) addStatPanel(ii + 1);
			if (getItask() == (statPanel.length - 1))
			{
				UpdateReadingsTimer urt = new UpdateReadingsTimer();
				urt.scheduleRepeating(500);
			}
		}
		
	}
	class UpdateReadingsTimer extends Timer
	{
		@Override
		public void run() {updateReadings();}
	}

}
