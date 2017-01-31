package se.esss.litterbox.its.bluemodgwt.client.googleplots;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.corechart.ScatterChart;
import com.googlecode.gwt.charts.client.corechart.ScatterChartOptions;
import com.googlecode.gwt.charts.client.options.HAxis;
import com.googlecode.gwt.charts.client.options.VAxis;


public class TimeLinePlotPanel extends HorizontalPanel
{
	private ScatterChart scatterChart;
	private DataTable dataTable;
	private ScatterChartOptions options;
	private ChartLoader chartLoader;
	private ChartRunnable chartRunnable;
	private int numPts;
	private int numTraces;
	private String title;
	private String haxisLabel;
	private String yaxisLabel;
	private String[] legend;
	private double[] xaxis;
	private double[][] traces;
	private String plotWidth;
	private String plotHeight;
	private boolean loaded = false;
	
	public int getNumPts() {return numPts;}
	public int getNumTraces() {return numTraces;}
	public String getTitle() {return title;}
	public String getHaxisLabel() {return haxisLabel;}
	public String getYaxisLabel() {return yaxisLabel;}
	public String[] getLegend() {return legend;}
	public double[] getXaxis() {return xaxis;}
	public double[][] getTraces() {return traces;}
	public String getPlotWidth() {return plotWidth;}
	public String getPlotHeight() {return plotHeight;}
	public boolean isLoaded() {return loaded;}

	public TimeLinePlotPanel(int numPts, int numTraces, String title, String haxisLabel, String yaxisLabel, String[] legend, String plotWidth, String plotHeight) 
	{
		loaded = false;
		this.numPts = numPts;
		this.numTraces = numTraces;
		this.title = title;
		this.haxisLabel = haxisLabel;
		this.yaxisLabel = yaxisLabel;
		this.plotWidth = plotWidth;
		this.plotHeight = plotHeight;
		this.legend = legend;
		xaxis = new double[numPts];
		traces = new double[numTraces][numPts];
		for (int ii = 0; ii < numPts; ++ii) xaxis[ii] = 0;
		for (int itrace = 0; itrace < numTraces; ++itrace)
			for (int ii = 0; ii < numPts; ++ii) 
				traces[itrace][ii] = 0;
	}

	public void initialize() 
	{
		loaded = false;
		chartLoader = new ChartLoader(ChartPackage.CORECHART);
		chartRunnable = new ChartRunnable(this);
		chartLoader.loadApi(chartRunnable);
	}

	private void setup()
	{
		// Prepare the data
		dataTable = DataTable.create();
		dataTable.addColumn(ColumnType.NUMBER, haxisLabel);
		for (int ii = 0; ii < numTraces; ++ii) dataTable.addColumn(ColumnType.NUMBER, legend[ii]);
		dataTable.addRows(numPts * numTraces);

		// Set options
		options = ScatterChartOptions.create();
		options.setBackgroundColor("#f0f0f0");
		options.setFontName("Tahoma");
		options.setTitle(title);
		options.setHAxis(HAxis.create(haxisLabel));
		options.setVAxis(VAxis.create(yaxisLabel));

	}
	public void draw() 
	{
		for (int itrace = 0; itrace < numTraces; ++itrace)
		{
			for (int ii = 0; ii < numPts; ++ii) 
			{
				dataTable.setValue(ii + itrace * numPts, 0, xaxis[ii]);
				dataTable.setValue(ii + itrace * numPts, itrace + 1, traces[itrace][ii]);
			}
		}

		scatterChart.draw(dataTable, options);
	}

	private static class ChartRunnable implements Runnable
	{
		TimeLinePlotPanel timeLinePlotPanel;
		private ChartRunnable(TimeLinePlotPanel timeLinePlotPanel)
		{
			this.timeLinePlotPanel = timeLinePlotPanel;
		}
		@Override
		public void run() 
		{
			timeLinePlotPanel.scatterChart = new ScatterChart();
			timeLinePlotPanel.add(timeLinePlotPanel.scatterChart);
			timeLinePlotPanel.setup();
			timeLinePlotPanel.draw();
			timeLinePlotPanel.scatterChart.setWidth(timeLinePlotPanel.plotWidth);
			timeLinePlotPanel.scatterChart.setHeight(timeLinePlotPanel.plotHeight);
			timeLinePlotPanel.loaded = true;
		}
		
	}
}
