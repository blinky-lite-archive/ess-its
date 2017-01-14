package se.esss.litterbox.its.cernrfgwt.client.googleplots;

import java.util.Date;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.CaptionPanel;

public class TimePlotCaptionPanel extends CaptionPanel
{
	TimeLinePlotPanel timeLinePlot;
	int ipt = 0;
	int numPts = 300;
	int numTraces = -1;
	long startDataDate;
	private boolean loaded = false;
	
	public boolean isLoaded() {return loaded;}
	
	public TimePlotCaptionPanel(int numPts, String title, String yaxisLabel, String[] timePlotLegend, String plotWidth, String plotHeight)
	{
		super("TimePlot");
		this.numPts = numPts;
		loaded = false;
		numTraces = timePlotLegend.length;
		timeLinePlot = new TimeLinePlotPanel(numPts, numTraces, title, "Time (sec)", yaxisLabel, timePlotLegend, plotWidth, plotHeight);
		LoadPlotTimer lpt = new LoadPlotTimer(this);
		lpt.scheduleRepeating(50);
	}
	public void updateReadings(double[] timeSlicedata)
	{
		if (!loaded) return;
		timeLinePlot.getXaxis()[ipt] = (double) (new Date().getTime() - startDataDate);
		for (int ii = 0; ii < numTraces; ++ii)
			timeLinePlot.getTraces()[ii][ipt] = timeSlicedata[ii];
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
