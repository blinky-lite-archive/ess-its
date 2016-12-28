package se.esss.litterbox.itsenvmongwt.client.contentpanels;

import java.util.Date;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.CaptionPanel;

public class TimePlotCaptionPanel extends CaptionPanel
{
	TimeLinePlotPanel timeLinePlot;
	int ipt = 0;
	int numPts = 300;
	long startDataDate;
	private boolean loaded = false;
	
	public boolean isLoaded() {return loaded;}
	
	public TimePlotCaptionPanel(int numPts, String plotWidth, String plotHeight)
	{
		super("TimePlot");
		this.numPts = numPts;
		loaded = false;
		String[] timePlotLegend = {"cpm", "tempDht11", "humidDht11", "temp", "photoAvg .1", "photo .1"};
		timeLinePlot = new TimeLinePlotPanel(numPts, 6, "Time Plot", "Time (sec)", "Value", timePlotLegend, plotWidth, plotHeight);
		LoadPlotTimer lpt = new LoadPlotTimer(this);
		lpt.scheduleRepeating(50);
	}
	public void updateReadings(String[][] readingfromServer)
	{
		if (!loaded) return;
		timeLinePlot.getXaxis()[ipt] = (double) (new Date().getTime() - startDataDate);
		timeLinePlot.getTraces()[0][ipt] = Double.parseDouble(readingfromServer[0][1]);
		timeLinePlot.getTraces()[1][ipt] = Double.parseDouble(readingfromServer[1][1]);
		timeLinePlot.getTraces()[2][ipt] = Double.parseDouble(readingfromServer[2][1]);
		timeLinePlot.getTraces()[3][ipt] = Double.parseDouble(readingfromServer[3][1]);
		timeLinePlot.getTraces()[4][ipt] = Double.parseDouble(readingfromServer[4][1]) * 0.1;
		timeLinePlot.getTraces()[5][ipt] = Double.parseDouble(readingfromServer[5][1]) * 0.1;
		timeLinePlot.draw();
		++ipt;
		if (ipt >= numPts) ipt = 0;
	}
	private static class LoadPlotTimer extends Timer
	{
		TimePlotCaptionPanel timePlotCaptionPanel;
		LoadPlotTimer(TimePlotCaptionPanel timePlotCaptionPanel)
		{
			this.timePlotCaptionPanel = timePlotCaptionPanel;
			timePlotCaptionPanel.loaded = false;
			timePlotCaptionPanel.timeLinePlot.initialize();
			timePlotCaptionPanel.add(timePlotCaptionPanel.timeLinePlot);
			timePlotCaptionPanel.ipt = 0;
		}
		@Override
		public void run() 
		{
			if (!timePlotCaptionPanel.timeLinePlot.isLoaded()) 
			{
				return;
			}
			else
			{
				timePlotCaptionPanel.loaded = true;
				timePlotCaptionPanel.startDataDate = new Date().getTime();
				this.cancel();
			}

		}
	}
}
