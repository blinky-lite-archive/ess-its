package se.esss.litterbox.its.dashboardgwt.client.googleplots;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.gauge.Gauge;
import com.googlecode.gwt.charts.client.gauge.GaugeOptions;

public class GaugePlotPanel extends HorizontalPanel
{
	private Gauge gauge;
	private String title = "setting";
	private double minValue = 0;
	private double maxValue = 100;
	private double value = 80;
	private double greenValueMax = 40;
	private double greenValueMin = 0;
	private double yellowValueMax = 80;
	private double yellowValueMin = 40;
	private double redValueMax = 100;
	private double redValueMin = 80;
	private DataTable dataTable;
	private GaugeOptions options;
	private ChartLoader chartLoader;
	private GaugeRunnable gaugeRunnable;
	private boolean loaded = false;
	private String plotWidth;
	private String plotHeight;
	
	public boolean isLoaded() {return loaded;}
	
	public GaugePlotPanel(String title, double min, double max, double greenMin, double greenMax, double yellowMin, double yellowMax, double redMin, double redMax, String plotWidth, String plotHeight)
	{
		loaded = false;
		this.plotWidth = plotWidth;
		this.plotHeight = plotHeight;
		this.minValue = min;
		this.maxValue = max;
		this.greenValueMin = greenMin;
		this.greenValueMax = greenMax;
		this.yellowValueMin = yellowMin;
		this.yellowValueMax = yellowMax;
		this.redValueMin = redMin;
		this.redValueMax = redMax;
		this.title = title;
	}
	public void setValue(double value) {this.value = value;}
	public void setGaugeVisible(boolean visible)
	{
		if (visible) gauge.setSize(plotWidth, plotHeight);
		gauge.setVisible(visible);
	}

	public void initialize() 
	{
		loaded = false;
		chartLoader = new ChartLoader(ChartPackage.GAUGE);
		gaugeRunnable = new GaugeRunnable(this);
		chartLoader.loadApi(gaugeRunnable);
	
	}
	private void setup()
	{
		// Prepare the data
		dataTable = DataTable.create();
		dataTable.addColumn(ColumnType.STRING, "LabelStyle");
		dataTable.addColumn(ColumnType.NUMBER, "Value");
		dataTable.addRows(1);
		dataTable.setValue(0, 0, title);
		
		options = GaugeOptions.create();
		options.setMin(minValue);
		options.setMax(maxValue);
		options.setGreenFrom(greenValueMin);
		options.setGreenTo(greenValueMax);
		options.setYellowFrom(yellowValueMin);
		options.setYellowTo(yellowValueMax);
		options.setRedFrom(redValueMin);
		options.setRedTo(redValueMax);
	}
	public void draw() 
	{
		dataTable.setValue(0, 1, value);
		// Draw the chart
		gauge.draw(dataTable, options);
	}
	private static class GaugeRunnable implements Runnable
	{
		GaugePlotPanel gaugePlotPanel;
		private GaugeRunnable(GaugePlotPanel gaugePlotPanel)
		{
			this.gaugePlotPanel = gaugePlotPanel;
		}
		@Override
		public void run() 
		{
			gaugePlotPanel.gauge = new Gauge();
			gaugePlotPanel.gauge.setVisible(false);
			gaugePlotPanel.gauge.setSize("10px", "10px");
			gaugePlotPanel.add(gaugePlotPanel.gauge);
			gaugePlotPanel.setHorizontalAlignment(ALIGN_CENTER);
			gaugePlotPanel.setup();
			gaugePlotPanel.draw();
			gaugePlotPanel.setWidth(gaugePlotPanel.plotWidth);
			gaugePlotPanel.setHeight(gaugePlotPanel.plotHeight);
			gaugePlotPanel.loaded = true;
		}
		
	}
}
