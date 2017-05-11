package se.esss.litterbox.its.toshibagwt.client.contentpanels;

import java.util.Date;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Grid;

import se.esss.litterbox.its.toshibagwt.client.EntryPointApp;
import se.esss.litterbox.its.toshibagwt.client.gskel.GskelLoadWaiter;

public class StatusPanel extends CaptionPanel
{
	ThermCaptionPanel[] statPanel = new ThermCaptionPanel[13];
	private int plotLoadTimeMs = 100;
	Date startTime = new Date();
	EntryPointApp entryPointApp;
	
	public StatusPanel(EntryPointApp entryPointApp)
	{
		super();
		this.setCaptionHTML("<font size=\"+2\">Status</font>");
		Grid statPanelGrid = new Grid (5,6);
		
		statPanel[0] = new ThermCaptionPanel("IP I", 		"klyPlcProtoAio", "KLY_IP_ISn_Current",				"EGU",	"LOLO",		"LOW",	"HIGH",		"HIHI", 	entryPointApp);
		statPanel[1] = new ThermCaptionPanel("Fil I", 		"klyPlcProtoPsu", "FILAMENT",						"IMON",	"I_LOLO",	"I_LOW","I_HIGH",	"I_HIHI",	entryPointApp);
		statPanel[2] = new ThermCaptionPanel("Sol1 I",		"klyPlcProtoPsu", "SOLENOID1",						"IMON",	"I_LOLO",	"I_LOW","I_HIGH",	"I_HIHI",	entryPointApp);
		statPanel[3] = new ThermCaptionPanel("Sol2 I", 		"klyPlcProtoPsu", "SOLENOID2",						"IMON",	"I_LOLO",	"I_LOW","I_HIGH",	"I_HIHI",	entryPointApp);
		statPanel[4] = new ThermCaptionPanel("Oil Sur T", 	"klyPlcProtoAio", "KLY_Oil_TSn_SurfTemp",			"EGU",	"LOLO",		"LOW",	"HIGH",		"HIHI",		entryPointApp);
		statPanel[5] = new ThermCaptionPanel("Sol In T", 	"klyPlcProtoAio", "KLY_Sol_TSn_WatInletTemp",		"EGU",	"LOLO",		"LOW",	"HIGH",		"HIHI",		entryPointApp);
		statPanel[6] = new ThermCaptionPanel("Sol Surf T", 	"klyPlcProtoAio", "KLY_Sol_TSn_SurfTemp",			"EGU",	"LOLO",		"LOW",	"HIGH",		"HIHI",		entryPointApp);
		statPanel[7] = new ThermCaptionPanel("Win Out T", 	"klyPlcProtoAio", "KLY_Win_TSn_WatOutletTemp",		"EGU",	"LOLO",		"LOW",	"HIGH",		"HIHI",		entryPointApp);
		statPanel[8] = new ThermCaptionPanel("Win Slv T", 	"klyPlcProtoAio", "KLY_Win_TSn_WatOutletSleeveTemp","EGU",	"LOLO",		"LOW",	"HIGH",		"HIHI",		entryPointApp);
		statPanel[9] = new ThermCaptionPanel("Col Top T", 	"klyPlcProtoAio", "KLY_Coll_TSn_TopTemp",			"EGU",	"LOLO",		"LOW",	"HIGH",		"HIHI",		entryPointApp);
		statPanel[10] = new ThermCaptionPanel("Col Edgp T", "klyPlcProtoAio", "KLY_Coll_TSn_EdgeTemp",			"EGU",	"LOLO",		"LOW",	"HIGH",		"HIHI",		entryPointApp);
		statPanel[11] = new ThermCaptionPanel("Body In T", 	"klyPlcProtoAio", "KLY_Body_TSn_WatInletTemp",		"EGU",	"LOLO",		"LOW",	"HIGH",		"HIHI",		entryPointApp);
		statPanel[12] = new ThermCaptionPanel("Body Out T", "klyPlcProtoAio", "KLY_Body_TSn_WatOutletTemp",		"EGU",	"LOLO",		"LOW",	"HIGH",		"HIHI",		entryPointApp);
		
		statPanelGrid.setWidget(0, 0, statPanel[0]);
		statPanelGrid.setWidget(0, 1, statPanel[1]);
		statPanelGrid.setWidget(0, 2, statPanel[2]);
		statPanelGrid.setWidget(0, 3, statPanel[3]);
		statPanelGrid.setWidget(0, 4, statPanel[4]);
		statPanelGrid.setWidget(0, 5, statPanel[5]);
		statPanelGrid.setWidget(1, 0, statPanel[6]);
		statPanelGrid.setWidget(1, 1, statPanel[7]);
		statPanelGrid.setWidget(1, 2, statPanel[8]);
		statPanelGrid.setWidget(1, 3, statPanel[9]);
		statPanelGrid.setWidget(1, 4, statPanel[10]);
		statPanelGrid.setWidget(1, 5, statPanel[11]);
		statPanelGrid.setWidget(2, 0, statPanel[12]);
		
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
