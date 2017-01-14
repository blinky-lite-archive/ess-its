package se.esss.litterbox.its.cernrfgwt.client.contentpanels;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import se.esss.litterbox.its.cernrfgwt.client.EntryPointApp;
import se.esss.litterbox.its.cernrfgwt.client.gskel.GskelSettingButtonGrid;
import se.esss.litterbox.its.cernrfgwt.client.mqttdata.MqttData;

public class IceCubeTimerPanel extends VerticalPanel 
{
	private CheckBox[][] eventBit = new CheckBox[4][8];
	private TextBox[] startTime = new TextBox[4];
	private TextBox[] stopTime = new TextBox[4];
	boolean settingsPermitted = false;
	EntryPointApp entryPointApp;
	String iceCubeTimerName;
	String mqtttopic;
	IceCubeTimerMqttData iceCubeTimerMqttData;
	SettingButtonGrid settingButtonGrid;
	
	public IceCubeTimerPanel(String iceCubeTimerName, String mqtttopic, String[] channelNickName, boolean settingsPermitted, EntryPointApp entryPointApp)
	{
		this.settingsPermitted = settingsPermitted;
		this.entryPointApp = entryPointApp;
		this.iceCubeTimerName = iceCubeTimerName;
		this.mqtttopic = mqtttopic;
		Grid channelGrid = new Grid(11, 5);
		HTMLTable.CellFormatter formatter = channelGrid.getCellFormatter();
		channelGrid.setWidget(1,0, new Label("Start (uS)"));
		channelGrid.setWidget(2,0, new Label("Stop  (uS)"));
		for (int ii = 0; ii < 8; ++ii)
		{
			channelGrid.setWidget(ii + 3, 0, new Label("B" + Integer.toString(ii)));
			formatter.setHorizontalAlignment(ii + 3, 0, HasHorizontalAlignment.ALIGN_CENTER);
			formatter.setVerticalAlignment(ii + 3, 0, HasVerticalAlignment.ALIGN_MIDDLE);	
		}
		for (int ii = 0; ii < 4; ++ii)
		{
			startTime[ii] = new TextBox();
			stopTime[ii] = new TextBox();
			startTime[ii].setWidth("3.0em");
			stopTime[ii].setWidth("3.0em");
			channelGrid.setWidget(0, ii + 1, new Label(channelNickName[ii]));
//			channelGrid.setWidget(0, ii + 1, new Label("Ch" + Integer.toString(ii + 1)));
			formatter.setHorizontalAlignment(0, ii + 1, HasHorizontalAlignment.ALIGN_CENTER);
			formatter.setVerticalAlignment(0, ii + 1, HasVerticalAlignment.ALIGN_MIDDLE);	
			channelGrid.setWidget(1, ii + 1, startTime[ii]);
			channelGrid.setWidget(2, ii + 1, stopTime[ii]);
			for (int ij = 0; ij < 8; ++ij)
			{
				eventBit[ii][ij] = new CheckBox();
				channelGrid.setWidget(ij + 3, ii + 1, eventBit[ii][ij]);
				formatter.setHorizontalAlignment(ij + 3, ii + 1, HasHorizontalAlignment.ALIGN_CENTER);
				formatter.setVerticalAlignment(ij + 3, ii + 1, HasVerticalAlignment.ALIGN_MIDDLE);	
			}
		}
		settingButtonGrid = new SettingButtonGrid(settingsPermitted);
		VerticalPanel vp1 = new VerticalPanel();
		vp1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vp1.add(settingButtonGrid);
		vp1.add(channelGrid);
		CaptionPanel cp = new CaptionPanel(iceCubeTimerName);
		cp.add(vp1);
		add(cp);
		iceCubeTimerMqttData = new IceCubeTimerMqttData();
	}
	private void enableInput(boolean enableInput)
	{
		for (int ii = 0; ii < 4; ++ii)
		{
			startTime[ii].setEnabled(enableInput);
			stopTime[ii].setEnabled(enableInput);
			for (int ij = 0; ij < 8; ++ij)
			{
				eventBit[ii][ij].setEnabled(enableInput);
			}
		}
	}
	private void setIceCubeTimer() throws Exception
	{
		entryPointApp.setupApp.getStatusTextArea().addStatus("Setting " + iceCubeTimerName + "...");
		String[][] jsonArray = new String[4][2];
		for (int ii = 0; ii < 4; ++ii)
		{
			int startval = 0;
			int stopval = 0;
			try
			{startval = Integer.parseInt(startTime[ii].getText());
			}catch (NumberFormatException nfe) {throw new Exception("Start time " + Integer.toString(ii + 1) + " not a number");}
			if (startval < 0) throw new Exception("Start time " + Integer.toString(ii + 1) + " less than zero");
			try
			{stopval = Integer.parseInt(stopTime[ii].getText());
			}catch (NumberFormatException nfe) {throw new Exception("Stop time " + Integer.toString(ii + 1) + " not a number");}
			if (stopval <= startval) throw new Exception("Start time " + Integer.toString(ii + 1) + " less than Start time");
			int mask = 0;
			int pow2 = 1;
			for (int ij = 0; ij < 8; ++ij)
			{
				if (eventBit[ii][ij].getValue()) mask = mask + pow2;
				pow2 = pow2 * 2;
			}
			String channelInfo = Integer.toString(mask) + " " + Integer.toString(startval) + " " + Integer.toString(stopval);
			jsonArray[ii][0] = "channel" + Integer.toString(ii + 1);
			jsonArray[ii][1] = channelInfo;
		}
		entryPointApp.mqttService.publishJsonArray(mqtttopic, jsonArray, settingsPermitted, entryPointApp.setupApp.isDebug(), "ok", new IceCubeTimerPublishSettingsCallback());
		enableInput(false);		
	}
	private void updateReadings() throws Exception
	{
		if (settingButtonGrid.isSettingsEnabled()) return;
		for (int ii = 0; ii < 4; ++ii)
		{
			String channelInfo = iceCubeTimerMqttData.getJsonValue("channel" + Integer.toString(ii + 1));
			channelInfo = channelInfo.trim();
			String[] channelData = channelInfo.split(" ");
			startTime[ii].setText(channelData[1]);
			stopTime[ii].setText(channelData[2]);
			byte mask = intToByte(Integer.parseInt(channelData[0]))[0];
			for (int ij = 0; ij < 8; ++ij)
			{
				boolean bitOn = false;
				if (getBit(mask, ij) > 0) bitOn = true;
				eventBit[ii][ij].setValue(bitOn);
			}
		}
		
	}
	private int getBit(byte myByte, int position)
	{
	   return (myByte >> position) & 1;
	}
	public byte[] intToByte(int itarget)
	{
		
	    int n = 0;
	    int goal = 0xFF << (8 * n);
	    int b0 = (itarget & goal) >> (8 * n);
	    
	    n = 1;
	    goal = 0xFF << (8 * n);
	    int b1 = (itarget & goal) >> (8 * n);
	    
	    byte[] twoBytes = new byte[2];
	    twoBytes[0] = (byte) b0;
	    twoBytes[1] = (byte) b1;
	    
	    return twoBytes;
	}
	class IceCubeTimerMqttData extends MqttData
	{
		public IceCubeTimerMqttData() 
		{
			super(mqtttopic, MqttData.JSONDATA, 1000, entryPointApp);
		}

		@Override
		public void doSomethingWithData() 
		{
			try 
			{
				updateReadings();
			} catch (Exception e) 
			{
				entryPointApp.setupApp.getStatusTextArea().addStatus(e.getMessage());
			}
			
		}
	}
	class IceCubeTimerPublishSettingsCallback implements AsyncCallback<String>
	{
		@Override
		public void onFailure(Throwable caught) 
		{
			entryPointApp.setupApp.getStatusTextArea().addStatus("Failure: Putting " + iceCubeTimerName + " Settings");
			entryPointApp.setupApp.getStatusTextArea().addStatus(caught.getMessage());
		}

		@Override
		public void onSuccess(String result) 
		{
			entryPointApp.setupApp.getStatusTextArea().addStatus("Success: Putting " + iceCubeTimerName + " Settings");
		}
		
	}
	class SettingButtonGrid extends GskelSettingButtonGrid
	{

		public SettingButtonGrid(boolean settingsPermitted) 
		{
			super(settingsPermitted);
		}

		@Override
		public void enableSettingsInput(boolean enabled) 
		{
			enableInput(enabled);
		}

		@Override
		public void doSettings() 
		{
			try 
			{
				setIceCubeTimer();
			} catch (Exception e) 
			{
				entryPointApp.setupApp.getStatusTextArea().addStatus(e.getMessage());
			}
		}
		
	}

}
