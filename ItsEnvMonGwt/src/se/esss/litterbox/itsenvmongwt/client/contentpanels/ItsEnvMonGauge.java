package se.esss.litterbox.itsenvmongwt.client.contentpanels;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.gauge.Gauge;
import com.googlecode.gwt.charts.client.gauge.GaugeOptions;

public class ItsEnvMonGauge extends HorizontalPanel
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
	DataTable dataTable;
	GaugeOptions options;
	ChartLoader chartLoader;
	GaugeRunnable gaugeRunnable;
	
	public ItsEnvMonGauge()
	{
	}
	public void setTitle(String title) {this.title = title;}
	public void setValue(double value) {this.value = value;}
	public void setMinMax(double minValue, double maxValue)
	{
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	public void setGreenMinMax(double minValue, double maxValue)
	{
		this.greenValueMin = minValue;
		this.greenValueMax = maxValue;
	}
	public void setYellowMinMax(double minValue, double maxValue)
	{
		this.yellowValueMin = minValue;
		this.yellowValueMax = maxValue;
	}
	public void setRedMinMax(double minValue, double maxValue)
	{
		this.redValueMin = minValue;
		this.redValueMax = maxValue;
	}

	public void initialize() 
	{
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
		ItsEnvMonGauge itsEnvMonGauge;
		private GaugeRunnable(ItsEnvMonGauge itsEnvMonGauge)
		{
			this.itsEnvMonGauge = itsEnvMonGauge;
		}
		@Override
		public void run() 
		{
			itsEnvMonGauge.gauge = new Gauge();
			itsEnvMonGauge.add(itsEnvMonGauge.gauge);
			itsEnvMonGauge.setup();
			itsEnvMonGauge.draw();
		}
		
	}
}
