package se.esss.litterbox.itsenvmongwt.client.contentpanels;

import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

public class ReadingsCaptionPanel extends CaptionPanel
{
	private Label[][] readings = new Label[6][2];
	public ReadingsCaptionPanel()
	{
		super("Readings");
		for (int ii = 0; ii < readings.length; ++ii)
		{
			readings[ii][0] = new Label("Name ");
			readings[ii][1] = new Label("Value");
		}
		Grid readGrid = new Grid(7, 2);
		HTMLTable.CellFormatter formatter = readGrid.getCellFormatter();
		formatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		formatter.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);	
		formatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		formatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);	
		for (int ii = 0; ii < readings.length; ++ii)
		{
			formatter.setHorizontalAlignment(ii + 1, 0, HasHorizontalAlignment.ALIGN_LEFT);
			formatter.setVerticalAlignment(ii + 1, 0, HasVerticalAlignment.ALIGN_MIDDLE);
			formatter.setHorizontalAlignment(ii + 1, 1, HasHorizontalAlignment.ALIGN_RIGHT);
			formatter.setVerticalAlignment(ii + 1, 1, HasVerticalAlignment.ALIGN_MIDDLE);
		}
		readGrid.setWidget(0, 0, new Label("Name "));
		readGrid.setWidget(0, 1, new Label("Value"));
		String labelWidth = "6.0em";
		for (int ii = 0; ii < readings.length; ++ii)
		{
			readings[ii][0].setWidth(labelWidth);
			readings[ii][1].setWidth(labelWidth);
			readings[ii][1].setStyleName("readings");
			readGrid.setWidget(ii + 1, 0, readings[ii][0]);
			readGrid.setWidget(ii + 1, 1, readings[ii][1]);
		}
		add(readGrid);
		setWidth("14.0em");
	}
	public void updateReadings(String[][] readingfromServer)
	{
		for (int ii = 0; ii < readingfromServer.length; ++ii)
		{
			readings[ii][0].setText(readingfromServer[ii][0]);
			readings[ii][1].setText(readingfromServer[ii][1]);
		}
	}
}
