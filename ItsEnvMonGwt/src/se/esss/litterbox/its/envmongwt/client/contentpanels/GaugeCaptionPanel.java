package se.esss.litterbox.its.envmongwt.client.contentpanels;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class GaugeCaptionPanel extends CaptionPanel
{
	private GaugePlotPanel[] gaugePlotPanel = new GaugePlotPanel[4];
	private HorizontalPanel gaugeHorizontalPanel;
	private boolean loaded = false;
	
	public boolean isLoaded() {return loaded;}
	
	public GaugeCaptionPanel()
	{
		super("Dashboard");
		loaded = false;
		
		gaugePlotPanel[0] = new GaugePlotPanel();
		gaugePlotPanel[0].setTitle("cpm");
		gaugePlotPanel[0].setMinMax(5, 30);
		gaugePlotPanel[0].setYellowMinMax(20,25);
		gaugePlotPanel[0].setGreenMinMax(5,20);
		gaugePlotPanel[0].setRedMinMax(25,30);
		
		gaugePlotPanel[1] = new GaugePlotPanel();
		gaugePlotPanel[1].setTitle("temp");
		gaugePlotPanel[1].setMinMax(5, 30);
		gaugePlotPanel[1].setYellowMinMax(5,14);
		gaugePlotPanel[1].setGreenMinMax(14,23);
		gaugePlotPanel[1].setRedMinMax(23,30);
		
		gaugePlotPanel[2] = new GaugePlotPanel();
		gaugePlotPanel[2].setTitle("photo avg");
		gaugePlotPanel[2].setMinMax(0, 1000);
		gaugePlotPanel[2].setYellowMinMax(0,300);
		gaugePlotPanel[2].setGreenMinMax(300,650);
		gaugePlotPanel[2].setRedMinMax(650,1000);

		gaugePlotPanel[3] = new GaugePlotPanel();
		gaugePlotPanel[3].setTitle("photo");
		gaugePlotPanel[3].setMinMax(0, 1000);
		gaugePlotPanel[3].setYellowMinMax(0,300);
		gaugePlotPanel[3].setGreenMinMax(300,650);
		gaugePlotPanel[3].setRedMinMax(650,1000);
		
		gaugeHorizontalPanel = new HorizontalPanel();
		setWidth("40.0em");
		add(gaugeHorizontalPanel);
		
		LoadPlotTimer lpt = new LoadPlotTimer(this);
		lpt.scheduleRepeating(50);
	}
	public void updateReadings(String[][] readingfromServer)
	{
		if (!loaded) return;
		for (int ii = 0; ii < readingfromServer.length; ++ii)
		{
			gaugePlotPanel[ii].setValue(Double.parseDouble(readingfromServer[ii][1]));
			gaugePlotPanel[ii].draw();
		}
	}
	private static class LoadPlotTimer extends Timer
	{
		GaugeCaptionPanel gaugeCaptionPanel;
		int iguageLoad = 0;
		LoadPlotTimer(GaugeCaptionPanel gaugeCaptionPanel)
		{
			this.gaugeCaptionPanel = gaugeCaptionPanel;
			gaugeCaptionPanel.loaded = false;
			iguageLoad = 0;
			gaugeCaptionPanel.gaugePlotPanel[iguageLoad].initialize();
			gaugeCaptionPanel.gaugeHorizontalPanel.add(gaugeCaptionPanel.gaugePlotPanel[iguageLoad]);
			++iguageLoad;
		}
		@Override
		public void run() 
		{
			if (!gaugeCaptionPanel.gaugePlotPanel[iguageLoad - 1].isLoaded()) return;
			if (iguageLoad < gaugeCaptionPanel.gaugePlotPanel.length)
			{
				gaugeCaptionPanel.gaugePlotPanel[iguageLoad].initialize();
				gaugeCaptionPanel.gaugeHorizontalPanel.add(gaugeCaptionPanel.gaugePlotPanel[iguageLoad]);
				++iguageLoad;
			}
			else
			{
				gaugeCaptionPanel.loaded = true;
				this.cancel();
			}

		}
	}

}
