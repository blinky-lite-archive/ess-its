package se.esss.litterbox.its.toshibagwt.client.contentpanels;

import java.util.Date;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.CaptionPanel;

import se.esss.litterbox.its.toshibagwt.client.EntryPointApp;
import se.esss.litterbox.its.toshibagwt.client.bytegearboxservice.ByteGearBoxData;
import se.esss.litterbox.its.toshibagwt.client.googleplots.TimeLineChartPlotPanel;
import se.esss.litterbox.its.toshibagwt.shared.bytegearboxgwt.ByteGearGwt;
import se.esss.litterbox.its.toshibagwt.shared.bytegearboxgwt.ByteToothGwt;

public class ByteToothTimeLinePlotPanel extends CaptionPanel
{
	TimeLineChartPlotPanel timeLineChartPlot;
	int numPts = 300;
	int numTraces = -1;
	private boolean loaded = false;
	Date startPlotDate;
	ByteToothGwt[] valByteTooth = null;
	double[] yaxisData;
	double timeSec;
	double[] valMult;
	String plotWidth = "800px";
	String plotHeight = "550px";
	
	public boolean isLoaded() {return loaded;}
	
	public ByteToothTimeLinePlotPanel(int numPts, String title, String[] byteGearBoxTopic, String[] byteGearName, String[] valByteToothName, double[] valMult, String[] legend, EntryPointApp entryPointApp)
	{
		super(title);
		try
		{
			this.numPts = numPts;
			numTraces = byteGearBoxTopic.length;
			yaxisData = new double[numTraces];
			this.valMult = new double[numTraces];
			valByteTooth = new ByteToothGwt[numTraces];
			for (int ii = 0; ii < numTraces; ++ii)
			{
				ByteGearBoxData byteGearBoxData = entryPointApp.getByteGearBoxData(byteGearBoxTopic[ii]);
				ByteGearGwt byteGear = byteGearBoxData.getByteGearBoxGwt().getByteGear(byteGearName[ii]);
				valByteTooth[ii] = byteGear.getReadByteTooth(valByteToothName[ii]);
				this.valMult[ii] = valMult[ii];
			}
			loaded = false;
			timeLineChartPlot = new TimeLineChartPlotPanel(numPts, numTraces, title, "Time (sec)", "", legend, plotWidth, plotHeight);
			startPlotDate = new Date();
		} catch (Exception e) {GWT.log(e.getMessage());}
	}
	public void updateReadings()
	{
		if (!loaded) return;
		timeSec =  (double) ((new Date().getTime() - startPlotDate.getTime()) / 1000);
		for (int ii = 0; ii < numTraces; ++ii)
		{
			yaxisData[ii] = Double.parseDouble(valByteTooth[ii].getValue()) * valMult[ii];
		}
		timeLineChartPlot.draw(timeSec, yaxisData);
	}
	public void initialize()
	{
		timeLineChartPlot.initialize();
		add(timeLineChartPlot);
		LoadPlotTimer lpt = new LoadPlotTimer();
		lpt.scheduleRepeating(50);
	}
	private  class LoadPlotTimer extends Timer
	{
		@Override
		public void run() 
		{
			if (!timeLineChartPlot.isLoaded()) 
			{
				return;
			}
			else
			{
				loaded = true;
				this.cancel();
			}

		}
	}
}
