package se.esss.litterbox.its.toshibagwt.client.bytegearboxservice;

import java.util.Date;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

import se.esss.litterbox.its.toshibagwt.client.EntryPointApp;
import se.esss.litterbox.its.toshibagwt.client.googleplots.TimeLineChartPlotPanel;
import se.esss.litterbox.its.toshibagwt.client.gskel.GskelVerticalPanel;
import se.esss.litterbox.its.toshibagwt.shared.bytegearboxgwt.ByteGearGwt;
import se.esss.litterbox.its.toshibagwt.shared.bytegearboxgwt.ByteToothGwt;

public class ByteGearBoxPlotter extends GskelVerticalPanel
{
	private static final int maxNumTraces = 4;

	private Grid traceDefinitionGrid;
	private ListBox[] byteGearBoxListBox = new ListBox[maxNumTraces];
	private ListBox[] byteGearListBox = new ListBox[maxNumTraces];
	private ListBox[] byteToothListBox = new ListBox[maxNumTraces];
	private ListBox[] multiplierListBox = new ListBox[maxNumTraces];
	private CheckBox[] enabledCheckBox = new CheckBox[maxNumTraces];
	private int[] igearBoxSelected = new int[maxNumTraces];
	private int[] igearSelected = new int[maxNumTraces];
	private int[] itoothSelected = new int[maxNumTraces];
	private TextBox plotWindowSpanTimeTextField = new TextBox();
	private Button startPlotButton = new Button("Start Plot");
	private int numTracesEnabled = 0;
	private boolean plotRunning =  false;
	private int readingUpdateRateMilliSec = 1000;
	private ByteToothGwt[] valByteTooth = null;
	private Date startPlotDate = new Date();
	private double[] valMult;
	private TimeLineChartPlotPanel timeLineChartPlot = null;
	private Grid controlAndPlotGrid = new Grid(2,1);
	private Label[] readings = new Label[maxNumTraces];
	private int[] itraceMapping = new int[maxNumTraces];
	NumberFormat twoPlaces = NumberFormat.getFormat("#.##");

	
	private ByteGearBoxData[] byteGearBoxData;

	public ByteGearBoxPlotter(ByteGearBoxData[] byteGearBoxData, EntryPointApp entryPointApp) 
	{
		super(true, entryPointApp);
		this.byteGearBoxData = byteGearBoxData;
		traceDefinitionGrid = new Grid(maxNumTraces + 1, 7);
		traceDefinitionGrid.setWidget(0, 0, new Label("Trace"));
		traceDefinitionGrid.setWidget(0, 1, new Label("ByteGearBox"));
		traceDefinitionGrid.setWidget(0, 2, new Label("ByteGear"));
		traceDefinitionGrid.setWidget(0, 3, new Label("ByteGearTooth"));
		traceDefinitionGrid.setWidget(0, 4, new Label("Mult."));
		traceDefinitionGrid.setWidget(0, 5, new Label("Enabled"));
		traceDefinitionGrid.setWidget(0, 6, new Label("Reading"));

		traceDefinitionGrid.setBorderWidth(1);
		CellFormatter cf = traceDefinitionGrid.getCellFormatter();
		for (int irow = 0; irow <= maxNumTraces; ++irow)
		{
			for (int icol = 0; icol < 7; ++icol)
			{
				cf.setHorizontalAlignment(irow, icol, HasHorizontalAlignment.ALIGN_CENTER);
				cf.setVerticalAlignment(irow, icol, HasVerticalAlignment.ALIGN_MIDDLE);	

			}
		}

		for (int ii = 0; ii < maxNumTraces; ++ii)
		{
			igearBoxSelected[ii] = -1;
			igearSelected[ii] = -1;
			itoothSelected[ii] = -1;
			traceDefinitionGrid.setWidget(ii + 1, 0, new Label(Integer.toString(ii + 1)));
			byteGearBoxListBox[ii] = new ListBox();
			byteGearBoxListBox[ii].addChangeHandler(new ByteGearBoxSelectedHandler(ii));
			byteGearBoxListBox[ii].setWidth("12.0em");
			traceDefinitionGrid.setWidget(ii + 1, 1, byteGearBoxListBox[ii]);
			byteGearBoxListBox[ii].addItem("");
			for (int ij = 0; ij < byteGearBoxData.length; ++ij) byteGearBoxListBox[ii].addItem(byteGearBoxData[ij].getByteGearBoxGwt().getTopic());

			byteGearListBox[ii] = new ListBox();
			byteGearListBox[ii].setEnabled(false);
			byteGearListBox[ii].addChangeHandler(new ByteGearSelectedHandler(ii));
			byteGearListBox[ii].setWidth("15.0em");
			traceDefinitionGrid.setWidget(ii + 1, 2, byteGearListBox[ii]);

			byteToothListBox[ii] = new ListBox();
			byteToothListBox[ii].setEnabled(false);
			byteToothListBox[ii].addChangeHandler(new ByteToothSelectedHandler(ii));
			byteToothListBox[ii].setWidth("10.0em");
			traceDefinitionGrid.setWidget(ii + 1, 3, byteToothListBox[ii]);

			multiplierListBox[ii] = new ListBox();
			multiplierListBox[ii].setEnabled(false);
			multiplierListBox[ii].addItem("0.001");
			multiplierListBox[ii].addItem("0.01");
			multiplierListBox[ii].addItem("0.1");
			multiplierListBox[ii].addItem("1");
			multiplierListBox[ii].addItem("10");
			multiplierListBox[ii].addItem("100");
			multiplierListBox[ii].addItem("1000");
			multiplierListBox[ii].setWidth("5.0em");
			multiplierListBox[ii].setItemSelected(3, true);
			traceDefinitionGrid.setWidget(ii + 1, 4, multiplierListBox[ii]);

			enabledCheckBox[ii] = new CheckBox();
			enabledCheckBox[ii].addClickHandler(new EnabledButtonChanged());
			enabledCheckBox[ii].setValue(false);
			enabledCheckBox[ii].setEnabled(false);
			traceDefinitionGrid.setWidget(ii + 1, 5, enabledCheckBox[ii]);
			
			readings[ii] = new Label("");
			readings[ii].setWidth("5.0em");
			traceDefinitionGrid.setWidget(ii + 1, 6, readings[ii]);
		}
		
		startPlotButton.setEnabled(false);
		startPlotButton.addClickHandler(new StartPlotButtonClickHandler());
		startPlotButton.setWidth("100%");
		
		Grid plotWindowDefGridPanel = new Grid(1,2);
		plotWindowSpanTimeTextField.setWidth("3.0em");
		plotWindowSpanTimeTextField.setText("1000");
		plotWindowDefGridPanel.setWidget(0, 0, plotWindowSpanTimeTextField);
		plotWindowDefGridPanel.setWidget(0, 1, new Label("seconds"));
		CaptionPanel plotWindowDefCaptionPanel = new CaptionPanel("Window Length");
		plotWindowDefCaptionPanel.add(plotWindowDefGridPanel);
		Grid controlPanel  = new Grid(2,1);
		controlPanel.setWidget(0, 0, plotWindowDefCaptionPanel);
		controlPanel.setWidget(1, 0, startPlotButton);
		Grid traceDefAndControlPanel = new Grid(1,2);
		traceDefAndControlPanel.setWidget(0, 0, traceDefinitionGrid);
		traceDefAndControlPanel.setWidget(0, 1, controlPanel);
		controlAndPlotGrid.setWidget(0, 0, traceDefAndControlPanel);
		add(controlAndPlotGrid);
	}
	private void checkNumberTracesEnabled()
	{
		numTracesEnabled = 0;
		for (int irow = 0; irow < maxNumTraces; ++irow)
		{
			if (enabledCheckBox[irow].getValue()) numTracesEnabled = numTracesEnabled + 1;
		}
		if (plotRunning) return;
		if (numTracesEnabled > 0)
		{
			startPlotButton.setEnabled(true);
		}
		else
		{
			startPlotButton.setEnabled(false);
			
		}
	}
	private void updateReadings()
	{
		double timeSec =  (double) ((new Date().getTime() - startPlotDate.getTime()) / 1000);
		double[] yaxisData = new double[valByteTooth.length];
		for (int ii = 0; ii < valByteTooth.length; ++ii)
		{
			String valData = valByteTooth[ii].getValue();
			if (valByteTooth[ii].getType().equals("BOOLEAN"))
			{
				valData = "0";
				if (valByteTooth[ii].getValue().equals("true")) valData = "1";
			}
			if (valByteTooth[ii].getType().equals("S7DT"))  valData = "0";
			
			yaxisData[ii] = Double.parseDouble(valData) * valMult[ii];
			readings[itraceMapping[ii]].setText(twoPlaces.format(yaxisData[ii] / valMult[ii]));
		}
		timeLineChartPlot.draw(timeSec, yaxisData);
	}

	class ByteGearBoxSelectedHandler implements ChangeHandler
	{
		int itrace;
		ByteGearBoxSelectedHandler(int itrace)
		{
			this.itrace = itrace;
		}
		@Override
		public void onChange(ChangeEvent event) 
		{
			byteGearListBox[itrace].clear();
			byteToothListBox[itrace].clear();
			multiplierListBox[itrace].setItemSelected(3, true);
			byteGearListBox[itrace].setEnabled(false);
			byteToothListBox[itrace].setEnabled(false);
			multiplierListBox[itrace].setEnabled(false);
			enabledCheckBox[itrace].setEnabled(false);
			enabledCheckBox[itrace].setValue(false);
			readings[itrace].setText("");
			checkNumberTracesEnabled();
			igearSelected[itrace] = -1;
			itoothSelected[itrace] = -1;
			igearBoxSelected[itrace] = byteGearBoxListBox[itrace].getSelectedIndex() - 1;
			if (igearBoxSelected[itrace] >= 0)
			{
				byteGearListBox[itrace].addItem("");
				for (int igear = 0; igear < byteGearBoxData[igearBoxSelected[itrace]].getByteGearBoxGwt().getByteGearList().size(); ++igear)
				{
					byteGearListBox[itrace].addItem(byteGearBoxData[igearBoxSelected[itrace]].getByteGearBoxGwt().getByteGearList().get(igear).getName());
				}
				byteGearListBox[itrace].setEnabled(true);
			}
		}
	}
	class ByteGearSelectedHandler implements ChangeHandler
	{
		int itrace;
		ByteGearSelectedHandler(int itrace)
		{
			this.itrace = itrace;
		}
		@Override
		public void onChange(ChangeEvent event) 
		{
			byteToothListBox[itrace].clear();
			multiplierListBox[itrace].setItemSelected(3, true);
			byteToothListBox[itrace].setEnabled(false);
			multiplierListBox[itrace].setEnabled(false);
			enabledCheckBox[itrace].setEnabled(false);
			enabledCheckBox[itrace].setValue(false);
			readings[itrace].setText("");
			checkNumberTracesEnabled();
			itoothSelected[itrace] = -1;
			igearSelected[itrace] = byteGearListBox[itrace].getSelectedIndex() - 1;
			if (igearSelected[itrace] >= 0)
			{
				byteToothListBox[itrace].addItem("");
				for (int itooth = 0; itooth < byteGearBoxData[igearBoxSelected[itrace]].getByteGearBoxGwt().getByteGearList().get(igearSelected[itrace]).getReadToothList().size(); ++itooth)
				{
					byteToothListBox[itrace].addItem(byteGearBoxData[igearBoxSelected[itrace]].getByteGearBoxGwt().getByteGearList().get(igearSelected[itrace]).getReadToothList().get(itooth).getName());
				}
				byteToothListBox[itrace].setEnabled(true);
			}
		}
	}
	class ByteToothSelectedHandler implements ChangeHandler
	{
		int itrace;
		ByteToothSelectedHandler(int itrace)
		{
			this.itrace = itrace;
		}
		@Override
		public void onChange(ChangeEvent event) 
		{
			multiplierListBox[itrace].setItemSelected(3, true);
			multiplierListBox[itrace].setEnabled(false);
			enabledCheckBox[itrace].setEnabled(false);
			enabledCheckBox[itrace].setValue(false);
			readings[itrace].setText("");
			checkNumberTracesEnabled();
			itoothSelected[itrace] = byteToothListBox[itrace].getSelectedIndex() - 1;
			if (igearSelected[itrace] >= 0)
			{
				multiplierListBox[itrace].setEnabled(true);
				enabledCheckBox[itrace].setEnabled(true);
			}
		}
	}
	class EnabledButtonChanged implements ClickHandler
	{
		@Override
		public void onClick(ClickEvent event) 
		{
			checkNumberTracesEnabled();
		}
		
	}
	class StartPlotButtonClickHandler implements ClickHandler
	{

		UpdateReadingsTimer updateReadingsTimer = new UpdateReadingsTimer();
		@Override
		public void onClick(ClickEvent event) 
		{
			if (plotRunning)				
			{
				plotRunning = false;
				startPlotButton.setText("Start Plot");
				updateReadingsTimer.cancel();
				for (int ii = 0; ii < maxNumTraces; ++ii)
				{
					byteGearBoxListBox[ii].setEnabled(true);
					if (byteGearListBox[ii].getItemCount() > 1)
					{
						byteGearListBox[ii].setEnabled(true);
						if (byteToothListBox[ii].getItemCount() > 1)
						{
							byteToothListBox[ii].setEnabled(true);
							if (byteToothListBox[ii].getSelectedIndex() > 0)
							{
								multiplierListBox[ii].setEnabled(true);
								enabledCheckBox[ii].setEnabled(true);
							}
						}
					}
				}

			}
			else
			{
				try
				{
					if (timeLineChartPlot != null)
					{
						controlAndPlotGrid.remove(timeLineChartPlot);
						timeLineChartPlot = null;
					}
					plotRunning = true;
					startPlotButton.setText("Stop Plot");
					double dnumPts = Double.parseDouble(plotWindowSpanTimeTextField.getText())  * 1000.0 / ((double) readingUpdateRateMilliSec);
					int inumPts = (int) dnumPts;
					String[] byteGearBoxTopic = new String[numTracesEnabled];
					String[] byteGearName = new String[numTracesEnabled];
					String[] valByteToothName  = new String[numTracesEnabled];
					valMult = new double[numTracesEnabled];
					String[] legend  = new String[numTracesEnabled];
					valByteTooth = new ByteToothGwt[numTracesEnabled];
					int itrace = 0;
					for (int ii = 0; ii < maxNumTraces; ++ii)
					{
						byteGearBoxListBox[ii].setEnabled(false);
						byteGearListBox[ii].setEnabled(false);
						byteToothListBox[ii].setEnabled(false);
						multiplierListBox[ii].setEnabled(false);
						enabledCheckBox[ii].setEnabled(false);
						if (enabledCheckBox[ii].getValue())
						{
							byteGearBoxTopic[itrace] = byteGearBoxListBox[ii].getSelectedItemText();
							byteGearName[itrace] = byteGearListBox[ii].getSelectedItemText();
							valByteToothName[itrace] = byteToothListBox[ii].getSelectedItemText();
							valMult[itrace] = Double.parseDouble(multiplierListBox[ii].getSelectedItemText());
							legend[itrace] = byteGearBoxTopic[itrace] + "\n" + byteGearName[itrace] + "\n" + valByteToothName[itrace] + " x " + multiplierListBox[ii].getSelectedItemText();
							ByteGearBoxData byteGearBoxDataPick = byteGearBoxData[byteGearBoxListBox[ii].getSelectedIndex() - 1];
							ByteGearGwt byteGearPick = byteGearBoxDataPick.getByteGearBoxGwt().getByteGear(byteGearName[itrace]);
							valByteTooth[itrace] = byteGearPick.getReadByteTooth(valByteToothName[itrace]);
							itraceMapping[itrace] = ii;
							++itrace;
						}
					}
					startPlotDate = new Date();
					String plotWidth = "950px";
					String plotHeight = "450px";
					timeLineChartPlot = new TimeLineChartPlotPanel(inumPts, itrace, "Time Plot - Start Date: " + startPlotDate.toString(), "Time (sec)", "", legend, plotWidth, plotHeight);
					timeLineChartPlot.setChartAreaPixelLeftOffset(100);
					timeLineChartPlot.setChartAreaPixelWidth(600);
					timeLineChartPlot.setChartAreaPixelTopOffset(30);
					timeLineChartPlot.setChartAreaPixelHeight(350);
					timeLineChartPlot.initialize();
					updateReadingsTimer.scheduleRepeating(readingUpdateRateMilliSec);
					controlAndPlotGrid.setWidget(1, 0, timeLineChartPlot);

				} catch (Exception e) {GWT.log(e.getMessage());}

			}
			
		}
		
	}
	class UpdateReadingsTimer extends Timer
	{
		@Override
		public void run() {updateReadings();}
	}

}
