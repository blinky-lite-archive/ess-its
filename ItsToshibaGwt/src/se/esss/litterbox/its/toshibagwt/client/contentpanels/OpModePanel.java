package se.esss.litterbox.its.toshibagwt.client.contentpanels;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
	int iopmode = 3;
	boolean updateReadings = true;
	Button expertSettingsButton = new Button("Show Expert Settings");
	boolean showExpertSettings = false;
	

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

		opModeRadioButton[iopmode - 1].setValue(true);
		VerticalPanel opModeVertPanel = new VerticalPanel();
		opModeVertPanel.setWidth("100%");
		for (int ii = 0; ii < 4; ++ii)
		{
			opModeVertPanel.add(opModeRadioButton[ii]);
			opModeRadioButton[ii].addClickHandler(this);
			opModeRadioButton[ii].setEnabled(settingsEnabled);
		}
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.setWidth("100%");
		hp1.add(opModeVertPanel);
		hp1.add(expertSettingsButton);
		expertSettingsButton.addClickHandler(new ShowExperTabsButtonClickHandler(this));
		hp1.setCellVerticalAlignment(expertSettingsButton, HasVerticalAlignment.ALIGN_MIDDLE);
		hp1.setCellHorizontalAlignment(expertSettingsButton, HasHorizontalAlignment.ALIGN_RIGHT);
		add(hp1);
		UpdateReadingsTimer urt = new UpdateReadingsTimer(this);
		urt.scheduleRepeating(500);
	}

	@Override
	public void onClick(ClickEvent event) 
	{
		int iopmodeSelected = -1;
		updateReadings = false;
		for (int ii = 0; ii < 4; ++ii)
		{
			if (opModeRadioButton[ii].getValue()) iopmodeSelected = ii + 1;
		}
		try 
		{
			cpuByteGear.getWriteByteTooth("OP_MODE").setValue(Integer.toString(iopmodeSelected));
			cpuByteGear.getWriteByteTooth("WR_DATA").setValue("true");
			cpuByteGearBoxData.setWriteData(cpuByteGear);
		} catch (Exception e) {GWT.log(e.getMessage());}
		updateReadings = true;
	}
	static class UpdateReadingsTimer extends Timer
	{
		OpModePanel opModePanel;
		UpdateReadingsTimer(OpModePanel opModePanel)
		{
			this.opModePanel = opModePanel;
		}
		@Override
		public void run() 
		{
			if (opModePanel.updateReadings)
			{
				try 
				{
					opModePanel.iopmode = Integer.parseInt(opModePanel.cpuByteGear.getReadByteTooth("OP_MODE").getValue());
					opModePanel.opModeRadioButton[opModePanel.iopmode - 1].setValue(true);
					for (int ii = 0; ii < 4; ++ii)
					{
						if (opModePanel.opModeRadioButton[ii].getValue()) 
						{
							opModePanel.opModeRadioButton[ii].setStyleName("opModeSelected");
						}
						else
						{
							opModePanel.opModeRadioButton[ii].setStyleName("opModeUnselected");
						}
					}
				} catch (Exception e) {GWT.log(e.getMessage());}
			}
			
		}
	}
	static class ShowExperTabsButtonClickHandler implements ClickHandler
	{
		OpModePanel opModePanel;
		ShowExperTabsButtonClickHandler(OpModePanel opModePanel)
		{
			this.opModePanel = opModePanel;
		}
		@Override
		public void onClick(ClickEvent event) 
		{
			if (!opModePanel.showExpertSettings)
			{
				opModePanel.expertSettingsButton.setText("Hide Expert Settings");
				opModePanel.showExpertSettings = true;
			}
			else
			{
				opModePanel.expertSettingsButton.setText("Show Expert Settings");
				opModePanel.showExpertSettings = false;
			}
			for (int ii = 0; ii < opModePanel.entryPointApp.getByteGearBoxData().length; ++ii)
			{
				opModePanel.entryPointApp.getSetup().getGskelTabLayoutPanel().getTabWidget(ii + 3).getParent().setVisible(opModePanel.showExpertSettings);
			}
			
		}
		
	}

}
