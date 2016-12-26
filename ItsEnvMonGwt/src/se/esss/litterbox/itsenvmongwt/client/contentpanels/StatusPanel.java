package se.esss.litterbox.itsenvmongwt.client.contentpanels;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import se.esss.litterbox.itsenvmongwt.client.MqttServiceAsync;
import se.esss.litterbox.itsenvmongwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.itsenvmongwt.client.gskel.GskelVerticalPanel;

public class StatusPanel extends GskelVerticalPanel
{
	private MqttServiceAsync mqttService;
	private String styleName = "ItsEnvStatusPanel";
	private ReadingsCaptionPanel readingsCaptionPanel;
	private TimePlotCaptionPanel timePlotCaptionPanel;
	private GaugeCaptionPanel gaugeCaptionPanel;
	private SettingsCaptionPanel settingsCaptionPanel;
	private boolean loaded = false;
	private int timePlotMultiplier = 1;
	private boolean settingsPermitted;

	private int timePlotCounter = 0;
	
	public boolean isLoaded() {return loaded;}
	public boolean isSettingsPermitted() {return settingsPermitted;}
	public MqttServiceAsync getMqttService() {return mqttService;}
	public int getTimePlotMultiplier() {return timePlotMultiplier;}

	public void setTimePlotMultiplier(int timePlotMultiplier) {this.timePlotMultiplier = timePlotMultiplier;}

	public StatusPanel(String tabTitle, GskelSetupApp setupApp, MqttServiceAsync mqttService, boolean settingsPermitted) 
	{
		super(tabTitle, tabTitle, setupApp);
		loaded = false;
		this.mqttService = mqttService;
		this.settingsPermitted = settingsPermitted;
		this.getGskelTabLayoutScrollPanel().setStyleName(styleName);
		readingsCaptionPanel = new ReadingsCaptionPanel();
		settingsCaptionPanel = new SettingsCaptionPanel(this);
		VerticalPanel vp1 = new VerticalPanel();
		vp1.add(readingsCaptionPanel);
		vp1.add(settingsCaptionPanel);
		timePlotCaptionPanel = new TimePlotCaptionPanel(300, "600px", "400px");
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.add(vp1);
		hp1.add(timePlotCaptionPanel);
		add(hp1);
		gaugeCaptionPanel = null;
		LoadPlotTimer lpt = new LoadPlotTimer(this);
		lpt.scheduleRepeating(20);
	}
	private void updateReadings(String[][] readingfromServer)
	{
		readingsCaptionPanel.updateReadings(readingfromServer);
		gaugeCaptionPanel.updateReadings(readingfromServer);
		if (timePlotCounter >= timePlotMultiplier)
		{
			timePlotCaptionPanel.updateReadings(readingfromServer);
			timePlotCounter = 0;
		}
		++timePlotCounter;

	}
	@Override
	public void tabLayoutPanelInterfaceAction(String message) {}
	@Override
	public void optionDialogInterfaceAction(String choiceButtonText) {}
	private static class LoadPlotTimer extends Timer
	{
		StatusPanel statusPanel;
		LoadPlotTimer(StatusPanel statusPanel)
		{
			this.statusPanel = statusPanel;
		}
		@Override
		public void run() 
		{
			if (!statusPanel.timePlotCaptionPanel.isLoaded()) 
			{
				return;
			}
			else
			{
				if (statusPanel.gaugeCaptionPanel != null)
				{
					if (!statusPanel.gaugeCaptionPanel.isLoaded()) return;
				}
				else
				{
					statusPanel.gaugeCaptionPanel = new GaugeCaptionPanel();
					statusPanel.add(statusPanel.gaugeCaptionPanel);
					return;
				}
				statusPanel.loaded = true;
				FetchReadingsTimer frt = new FetchReadingsTimer(statusPanel);
				frt.scheduleRepeating(1000);
				this.cancel();
			}

		}
	}
	private static class FetchReadingsTimer extends Timer
	{
		StatusPanel statusPanel;
		public FetchReadingsTimer(StatusPanel statusPanel) {this.statusPanel = statusPanel;}
		@Override
		public void run() 
		{
			if (!statusPanel.timePlotCaptionPanel.isLoaded()) return;
			if (!statusPanel.gaugeCaptionPanel.isLoaded()) return;
			String[][] debugResponse = {{"key1", "val1"}, {"key2", "val2"}};
			statusPanel.mqttService.getNameValuePairArray(statusPanel.isDebug(), debugResponse, new GetNameValuePairArrayAsyncCallback(statusPanel));
		}
	}

	private static class GetNameValuePairArrayAsyncCallback implements AsyncCallback<String[][]>
	{
		StatusPanel statusPanel;
		GetNameValuePairArrayAsyncCallback(StatusPanel statusPanel)
		{
			this.statusPanel = statusPanel;
		}
		@Override
		public void onFailure(Throwable caught) 
		{
			statusPanel.getStatusTextArea().addStatus("Error on GetNameValuePairArrayAsyncCallback: " +  caught.getMessage());
			
		}
		@Override
		public void onSuccess(String[][] result) 
		{
			statusPanel.updateReadings(result);
		}
	}
}
