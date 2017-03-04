package se.esss.litterbox.its.dashboardgwt.client.googleplots;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.CaptionPanel;

import se.esss.litterbox.its.dashboardgwt.client.EntryPointApp;

public class ThermCaptionPanel extends CaptionPanel
{
	private ThermPlotPanel thermPlotPanel;
	private boolean loaded = false;
	EntryPointApp entryPointApp;
	String captionTitle;
	
	public boolean isLoaded() {return loaded;}
	public ThermPlotPanel getThermPlotPanel() {return thermPlotPanel;}
	
	public ThermCaptionPanel(String captionTitle, EntryPointApp entryPointApp)
	{
		super(captionTitle);
		loaded = false;
		thermPlotPanel = new ThermPlotPanel(entryPointApp);
		this.entryPointApp = entryPointApp;
		this.captionTitle = captionTitle;
	}
	public void initialize()
	{
		thermPlotPanel.initialize();
		add(thermPlotPanel);
		LoadPlotTimer lpt = new LoadPlotTimer();
		lpt.scheduleRepeating(50);
	}
	public void updateReadings(double value)
	{
		if (!loaded) return;
		thermPlotPanel.setValue(value);
		thermPlotPanel.draw();
		thermPlotPanel.setChartVisible(true);
	}
	private  class LoadPlotTimer extends Timer
	{
		@Override
		public void run() 
		{
			if (!thermPlotPanel.isLoaded()) 
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
