package se.esss.litterbox.its.toshibagwt.client.contentpanels;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import se.esss.litterbox.its.toshibagwt.client.EntryPointApp;
import se.esss.litterbox.its.toshibagwt.client.bytegearboxservice.ByteGearBoxData;
import se.esss.litterbox.its.toshibagwt.shared.bytegearboxgwt.ByteGearGwt;
import se.esss.litterbox.its.toshibagwt.shared.bytegearboxgwt.ByteToothGwt;

public class ThermCaptionPanel extends CaptionPanel
{
	private ThermPlotPanel thermPlotPanel;
	private boolean loaded = false;
	ByteGearGwt byteGear = null;
	ByteToothGwt valByteTooth = null;
	ByteToothGwt interlockByteTooth = null;
	ByteToothGwt errByteTooth = null;
	ByteToothGwt warningByteTooth = null;
	ByteGearBoxData byteGearBoxData = null;
	private String red = "#ff0000";
	private String yellow = "#ffff00";
	private String green = "#00ff00";
	private String plotWidth = "50px";
	private String plotHeight = "90px";
	private int fontPixelSize = 15;
	private int chartAreaPixelLeftOffset = 35;
	private int chartAreaPixelWidth = 20;
	private double chartAreaOpacity = 0.5;
	private String chartAreaBackgroundColor = "#aaaaaa";
	private String plotBackgroundColor = "#0095CD";
	private String gridlineColor = "#000000";
	private Label interlockLabel = new Label("OK");
	NumberFormat twoPlaces = NumberFormat.getFormat("#.##");
	NumberFormat onePlaces = NumberFormat.getFormat("#.#");
	
	public boolean isLoaded() {return loaded;}
	public ThermPlotPanel getThermPlotPanel() {return thermPlotPanel;}
	
	public ThermCaptionPanel(String captionTitle, String topic, String byteGearName, String valByteToothName, String lolo, String low, String high, String hihi, EntryPointApp entryPointApp)
	{
		super(captionTitle);
		try 
		{
			byteGearBoxData = entryPointApp.getByteGearBoxData(topic);
			byteGear = byteGearBoxData.getByteGearBoxGwt().getByteGear(byteGearName);
			valByteTooth = byteGear.getReadByteTooth(valByteToothName);
			interlockByteTooth = byteGear.getReadByteTooth("INTERLOCK");
			errByteTooth = byteGear.getReadByteTooth("ERR");
			warningByteTooth = byteGear.getReadByteTooth("WARNING");
			loaded = false;
			thermPlotPanel = new ThermPlotPanel();
			thermPlotPanel.setMinValue(Double.parseDouble(byteGear.getReadByteTooth(lolo).getValue()));
			double[] limits = new double[3];
			limits[0] = Double.parseDouble(byteGear.getReadByteTooth(low).getValue());
			limits[1] = Double.parseDouble(byteGear.getReadByteTooth(high).getValue());
			limits[2] = Double.parseDouble(byteGear.getReadByteTooth(hihi).getValue());
			String[] color = {yellow, green, yellow, red};
			thermPlotPanel.setColor(color);
			thermPlotPanel.setPlotWidth(plotWidth);
			thermPlotPanel.setPlotHeight(plotHeight);
			thermPlotPanel.setFontPixelSize(fontPixelSize);
			thermPlotPanel.setTextColor(yellow);
			thermPlotPanel.setChartAreaPixelLeftOffset(chartAreaPixelLeftOffset);
			thermPlotPanel.setChartAreaPixelWidth(chartAreaPixelWidth);
			thermPlotPanel.setChartAreaOpacity(chartAreaOpacity);
			thermPlotPanel.setChartAreaBackgroundColor(chartAreaBackgroundColor);
			thermPlotPanel.setPlotBackgroundColor(plotBackgroundColor);
			thermPlotPanel.setGridlineColor(gridlineColor);
			thermPlotPanel.setMax(limits);
			interlockLabel.setStyleName("statusInterlockOk");
		} catch (Exception e) {GWT.log(e.getMessage());}
	}
	public void initialize()
	{
		thermPlotPanel.initialize();
		VerticalPanel vp1 = new VerticalPanel();
		vp1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vp1.setWidth("100%");
		vp1.add(interlockLabel);
		vp1.add(thermPlotPanel);
		add(vp1);
		LoadPlotTimer lpt = new LoadPlotTimer();
		lpt.scheduleRepeating(50);
	}
	public void updateReadings()
	{
		if (!loaded) return;
		double dvalue = Double.parseDouble(valByteTooth.getValue());
		thermPlotPanel.setValue(dvalue);
		thermPlotPanel.draw();
		thermPlotPanel.setChartVisible(true);
		interlockLabel.setText(onePlaces.format(dvalue));
		if (Boolean.parseBoolean(interlockByteTooth.getValue()))
		{
			interlockLabel.setStyleName("statusInterlockNotOk");
		}
		else
		{
			if(Boolean.parseBoolean(warningByteTooth.getValue()))
			{
				interlockLabel.setStyleName("statusInterlockWarn");
			}
			else
			{
				interlockLabel.setStyleName("statusInterlockOk");
			}
		}
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
