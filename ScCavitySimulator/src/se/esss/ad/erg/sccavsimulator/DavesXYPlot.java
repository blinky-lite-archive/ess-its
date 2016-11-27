package se.esss.ad.erg.sccavsimulator;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.UIManager;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class DavesXYPlot extends XYSeriesCollection
{
	private static final long serialVersionUID = 2040219133061903217L;
	private String chartTitle = "Title";
	private String xAxisTitle = "X";
	private String yAxisTitle = "Y";
	private boolean shapesOn = false;
	private ChartPanel chartPanel = null;
	
	public DavesXYPlot()
	{
		super();
		
	}
	public class DavesXYPlotException extends Exception
	{
		private static final long serialVersionUID = 3797205755067060614L;
		public DavesXYPlotException(String smessage) {super(smessage);}
		public DavesXYPlotException(String smessage, Throwable cause) {super(smessage, cause);}
	}
	public void addSeries(String title, double[] xData, double[] yData) throws DavesXYPlotException
	{
    	int npts = xData.length;
    	if (yData.length != npts) throw new DavesXYPlotException("Ydata length does not match Xdata length");
    	XYSeries series = new XYSeries(title);
    	series.setDescription(title);
    	for (int ii = 0; ii < npts; ++ii)
    	{
    		series.add(xData[ii], yData[ii]);
    	}
    	addSeries(series);
	}
	public void updateSeries(String title, double[] xData, double[] yData) throws DavesXYPlotException
	{
		int iseries = -1;
		for (int ii = 0; ii < getSeriesCount(); ++ii)
		{
			if (title.equals(getSeries(ii).getDescription())) iseries = ii;
		}
		if (iseries < 0) throw new DavesXYPlotException("Can't find series description");
    	int npts = yData.length;
    	if (yData.length != xData.length) throw new DavesXYPlotException("Ydata length does not match Xdata length");
		getSeries(iseries).clear();
    	for (int ii = 0; ii < npts; ++ii)
    	{
    		getSeries(iseries).add(xData[ii], yData[ii]);
    	}
  	}
    private JFreeChart createChart() 
    {
        JFreeChart chart = ChartFactory.createXYLineChart(
        		chartTitle,      // chart title
        		xAxisTitle,                      // x axis label
        		yAxisTitle,                      // y axis label
        		this,                  // data
        		PlotOrientation.VERTICAL,
        		true,                     // include legend
        		true,                     // tooltips
        		false                     // urls
        );

        // get a reference to the plot for further customisation...
        XYPlot plot = (XYPlot) chart.getPlot();

        XYLineAndShapeRenderer renderer
                = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseShapesVisible(shapesOn);
        renderer.setBaseShapesFilled(shapesOn);

        // change the auto tick unit selection to integer units only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.gray);
        plot.setDomainGridlinePaint(Color.gray);
        return chart;

    }
    public void createChartPanel(int xSize, int ySize)
    {
    	JFreeChart chart = createChart();
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(xSize, ySize));
        chartPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(chart.getTitle().getText()),BorderFactory.createEmptyBorder(5,5,5,5)));
        return;
    }
    public  JFrame createChartJFrame(String frameTitle)
    {
		JFrame jframe = new JFrame(frameTitle);
        try 
        {
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {}
        jframe.setContentPane(chartPanel);
        jframe.pack();
        jframe.setVisible(true);
        return jframe;
    }
	public void setChartTitle(String chartTitle) {this.chartTitle = chartTitle;}
	public void setxAxisTitle(String xAxisTitle) {this.xAxisTitle = xAxisTitle;}
	public void setyAxisTitle(String yAxisTitle) {this.yAxisTitle = yAxisTitle;}
	public void setShapesOn(boolean shapesOn) {this.shapesOn = shapesOn;}

	public String getChartTitle() {return chartTitle;}
	public String getxAxisTitle() {return xAxisTitle;}
	public String getyAxisTitle() {return yAxisTitle;}
	public boolean isShapesOn() {return shapesOn;}
	public ChartPanel getChartPanel() {return chartPanel;}
	
	public static void main(String[] args) 
	{

	}

}
