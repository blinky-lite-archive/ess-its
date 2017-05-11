package se.esss.litterbox.its.toshibagwt.client.contentpanels;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.corechart.SteppedAreaChart;
import com.googlecode.gwt.charts.client.corechart.SteppedAreaChartOptions;
import com.googlecode.gwt.charts.client.options.ChartArea;
import com.googlecode.gwt.charts.client.options.Gridlines;
import com.googlecode.gwt.charts.client.options.HAxis;
import com.googlecode.gwt.charts.client.options.Legend;
import com.googlecode.gwt.charts.client.options.LegendPosition;
import com.googlecode.gwt.charts.client.options.TextStyle;
import com.googlecode.gwt.charts.client.options.TitlePosition;
import com.googlecode.gwt.charts.client.options.VAxis;

public class ThermPlotPanel extends VerticalPanel
{
	private SteppedAreaChart chart;
	private double value = 80;
	private DataTable dataTable;
	private SteppedAreaChartOptions options;
	private ChartLoader chartLoader;
	private ChartRunnable chartRunnable;
	private boolean loaded = false;
	private String plotWidth = "100px";
	private String plotHeight = "100px";
	private double fontPixelSize = 10;
	private double minValue = 0;
	private double[] min = {0.0, 30.0, 70.0, 70.01};
	private double[] max = {30.0, 70.0, 100, 10.01};
	private String[] color = {"#0000ff", "#00ff00", "#ff0000", "#ff0000"};
	private String textColor = "ffffff";
	private int chartAreaPixelLeftOffset = 40;
	private int chartAreaPixelWidth = 20;
	private double chartAreaOpacity = 0.5;
	private String chartAreaBackgroundColor = "#aaaaaa";
	private String plotBackgroundColor = "#0095CD";
	private String gridlineColor = "#000000";
	HAxis haxis = HAxis.create("");
	TextStyle haxisTextStyle = TextStyle.create();

	public void setPlotHeight(String plotHeight) {this.plotHeight = plotHeight;}
	public void setFontPixelSize(double fontPixelSize) {this.fontPixelSize = fontPixelSize;}
	public void setMinValue(double minValue) {this.minValue = minValue;}
	public void setMax(double[] max) 
	{
		for (int ii = 0; ii < 3; ++ii) this.max[ii] = max[ii];
		this.max[3] = this.max[2] * 1.01;
	}
	public void setColor(String[] color) {for (int ii = 0; ii < 4; ++ii) this.color[ii] = color[ii];}
	public void setPlotWidth(String plotWidth) {this.plotWidth = plotWidth;}
	public void setTextColor(String textColor) {this.textColor = textColor;}
	public void setChartAreaPixelLeftOffset(int chartAreaPixelLeftOffset) {this.chartAreaPixelLeftOffset = chartAreaPixelLeftOffset;}
	public void setChartAreaPixelWidth(int chartAreaPixelWidth) {this.chartAreaPixelWidth = chartAreaPixelWidth;}
	public void setChartAreaOpacity(double chartAreaOpacity) {this.chartAreaOpacity = chartAreaOpacity;}
	public void setChartAreaBackgroundColor(String chartAreaBackgroundColor) {this.chartAreaBackgroundColor = chartAreaBackgroundColor;}
	public void setPlotBackgroundColor(String plotBackgroundColor) {this.plotBackgroundColor = plotBackgroundColor;}
	public void setGridlineColor(String gridlineColor) {this.gridlineColor = gridlineColor;}

	public boolean isLoaded() {return loaded;}
	
	public ThermPlotPanel()
	{
		loaded = false;
	}
	public void setValue(double value) {this.value = value;}
	public void setChartVisible(boolean visible)
	{
		if (visible) chart.setHeight(plotHeight);
		if (visible) chart.setWidth(plotWidth);
		chart.setVisible(visible);
	}

	public void initialize() 
	{
		min[0] = minValue;
		min[1] = max[0];
		min[2] = max[1];
		min[3] = max[2];
		value = minValue;
		loaded = false;
		chartLoader = new ChartLoader(ChartPackage.CORECHART);
		chartRunnable = new ChartRunnable(this);
		chartLoader.loadApi(chartRunnable);
	
	}
	private void setup()
	{
		// Prepare the data
		dataTable = DataTable.create();
		dataTable.addColumn(ColumnType.STRING, "");
		dataTable.addColumn(ColumnType.NUMBER, "");
		dataTable.addColumn(ColumnType.NUMBER, "");
		dataTable.addColumn(ColumnType.NUMBER, "");
		dataTable.addColumn(ColumnType.NUMBER, "");
		dataTable.addRows(1);
		dataTable.setValue(0, 0, "");
		
		options = SteppedAreaChartOptions.create();
		VAxis vaxis = VAxis.create("");
		vaxis.setMaxValue(max[2]);
		vaxis.setMinValue(min[0]);
//		double[] ticks = {min[0], min[1], min[2], max[2]};
		double[] ticks = {min[1], min[2]};
		vaxis.setTicks(ticks);
		TextStyle textStyle = TextStyle.create();
		textStyle.setColor(textColor);
		vaxis.setTextStyle(textStyle);
		Gridlines gridlines = Gridlines.create();
		gridlines.setColor(gridlineColor);
		vaxis.setGridlines(gridlines);
		
		options.setVAxis(vaxis);
		options.setColors(color);
		options.setIsStacked(false);
		Legend legend = Legend.create();
		legend.setPosition(LegendPosition.NONE);
		options.setLegend(legend);
		options.setFontSize(fontPixelSize);
		options.setTitlePosition(TitlePosition.NONE);
		ChartArea chartArea = ChartArea.create();
		chartArea.setLeft(chartAreaPixelLeftOffset);
		chartArea.setWidth(chartAreaPixelWidth);
		chartArea.setBackgroundColor(chartAreaBackgroundColor);
		options.setBackgroundColor(plotBackgroundColor);
		options.setChartArea(chartArea);
		options.setAreaOpacity(chartAreaOpacity);
	}
	public void draw() 
	{
		double[] tempVal = {min[0],min[0],min[0], min[0]};
		dataTable.setValue(0, 0, NumberFormat.getFormat("0.0").format(value));
		if (value < min[0]) value = min[0];
		if (value > max[2])
		{
			value = max[2] * 1.005;
		}
		for (int ii = 0; ii < 4; ++ii)
		{
			if (value >= max[ii]) 
			{
				tempVal[ii] = min[0];
			}
			else
			{
				if (value < min[ii])
				{
					tempVal[ii] = min[0];
				}
				else
				{
					tempVal[ii] = value;
					String valColor = color[ii];
					haxisTextStyle.setColor(valColor);
				}
			}
			dataTable.setValue(0, ii + 1, tempVal[ii]);
		}
		haxisTextStyle.setFontSize(1);
		haxis.setTextStyle(haxisTextStyle);
		options.setHAxis(haxis);

		// Draw the chart
		chart.draw(dataTable, options);
	}
	private static class ChartRunnable implements Runnable
	{
		ThermPlotPanel plotPanel;
		private ChartRunnable(ThermPlotPanel plotPanel)
		{
			this.plotPanel = plotPanel;
		}
		@Override
		public void run() 
		{
			plotPanel.chart = new SteppedAreaChart();
			plotPanel.chart.setVisible(false);
			plotPanel.setHorizontalAlignment(ALIGN_CENTER);
			plotPanel.add(plotPanel.chart);
			plotPanel.setup();
			plotPanel.draw();
			plotPanel.loaded = true;
		}
		
	}
}
