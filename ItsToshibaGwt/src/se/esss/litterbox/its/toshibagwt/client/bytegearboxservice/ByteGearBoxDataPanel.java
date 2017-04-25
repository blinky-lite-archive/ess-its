package se.esss.litterbox.its.toshibagwt.client.bytegearboxservice;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import se.esss.litterbox.its.toshibagwt.client.gskel.GskelVerticalPanel;

public class ByteGearBoxDataPanel extends GskelVerticalPanel
{
	private ByteGearBoxData byteGearBoxData;
	private Grid byteGearGrid;
	private int igearSelected = -1;
	private CaptionPanel byteToothReadCaptionPanel = new CaptionPanel("Read Properties");
	private CaptionPanel byteToothWriteCaptionPanel = new CaptionPanel("Write Properties");
	private UpdateByteToothPanelTimer updateByteToothPanelTimer;

	public ByteGearBoxData getByteGearBoxData() {return byteGearBoxData;}
	public ByteGearBoxDataPanel(ByteGearBoxData byteGearBoxData) 
	{
		super(true, byteGearBoxData.getEntryPointApp());
		this.byteGearBoxData =  byteGearBoxData;
		byteGearGrid = new Grid(byteGearBoxData.getByteGearBoxGwt().getNumByteGear(),1);
		for (int ii = 0; ii < byteGearBoxData.getByteGearBoxGwt().getNumByteGear(); ++ii)
		{
			Label byteGearLabel = new Label(byteGearBoxData.getByteGearBoxGwt().getByteGearList().get(ii).getName());
			byteGearLabel.setStyleName("byteGearNotSelected");
			byteGearGrid.setWidget(ii, 0, byteGearLabel);
			byteGearLabel.addClickHandler(new ByteGearClickHandler(ii, byteGearBoxData));
		}
		CaptionPanel byteGearCaptionPanel = new CaptionPanel("Devices");
		byteGearCaptionPanel.add(byteGearGrid);
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.add(byteGearCaptionPanel);
		hp1.add(byteToothReadCaptionPanel);
		hp1.add(byteToothWriteCaptionPanel);
		byteToothReadCaptionPanel.setVisible(false);
		byteToothWriteCaptionPanel.setVisible(false);
		add(hp1);
		updateByteToothPanelTimer = new UpdateByteToothPanelTimer();
		updateByteToothPanelTimer.scheduleRepeating(1000);
	}
	class ByteGearClickHandler implements ClickHandler
	{
		int igear = -1;
		ByteToothReadPanel byteToothReadPanel;
		ByteToothWritePanel byteToothWritePanel;
		ByteGearBoxData byteGearBoxData;
		ByteGearClickHandler(int igear, ByteGearBoxData byteGearBoxData)
		{
			this.igear = igear;
			this.byteGearBoxData = byteGearBoxData;
		}
		@Override
		public void onClick(ClickEvent event) 
		{
			if (igearSelected < 0)
			{
				byteGearGrid.getWidget(igear, 0).setStyleName("byteGearSelected");
				igearSelected = igear;
			}
			else
			{
				byteGearGrid.getWidget(igearSelected, 0).setStyleName("byteGearNotSelected");
				byteToothReadCaptionPanel.clear();
				byteToothWriteCaptionPanel.clear();
				if (igearSelected == igear)
				{
					igearSelected = -1;
				}
				else
				{
					byteGearGrid.getWidget(igear, 0).setStyleName("byteGearSelected");
					igearSelected = igear;
				}
			}
			if (igearSelected >= 0)
			{
				byteToothReadCaptionPanel.setVisible(true);
				byteToothWriteCaptionPanel.setVisible(true);
				byteToothReadPanel = new ByteToothReadPanel(byteGearBoxData.getByteGearBoxGwt().getByteGearList().get(igearSelected));
				byteToothWritePanel = new ByteToothWritePanel(byteGearBoxData.getByteGearBoxGwt().getByteGearList().get(igearSelected), byteGearBoxData);
				updateByteToothPanelTimer.setByteToothReadPanel(byteToothReadPanel);
				updateByteToothPanelTimer.setByteToothWritePanel(byteToothWritePanel);
				byteToothReadCaptionPanel.add(byteToothReadPanel);
				byteToothWriteCaptionPanel.add(byteToothWritePanel);
				updateByteToothPanelTimer.setPauseTimer(false);
			}
			else
			{
				updateByteToothPanelTimer.setPauseTimer(true);
				byteToothReadCaptionPanel.setVisible(false);
				byteToothWriteCaptionPanel.setVisible(false);
			}
			
		}
		
	}
	public static class UpdateByteToothPanelTimer extends Timer
	{
		ByteToothReadPanel byteToothReadPanel;
		ByteToothWritePanel byteToothwritePanel;

		private boolean pauseTimer = true;
		public boolean isPauseTimer() {return pauseTimer;}

		public void setPauseTimer(boolean pauseTimer) {this.pauseTimer = pauseTimer;}
		public void setByteToothReadPanel(ByteToothReadPanel byteToothReadPanel) {this.byteToothReadPanel = byteToothReadPanel;}
		public void setByteToothWritePanel(ByteToothWritePanel byteToothwritePanel) {this.byteToothwritePanel = byteToothwritePanel;}

		UpdateByteToothPanelTimer()
		{
		}

		@Override
		public void run() 
		{
			if (!pauseTimer)
			{
				if (byteToothReadPanel != null) byteToothReadPanel.updateReadings();
				if (byteToothwritePanel != null) byteToothwritePanel.updateReadings();

			}
		}
	}

}
