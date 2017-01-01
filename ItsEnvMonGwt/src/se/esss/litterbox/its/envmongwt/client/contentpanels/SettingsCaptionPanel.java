package se.esss.litterbox.its.envmongwt.client.contentpanels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.TextBox;

public class SettingsCaptionPanel extends CaptionPanel
{
	private Button cpmAvgButton;
	private Button plotIntvlButton;
	private TextBox cpmAvgSetTextBox;
	private TextBox plotIntTextBox;
	StatusPanel statusPanel;
	public SettingsCaptionPanel(StatusPanel statusPanel)
	{
		super("Settings");
		this.statusPanel = statusPanel;
		Grid setGrid = new Grid(2, 2);
		setGrid.setWidth("100%");
		HTMLTable.CellFormatter formatter = setGrid.getCellFormatter();
		for (int ii = 0; ii < 2; ++ii)
		{
			formatter.setHorizontalAlignment(ii, 0, HasHorizontalAlignment.ALIGN_LEFT);
			formatter.setVerticalAlignment(ii, 0, HasVerticalAlignment.ALIGN_MIDDLE);
			formatter.setHorizontalAlignment(ii, 1, HasHorizontalAlignment.ALIGN_RIGHT);
			formatter.setVerticalAlignment(ii, 1, HasVerticalAlignment.ALIGN_MIDDLE);
		}
		cpmAvgButton = new Button("cpm Avg");
		cpmAvgButton.addClickHandler(new ButtonClickHandler("cpm Avg", this));
		cpmAvgButton.setEnabled(statusPanel.isSettingsPermitted());
		plotIntvlButton = new Button("plot intvl");
		plotIntvlButton.addClickHandler(new ButtonClickHandler("plot intvl", this));
		cpmAvgSetTextBox = new TextBox();
		plotIntTextBox = new TextBox();
		plotIntTextBox.setText(Integer.toString(statusPanel.getTimePlotMultiplier()));
		cpmAvgButton.setWidth("6.0em");
		plotIntvlButton.setWidth("6.0em");
		cpmAvgSetTextBox.setWidth("6.0em");
		plotIntTextBox.setWidth("6.0em");
		setGrid.setWidget(0, 0, cpmAvgButton);
		setGrid.setWidget(1, 0, plotIntvlButton);
		setGrid.setWidget(0, 1, cpmAvgSetTextBox);
		setGrid.setWidget(1, 1, plotIntTextBox);
		add(setGrid);

	}
	private static class ButtonClickHandler  implements ClickHandler
	{
		private SettingsCaptionPanel settingsCaptionPanel;
		private String buttonText = null;
		
		public ButtonClickHandler(String buttonText, SettingsCaptionPanel settingsCaptionPanel)
		{
			this.settingsCaptionPanel = settingsCaptionPanel;
			this.buttonText = buttonText;
		}
		
		@Override
		public void onClick(ClickEvent event) 
		{
			
			if (buttonText.equals("cpm Avg"))
			{
				if (!settingsCaptionPanel.statusPanel.isSettingsPermitted())
				{
					settingsCaptionPanel.cpmAvgButton.setEnabled(false);
					settingsCaptionPanel.statusPanel.getStatusTextArea().addStatus("Knock it off Inigo!");
					return;
				}
				try
				{
					int cpmAvg = Integer.parseInt(settingsCaptionPanel.cpmAvgSetTextBox.getText());
					String[] topicValue = new String[3];
					topicValue[0] = "itsGeiger01/set/avg";
					topicValue[1] = "avgSet";
					topicValue[2] = Integer.toString(cpmAvg);
					String[] debugResponse = topicValue;
					settingsCaptionPanel.statusPanel.getMqttService().setNameValuePairArray(
							topicValue, 
							settingsCaptionPanel.statusPanel.isDebug(), 
							debugResponse, 
							new SetNameValuePairArrayAsyncCallback(settingsCaptionPanel.statusPanel));
				}
				catch (NumberFormatException nfe) {}
				settingsCaptionPanel.cpmAvgSetTextBox.setText("");
			}
			if (buttonText.equals("plot intvl"))
			{
				try
				{
					int pltIntvl = Integer.parseInt(settingsCaptionPanel.plotIntTextBox.getText());
					if (pltIntvl > 0) settingsCaptionPanel.statusPanel.setTimePlotMultiplier(pltIntvl);
				}
				catch (NumberFormatException nfe) {}
			}
			settingsCaptionPanel.plotIntTextBox.setText(Integer.toString(settingsCaptionPanel.statusPanel.getTimePlotMultiplier()));
		}

	}
	private static class SetNameValuePairArrayAsyncCallback implements AsyncCallback<String[]>
	{
		StatusPanel statusPanel;
		SetNameValuePairArrayAsyncCallback(StatusPanel statusPanel)
		{
			this.statusPanel = statusPanel;
		}
		@Override
		public void onFailure(Throwable caught) 
		{
			statusPanel.getMessageDialog().setMessage("Error on Call back", caught.getMessage(), true);
			
		}
		@Override
		public void onSuccess(String[] result) 
		{
			statusPanel.getStatusTextArea().addStatus("Success in setting: " + result[0] + " " + result[2]);
		}
	}
}
