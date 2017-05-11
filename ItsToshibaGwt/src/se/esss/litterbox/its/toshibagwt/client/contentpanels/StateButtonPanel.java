package se.esss.litterbox.its.toshibagwt.client.contentpanels;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

import se.esss.litterbox.its.toshibagwt.client.EntryPointApp;
import se.esss.litterbox.its.toshibagwt.client.bytegearboxservice.ByteGearBoxData;
import se.esss.litterbox.its.toshibagwt.shared.bytegearboxgwt.ByteGearGwt;

public class StateButtonPanel extends CaptionPanel
{
	EntryPointApp entryPointApp;
	Grid stateButtonGrid = new Grid(7,3);
	CellFormatter stateButtonGridCellFormatter;
	StateButton[] stateButton;
	int setState = -1;
	int actState = -1;
	boolean interlock = false;
	ByteGearGwt cpuByteGear = null;
	ByteGearBoxData cpuByteGearBoxData = null;
	boolean settingsEnabled;
	Image interlockImage = null;


	public StateButtonPanel(EntryPointApp entryPointApp)
	{
		super("State");
		this.setCaptionHTML("<font size=\"+2\">State</font>");
		this.entryPointApp = entryPointApp;
		settingsEnabled = entryPointApp.getSetup().isSettingsPermitted();
		try 
		{
			cpuByteGearBoxData = entryPointApp.getByteGearBoxData("klyPlcProtoCpu");
			cpuByteGear = cpuByteGearBoxData.getByteGearBoxGwt().getByteGear("CPU_CONF");
		} catch (Exception e) {GWT.log(e.getMessage());}
		
		stateButtonGrid = new Grid(7,3);
		stateButtonGridCellFormatter = stateButtonGrid.getCellFormatter();
		stateButton = new StateButton[7];
		
		stateButton[0] 	= new StateButton("Reset", 			null, 					null, 					"RESET", 	"stateButtonReset",				-1, 0, this);
		stateButton[1] 	= new StateButton("Off", 			null, 					null, 					"OFF_CMD", 	"stateButtonOff",				0,  1, this);
		stateButton[2] 	= new StateButton("Aux", 			"AUX_MISS_PRECOND",		"AUX_MISS_SUPCOND",		"AUX_CMD", 	"stateButtonPreConFalse",		1,  2, this);
		stateButton[3] 	= new StateButton("Filament", 		"FIL_MISS_PRECOND",		"FIL_MISS_SUPCOND",		"FIL_CMD", 	"stateButtonPreConFalse",		2,  3, this);
		stateButton[4] 	= new StateButton("Standby", 		"STBY_MISS_PRECOND",	"STBY_MISS_SUPCOND",	"STBY_CMD",	"stateButtonPreConFalse",		3,  4, this);
		stateButton[5] 	= new StateButton("High Voltage", 	"HV_ENA_MISS_PRECOND",	"HV_ENA_MISS_SUPCOND",	"HV_CMD", 	"stateButtonPreConFalse",		4,  5, this);
		stateButton[6] 	= new StateButton("RF", 			"RF_ENA_MISS_PRECOND",	"RF_ENA_MISS_SUPCOND",	"RF_CMD", 	"stateButtonPreConFalse",		5,  6, this);
		
		
		add(stateButtonGrid);
		interlockImage = new Image("images/redlight.png");
		interlockImage.setSize("3.0em", "3.0em");
		stateButtonGrid.setWidget(0, 0, interlockImage);
		stateButtonGridCellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		UpdateReadingsTimer urt = new UpdateReadingsTimer(this);
		
		urt.scheduleRepeating(500);
	}
	static class StateButton extends Timer implements ClickHandler
	{
		Button button;
		StateButtonPanel stateButtonPanel;
		String preConDevice = null;
		String postConDevice = null;;
		String writeCommand = null;;
		int state = -10;
		int gridRow; 
		Image preConImage = null;
		Image postConImage = null;
		boolean preConFilled = false;
		boolean postConFilled = false;
		String initStyleName;
		boolean buttonPushed = false;

		StateButton(String title, String preConDevice, String postConDevice, String writeCommand, String initStyleName, int state, int gridRow,  StateButtonPanel stateButtonPanel)
		{
			this.stateButtonPanel = stateButtonPanel;
			this.preConDevice = preConDevice;
			this.postConDevice = postConDevice;
			this.writeCommand = writeCommand;
			this.state = state;
			this.gridRow = gridRow;
			this.initStyleName = initStyleName;
			button = new Button(title);
			button.setStyleName(initStyleName);
			button.setSize("8.0em", "2.0em");
			button.setEnabled(stateButtonPanel.settingsEnabled);
//			button.setEnabled(false);
			button.addClickHandler(this);
			stateButtonPanel.stateButtonGrid.setWidget(gridRow, 1, button);
			stateButtonPanel.stateButtonGridCellFormatter.setHorizontalAlignment(gridRow, 1, HasHorizontalAlignment.ALIGN_CENTER);
			if (preConDevice != null)
			{
				preConImage = new Image("images/redlight.png");
				preConImage.setSize("3.0em", "3.0em");
				stateButtonPanel.stateButtonGrid.setWidget(gridRow, 0, preConImage);
				stateButtonPanel.stateButtonGridCellFormatter.setHorizontalAlignment(gridRow, 0, HasHorizontalAlignment.ALIGN_CENTER);
			}
			if (postConDevice != null)
			{
				postConImage = new Image("images/redlight.png");
				postConImage.setSize("3.0em", "3.0em");
				stateButtonPanel.stateButtonGrid.setWidget(gridRow, 2, postConImage);
				stateButtonPanel.stateButtonGridCellFormatter.setHorizontalAlignment(gridRow, 0, HasHorizontalAlignment.ALIGN_CENTER);
			}
			
		}
		void updateReadings()
		{
			if (preConDevice != null)
			{
				try 
				{
					String sval = stateButtonPanel.cpuByteGear.getReadByteTooth(preConDevice).getValue();
					if (sval.equals("true"))
					{
						preConImage.setUrl("images/greenlight.png");
						preConFilled = true;
					}
					else
					{
						preConImage.setUrl("images/redlight.png");
						preConFilled = false;
					}
				} catch (Exception e) {GWT.log(e.getMessage());}
				if (!buttonPushed)
				{
					if (!preConFilled)
					{
						button.setStyleName("stateButtonPreConFalse");
						button.setEnabled(false);
					}
					else
					{
						button.setEnabled(true & stateButtonPanel.settingsEnabled);
						if (stateButtonPanel.actState >= state)
						{
							button.setStyleName("stateButtonActMatch");
						}
						else
						{
							if (stateButtonPanel.setState >= state)
							{
								button.setStyleName("stateButtonSetMatch");
							}
							else
							{
								button.setStyleName("stateButtonPreConTrue");
							}
						}
						
					}
				}
			}
			if (postConDevice != null)
			{
				try 
				{
					String sval = stateButtonPanel.cpuByteGear.getReadByteTooth(postConDevice).getValue();
					if (sval.equals("true"))
					{
						postConImage.setUrl("images/greenlight.png");
						postConFilled = true;
					}
					else
					{
						postConImage.setUrl("images/redlight.png");
						postConFilled = false;
					}
				} catch (Exception e) {GWT.log(e.getMessage());}
			}
			
		}
		@Override
		public void onClick(ClickEvent event) 
		{
			try 
			{
				button.setStyleName("stateButtonPressed");
				buttonPushed = true;
				this.schedule(1000);
				for (int ii = 0; ii < stateButtonPanel.stateButton.length; ++ii)
				{
					stateButtonPanel.cpuByteGear.getWriteByteTooth(stateButtonPanel.stateButton[ii].writeCommand).setValue("false");
				}
				stateButtonPanel.cpuByteGear.getWriteByteTooth(writeCommand).setValue("true");
				stateButtonPanel.cpuByteGear.getWriteByteTooth("WR_DATA").setValue("true");
				stateButtonPanel.cpuByteGearBoxData.setWriteData(stateButtonPanel.cpuByteGear);
			} catch (Exception e) {GWT.log(e.getMessage());}
		}
		@Override
		public void run() 
		{
			button.setStyleName(initStyleName);
			buttonPushed = false;
			this.cancel();
		}
	}
	static class UpdateReadingsTimer extends Timer
	{
		StateButtonPanel stateButtonPanel;
		UpdateReadingsTimer(StateButtonPanel stateButtonPanel)
		{
			this.stateButtonPanel = stateButtonPanel;
		}
		@Override
		public void run() 
		{
			try 
			{
				stateButtonPanel.setState = Integer.parseInt(stateButtonPanel.cpuByteGear.getReadByteTooth("SET_STATE").getValue());
				stateButtonPanel.actState = Integer.parseInt(stateButtonPanel.cpuByteGear.getReadByteTooth("ACT_STATE").getValue());
				stateButtonPanel.interlock = Boolean.parseBoolean(stateButtonPanel.cpuByteGear.getReadByteTooth("INTERLOCK").getValue());
				if (stateButtonPanel.interlock)
				{
					stateButtonPanel.interlockImage.setUrl("images/redlight.png");
				}
				else
				{
					stateButtonPanel.interlockImage.setUrl("images/greenlight.png");
				}
			} catch (Exception e) {GWT.log(e.getMessage());}
			for (int ii = 0; ii < stateButtonPanel.stateButton.length; ++ii)
			{
				stateButtonPanel.stateButton[ii].updateReadings();
			}
			
		}
	}
}
