package se.esss.litterbox.its.toshibagwt.client.contentpanels;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

import se.esss.litterbox.its.toshibagwt.client.EntryPointApp;
import se.esss.litterbox.its.toshibagwt.client.bytegearboxservice.ByteGearBoxData;
import se.esss.litterbox.its.toshibagwt.shared.bytegearboxgwt.ByteGearGwt;

public class OpModePanel extends CaptionPanel implements ClickHandler
{
	EntryPointApp entryPointApp;
	ByteGearGwt cpuByteGear = null;
	ByteGearBoxData cpuByteGearBoxData = null;
	boolean settingsEnabled;
	RadioButton[] opModeRadioButton;

	public OpModePanel(EntryPointApp entryPointApp)
	{
		super("Op Mode");
		this.setCaptionHTML("<font size=\"+2\">Op Mode</font>");
		this.entryPointApp = entryPointApp;
		settingsEnabled = entryPointApp.getSetup().isSettingsPermitted();
		try 
		{
			cpuByteGearBoxData = entryPointApp.getByteGearBoxData("klyPlcProtoCpu");
			cpuByteGear = cpuByteGearBoxData.getByteGearBoxGwt().getByteGear("CPU_CONF");
		} catch (Exception e) {GWT.log(e.getMessage());}

		opModeRadioButton = new RadioButton[4];
		opModeRadioButton[0] = new RadioButton("opModeRadioGroup", "Bypass");
		opModeRadioButton[1] = new RadioButton("opModeRadioGroup", "Test");
		opModeRadioButton[2] = new RadioButton("opModeRadioGroup", "Normal");
		opModeRadioButton[3] = new RadioButton("opModeRadioGroup", "Wire");
		opModeRadioButton[0].setHTML("<font size=\"+2\">Bypass</font>");
		opModeRadioButton[1].setHTML("<font size=\"+2\">Test</font>");
		opModeRadioButton[2].setHTML("<font size=\"+2\">Normal</font>");
		opModeRadioButton[3].setHTML("<font size=\"+2\">Wire</font>");

		opModeRadioButton[2].setValue(true);
		VerticalPanel opModeVertPanel = new VerticalPanel();
		opModeVertPanel.setWidth("100%");
		for (int ii = 0; ii < 4; ++ii)
		{
			opModeVertPanel.add(opModeRadioButton[ii]);
			opModeRadioButton[0].addClickHandler(this);
		}
		add(opModeVertPanel);
	}

	@Override
	public void onClick(ClickEvent event) 
	{
		for (int ii = 0; ii < 4; ++ii)
		{
			opModeRadioButton[0].addClickHandler(this);
		}
	}
}
