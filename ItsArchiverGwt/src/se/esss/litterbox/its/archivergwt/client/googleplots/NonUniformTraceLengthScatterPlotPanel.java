package se.esss.litterbox.its.archivergwt.client.googleplots;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.corechart.ScatterChart;
import com.googlecode.gwt.charts.client.corechart.ScatterChartOptions;
import com.googlecode.gwt.charts.client.options.HAxis;
import com.googlecode.gwt.charts.client.options.VAxis;

public class NonUniformTraceLengthScatterPlotPanel extends HorizontalPanel
{
	private ScatterChart scatterChart;
	private DataTable dataTable;
	private ScatterChartOptions options;
	private ChartLoader chartLoader;
	private ChartRunnable chartRunnable;
	private String title;
	private String xaxisLabel;
	private String yaxisLabel;
	private String[] legend;
	private ScatterPlotTraceData[] scatterPlotTraceData;
	private int pointSize = 1;

	private String plotWidth;
	private String plotHeight;
	private boolean loaded = false;
	
	public String getTitle() {return title;}
	public String getHaxisLabel() {return xaxisLabel;}
	public String getYaxisLabel() {return yaxisLabel;}
	public String[] getLegend() {return legend;}
	public String getPlotWidth() {return plotWidth;}
	public String getPlotHeight() {return plotHeight;}
	public boolean isLoaded() {return loaded;}


	public NonUniformTraceLengthScatterPlotPanel(ScatterPlotTraceData[] scatterPlotTraceData, String title, String xaxisLabel, String yaxisLabel, String[] legend, String plotWidth, String plotHeight, int pointSize) throws Exception 
	{
		loaded = false;
		if (legend.length != scatterPlotTraceData.length) throw new Exception("Number of traces does not match legend");
		this.title = title;
		this.xaxisLabel = xaxisLabel;
		this.yaxisLabel = yaxisLabel;
		this.plotWidth = plotWidth;
		this.plotHeight = plotHeight;
		this.legend = legend;
		this.scatterPlotTraceData = scatterPlotTraceData;
		this.pointSize = pointSize;
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
		dataTable.addColumn(ColumnType.NUMBER, xaxisLabel);
		int numRows = 0;
		for (int ii = 0; ii < scatterPlotTraceData.length; ++ii)
		{
			dataTable.addColumn(ColumnType.NUMBER, legend[ii]);
			numRows = numRows + scatterPlotTraceData[ii].getNumPts();
		}
		dataTable.addRows(numRows);

		// Set options
		options = ScatterChartOptions.create();
		options.setBackgroundColor("#f0f0f0");
		options.setFontName("Tahoma");
		options.setTitle(title);
		options.setHAxis(HAxis.create(xaxisLabel));
		options.setVAxis(VAxis.create(yaxisLabel));
		options.setPointSize(pointSize);

	}
	public void draw()  
	{
		int numRows = 0;
		for (int itrace = 0; itrace < scatterPlotTraceData.length; ++itrace)
		{
			for (int ii = 0; ii < scatterPlotTraceData[itrace].getNumPts(); ++ii) 
			{
				dataTable.setValue(ii + numRows, 0, scatterPlotTraceData[itrace].getXdata()[ii]);
				dataTable.setValue(ii + numRows, itrace + 1, scatterPlotTraceData[itrace].getYdata()[ii]);
			}
			numRows = numRows + scatterPlotTraceData[itrace].getNumPts();
		}

		scatterChart.draw(dataTable, options);
	}

	private static class ChartRunnable implements Runnable
	{
		NonUniformTraceLengthScatterPlotPanel nonUniformTraceLengthScatterPlotPanel;
		private ChartRunnable(NonUniformTraceLengthScatterPlotPanel nonUniformTraceLengthScatterPlotPanel)
		{
			this.nonUniformTraceLengthScatterPlotPanel = nonUniformTraceLengthScatterPlotPanel;
		}
		@Override
		public void run() 
		{
			nonUniformTraceLengthScatterPlotPanel.scatterChart = new ScatterChart();
			nonUniformTraceLengthScatterPlotPanel.add(nonUniformTraceLengthScatterPlotPanel.scatterChart);
			nonUniformTraceLengthScatterPlotPanel.setup();
			nonUniformTraceLengthScatterPlotPanel.draw();
			nonUniformTraceLengthScatterPlotPanel.scatterChart.setWidth(nonUniformTraceLengthScatterPlotPanel.plotWidth);
			nonUniformTraceLengthScatterPlotPanel.scatterChart.setHeight(nonUniformTraceLengthScatterPlotPanel.plotHeight);
			nonUniformTraceLengthScatterPlotPanel.loaded = true;
		}
		
	}
	public static class ScatterPlotTraceData
	{
		private double[] xdata  = null;
		private double[] ydata = null;
		public double[] getXdata() {return xdata;}
		public double[] getYdata() {return ydata;}
		
		public ScatterPlotTraceData(double[] xdata, double[] ydata) throws Exception
		{
			this.xdata = xdata;
			this.ydata = ydata;
			if (xdata == null) throw new Exception("xdata not defined");
			if (ydata == null) throw new Exception("ydata not defined");
			if (xdata.length != ydata.length) throw new Exception("ydata does not match xdata");
		}
		public int getNumPts()
		{
			return xdata.length;
		}
	}
}
