package se.esss.litterbox.itsllrfgwt.client.contentpanels;

import se.esss.litterbox.itsllrfgwt.client.callbacks.GetLlrfStateAsyncCallback;
import se.esss.litterbox.itsllrfgwt.client.callbacks.PutLlrfSettingsAsyncCallback;
import se.esss.litterbox.itsllrfgwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.itsllrfgwt.client.gskel.GskelVerticalPanel;
import se.esss.litterbox.itsllrfgwt.client.handlers.LlrfButtonClickHandler;
import se.esss.litterbox.itsllrfgwt.shared.LlrfData;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


public class LLrfSetupVerticalPanel extends GskelVerticalPanel
{
	boolean superCreated = false;
	LlrfData llrfData;
	private boolean gettingLlrfState = false;
	private boolean puttingSettingsState = false;
	private boolean settingsPermitted = false;
	private String styleName = "llrfSetupPanel";

	public boolean isGettingLlrfState() {return gettingLlrfState;}
	public boolean isSuperCreated() {return superCreated;}
	public boolean isPuttingSettingsState() {return puttingSettingsState;}
	public boolean isSettingsPermitted() {return settingsPermitted;}
	public String getStyleName() {return styleName;}
	public LlrfData getLlrfData() {return llrfData;}

	public void setGettingLlrfState(boolean gettingLlrfState) {this.gettingLlrfState = gettingLlrfState;}
	public void setSuperCreated(boolean superCreated) {this.superCreated = superCreated;}
	public void setPuttingSettingsState(boolean puttingSettingsState) {this.puttingSettingsState = puttingSettingsState;}
	public void setSettingsPermitted(boolean settingsPermitted) {this.settingsPermitted = settingsPermitted;}
	public void setLlrfData(LlrfData llrfData) {this.llrfData = llrfData;}

	
	private TextBox rfFreqTextBox = new TextBox();
	private TextBox rfPowLvlTextBox = new TextBox();
	private TextBox rfPulseWidthTextBox = new TextBox();
	private CheckBox rfPowOnCheckBox = new CheckBox();
	private CheckBox rfPulseOnCheckBox = new CheckBox();
	private TextBox modRiseTimeTextBox = new TextBox();
	private TextBox modRepRateTextBox = new TextBox();
	private CheckBox modPulseOnCheckBox = new CheckBox();
	private Label rfPowerReading1 = new Label();
	private Label rfPowerReading2 = new Label();

	public LLrfSetupVerticalPanel(String tabTitle, GskelSetupApp setupApp, boolean settingsPermitted) 
	{
		super(tabTitle, tabTitle, setupApp);
		this.getGskelTabLayoutScrollPanel().setStyleName(styleName);
		this.settingsPermitted = settingsPermitted;
		superCreated = true;
		llrfData = new LlrfData();
		add(settingsCaptionPanel());
		getLlrfState();
		Window.addResizeHandler(new LlrfSetupVerticalPanelResizeHandler());
	}

	@Override
	public void tabLayoutPanelInterfaceAction(String message) 
	{
		if (!superCreated) return;
		getLlrfState();
//		getStatusTextArea().addStatus("Tab " + this.getTabValue() + " " + message);
	}
	@Override
	public void optionDialogInterfaceAction(String choiceButtonText) 
	{
	}
	public CaptionPanel settingsCaptionPanel()
	{
		Grid settingGrid = new Grid(11, 3);
		HTMLTable.CellFormatter formatter = settingGrid.getCellFormatter();
		formatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		formatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);	
		
		String textBoxWidth = "6.0em";
		
		rfFreqTextBox.setSize(textBoxWidth, "1.0em");
		rfPowLvlTextBox.setSize(textBoxWidth, "1.0em");
		rfPulseWidthTextBox.setSize(textBoxWidth, "1.0em");
		modRiseTimeTextBox.setSize(textBoxWidth, "1.0em");
		modRepRateTextBox.setSize(textBoxWidth, "1.0em");
		rfPowerReading1.setSize(textBoxWidth, "1.0em");
		rfPowerReading2.setSize(textBoxWidth, "1.0em");

		for (int irow = 0; irow < 11; ++irow)
		{
			formatter.setHorizontalAlignment(irow, 1, HasHorizontalAlignment.ALIGN_CENTER);
			formatter.setVerticalAlignment(irow, 1, HasVerticalAlignment.ALIGN_MIDDLE);
		}
		
		settingGrid.setWidget(0, 0, new Label("Name"));
		settingGrid.setWidget(0, 1, new Label("Value"));
		settingGrid.setWidget(0, 2, new Label("Unit"));
		
		settingGrid.setWidget(1, 0, new Label("RF Frequency"));
		settingGrid.setWidget(1, 1, rfFreqTextBox);
		settingGrid.setWidget(1, 2, new Label("MHz"));
		
		settingGrid.setWidget(2, 0, new Label("RF Power Level"));
		settingGrid.setWidget(2, 1, rfPowLvlTextBox);
		settingGrid.setWidget(2, 2, new Label("dBm"));
		
		settingGrid.setWidget(3, 0, new Label("RF Pulse Width"));
		settingGrid.setWidget(3, 1, rfPulseWidthTextBox);
		settingGrid.setWidget(3, 2, new Label("mSec"));
		
		settingGrid.setWidget(4, 0, new Label("RF Power On"));
		settingGrid.setWidget(4, 1, rfPowOnCheckBox);
		settingGrid.setWidget(4, 2, new Label(""));
		
		settingGrid.setWidget(5, 0, new Label("RF Pulse On"));
		settingGrid.setWidget(5, 1, rfPulseOnCheckBox);
		settingGrid.setWidget(5, 2, new Label(""));
		
		settingGrid.setWidget(6, 0, new Label("Mod. Rise Time"));
		settingGrid.setWidget(6, 1, modRiseTimeTextBox);
		settingGrid.setWidget(6, 2, new Label("mSec"));
		
		settingGrid.setWidget(7, 0, new Label("Mod Rep. Rate"));
		settingGrid.setWidget(7, 1, modRepRateTextBox);
		settingGrid.setWidget(7, 2, new Label("Hz"));
		
		settingGrid.setWidget(8, 0, new Label("Mod Pulse On"));
		settingGrid.setWidget(8, 1, modPulseOnCheckBox);
		settingGrid.setWidget(8, 2, new Label(""));
		
		settingGrid.setWidget(9, 0, new Label("RF Power Reading 1"));
		settingGrid.setWidget(9, 1, rfPowerReading1);
		settingGrid.setWidget(9, 2, new Label("dBm"));
		
		settingGrid.setWidget(10, 0, new Label("RF Power Reading 2"));
		settingGrid.setWidget(10, 1, rfPowerReading2);
		settingGrid.setWidget(10, 2, new Label("dBm"));

		Button setLlrfSettingsButton = new Button("Set");
		setLlrfSettingsButton.addClickHandler(new LlrfButtonClickHandler("Set", this));
		setLlrfSettingsButton.setWidth("6.0em");
		setLlrfSettingsButton.setEnabled(settingsPermitted);

		Button initLlrfSettingsButton = new Button("Init");
		initLlrfSettingsButton.addClickHandler(new LlrfButtonClickHandler("Init", this));
		initLlrfSettingsButton.setWidth("6.0em");
		initLlrfSettingsButton.setEnabled(settingsPermitted);

		Grid buttonGrid = new Grid(1, 2);
		buttonGrid.setWidth("100%");
		buttonGrid.setWidget(0, 0, setLlrfSettingsButton);
		buttonGrid.setWidget(0, 1, initLlrfSettingsButton);
		HTMLTable.CellFormatter formatter2 = buttonGrid.getCellFormatter();
		formatter2.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
		formatter2.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);

		CaptionPanel actionsCaptionPanel = new CaptionPanel("Actions");
		actionsCaptionPanel.add(buttonGrid);
		CaptionPanel settingsCaptionPanel = new CaptionPanel("Settings");
		settingsCaptionPanel.add(settingGrid);
		VerticalPanel vp1 = new VerticalPanel();
		vp1.add(actionsCaptionPanel);
		vp1.add(settingsCaptionPanel);
		CaptionPanel actionsSettingsCaptionPanel = new CaptionPanel("LLRF Settings");
		actionsSettingsCaptionPanel.add(vp1);
		actionsSettingsCaptionPanel.setWidth("23em");
		return actionsSettingsCaptionPanel;
		
	}
	public void updateSettingDisplay()
	{
		rfFreqTextBox.setText(Double.toString(llrfData.getRfFreq()));
		rfPowLvlTextBox.setText(Double.toString(llrfData.getRfPowLvl()));
		rfPulseWidthTextBox.setText(Double.toString(llrfData.getRfPulseWidth()));
		modRiseTimeTextBox.setText(Double.toString(llrfData.getModRiseTime()));
		modRepRateTextBox.setText(Double.toString(llrfData.getModRepRate()));
		rfPowerReading1.setText(Double.toString(llrfData.getRfPowRead1()));
		rfPowerReading2.setText(Double.toString(llrfData.getRfPowRead2()));
		rfPowOnCheckBox.setValue(llrfData.isRfPowOn());
		rfPulseOnCheckBox.setValue(llrfData.isRfPulseOn());
		modPulseOnCheckBox.setValue(llrfData.isModPulseOn());
	}
	public void getSettingsFromDisplay()
	{
		try
		{
			double data = Double.parseDouble(rfFreqTextBox.getText());
			llrfData.setRfFreq(data);
		} catch(NumberFormatException nfe) {getStatusTextArea().addStatus("Bad setting for Rf Frequency");}
		try
		{
			double data = Double.parseDouble(rfPowLvlTextBox.getText());
			llrfData.setRfPowLvl(data);;
		} catch(NumberFormatException nfe) {getStatusTextArea().addStatus("Bad setting for Rf Power Level");}
		try
		{
			double data = Double.parseDouble(rfPulseWidthTextBox.getText());
			llrfData.setRfPulseWidth(data);;
		} catch(NumberFormatException nfe) {getStatusTextArea().addStatus("Bad setting for Rf Pulse Width");}
		try
		{
			double data = Double.parseDouble(modRiseTimeTextBox.getText());
			llrfData.setModRiseTime(data);;
		} catch(NumberFormatException nfe) {getStatusTextArea().addStatus("Bad setting for Modulator Rise Time");}
		try
		{
			double data = Double.parseDouble(modRepRateTextBox.getText());
			llrfData.setModRepRate(data);;
		} catch(NumberFormatException nfe) {getStatusTextArea().addStatus("Bad setting for Modulator Rep rate");}
		llrfData.setRfPowOn(rfPowOnCheckBox.getValue());
		llrfData.setRfPulseOn(rfPulseOnCheckBox.getValue());
		llrfData.setModPulseOn(modPulseOnCheckBox.getValue());
	}
	public void getLlrfState()
	{
		if (gettingLlrfState) return;
		getSetupApp().getMessageDialog().setImageUrl("images/wait.png");
		this.getSetupApp().getMessageDialog().setMessage("Meddelande", "Vänta - Hämtar inställningar", false);
		gettingLlrfState = true;
		getStatusTextArea().addStatus("Getting last known LLRF state");
		getEntryPointAppService().getLlrfState(getSetupApp().isDebug(), new GetLlrfStateAsyncCallback(this));
	}
	public void putSettings(boolean initSettings)
	{
		if (puttingSettingsState) return;
		getSetupApp().getMessageDialog().setImageUrl("images/wait.png");
		getSetupApp().getMessageDialog().setMessage("Meddelande", "Vänta - Ställer inställningar", false);
		puttingSettingsState = true;
		getSettingsFromDisplay();
		getStatusTextArea().addStatus("Changing LLRF settings");
		getEntryPointAppService().putLlrfSettings(llrfData, initSettings, getSetupApp().isDebug(), new PutLlrfSettingsAsyncCallback(this));
	}
	public class LlrfSetupVerticalPanelResizeHandler implements ResizeHandler
	{
		@Override
		public void onResize(ResizeEvent event) 
		{
//			getStatusTextArea().addStatus("Got a resize");
			getLlrfState();
		}
	}
}
