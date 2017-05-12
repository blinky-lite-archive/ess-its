package se.esss.litterbox.its.toshibagwt.client.contentpanels;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.VerticalPanel;

import se.esss.litterbox.its.toshibagwt.client.EntryPointApp;
import se.esss.litterbox.its.toshibagwt.client.gskel.GskelLoadWaiter;
import se.esss.litterbox.its.toshibagwt.client.gskel.GskelVerticalPanel;

public class PlotterPanel extends GskelVerticalPanel 
{
	ByteToothTimeLinePlotPanel[] byteToothPlot = new ByteToothTimeLinePlotPanel[1];
	int numPts = 500;
	private int plotLoadTimeMs = 100;
	String[] title  = new String[byteToothPlot.length];
	String[][] byteGearBoxTopic = new String[byteToothPlot.length][];
	String[][] byteGearName = new String[byteToothPlot.length][];
	String[][] valByteToothName = new String[byteToothPlot.length][];
	String[][] legend = new String[byteToothPlot.length][];
	double[][] valMult = new double[byteToothPlot.length][];
	VerticalPanel plotPanel = new VerticalPanel();

	public PlotterPanel(EntryPointApp entryPointApp) 
	{
		super(true, entryPointApp);
		
		int numPts = 1000;
		title[0] = "Filament Warmup";
		String[] topic0	= {"klyPlcProtoAio",		"klyPlcProtoPsu",		"klyPlcProtoPsu"};
		String[] name0 	= {"KLY_IP_ISn_Current",	"FILAMENT",				"FILAMENT"};
		String[] tooth0	= {"EGU",					"IMON",					"WMON"};
		double[] val0	= {0.01,					1.0,					0.1};
		String[] leg0	= {"IP I x 0.01",			"Fil I",				"Fil W x 0.1"};
		byteGearBoxTopic[0]	= topic0;
		byteGearName[0]		= name0;
		valByteToothName[0]	= tooth0;
		valMult[0]			= val0;
		legend[0]			= leg0;
		
		int iplot = 0;
		byteToothPlot[iplot] = new ByteToothTimeLinePlotPanel(numPts, title[iplot], byteGearBoxTopic[iplot], byteGearName[iplot], valByteToothName[iplot], valMult[iplot], legend[iplot], entryPointApp);
		plotPanel.add(byteToothPlot[iplot]);
		
		add(plotPanel);
		loadPlotPanel(0);
	}
	private void loadPlotPanel(int ipanel)
	{
		if (byteToothPlot.length <= ipanel) return;
		byteToothPlot[ipanel].initialize();
		new GaugePlotWaiter(plotLoadTimeMs, ipanel);
	}
	private void updateReadings()
	{
		for (int ii = 0; ii < byteToothPlot.length; ++ii) byteToothPlot[ii].updateReadings();
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
			for (int ii = 0; ii < byteToothPlot.length; ++ii)
				if (getItask() == ii) loaded = byteToothPlot[ii].isLoaded();
			return loaded;
		}
		@Override
		public void taskAfterLoad() 
		{
			for (int ii = 0; ii < (byteToothPlot.length - 1); ++ii)
				if (getItask() == ii) loadPlotPanel(ii + 1);
			if (getItask() == (byteToothPlot.length - 1))
			{
				UpdateReadingsTimer urt = new UpdateReadingsTimer();
				urt.scheduleRepeating(1000);
			}
		}
		
	}
	class UpdateReadingsTimer extends Timer
	{
		@Override
		public void run() {updateReadings();}
	}

}
