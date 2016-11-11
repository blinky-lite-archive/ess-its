package se.esss.litterbox.itsllrfgwt.client.contentpanels.icecube;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

import se.esss.litterbox.itsllrfgwt.shared.IceCubeDeviceList;

public class IceCubeReadingDisplayListCaptionPanel extends CaptionPanel
{
	private ArrayList<IceCubeReadingDisplay>  readingDeviceDisplayList = new ArrayList<IceCubeReadingDisplay>();
	int numDevices = 0;

	public IceCubeReadingDisplayListCaptionPanel(String title, IceCubeDeviceList readingDeviceList, int startRow, int endRow) throws Exception 
	{
		super(title);
		for (int ii = startRow; ii <= endRow; ++ ii) 
			if (!readingDeviceList.getDevice(ii).getName().equals("/")) numDevices = numDevices + 1;
		Grid readingGrid = new Grid(numDevices + 1, 3);
		int idevice = 0;
		for (int ii = startRow; ii <= endRow; ++ ii) 
		{
			if (!readingDeviceList.getDevice(ii).getName().equals("/"))
			{
				readingDeviceDisplayList.add(new IceCubeReadingDisplay(readingDeviceList.getDevice(ii), readingGrid, idevice + 1));
				idevice = idevice + 1;
			}
		}
		readingGrid.setWidget(0, 0, new Label("Name"));
		readingGrid.setWidget(0, 1, new Label("Value"));
		readingGrid.setWidget(0, 2, new Label("Comment"));
		
		HTMLTable.CellFormatter formatter = readingGrid.getCellFormatter();
		formatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
		formatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);
		add(readingGrid);
	}
	public void updateReadingsDisplayFromDevices()
	{
		for (int ii = 0; ii < readingDeviceDisplayList.size(); ++ii)
		{
			readingDeviceDisplayList.get(ii).updateReadingDisplayFromDevice();
		}
	}

}
