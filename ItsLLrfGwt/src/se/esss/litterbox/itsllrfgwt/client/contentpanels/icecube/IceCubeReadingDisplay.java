package se.esss.litterbox.itsllrfgwt.client.contentpanels.icecube;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

import se.esss.litterbox.itsllrfgwt.shared.IceCubeDevice;

public class IceCubeReadingDisplay 
{
	private IceCubeDevice iceCubeDevice = null;
	private CheckBox enabledCheckBox = new CheckBox();
	private Label readingLabel = new Label();
	private HTMLTable.CellFormatter formatter;
	private int displayRow;
	
	public IceCubeDevice getIceCubeDevice() {return iceCubeDevice;}
	public IceCubeReadingDisplay(IceCubeDevice icecubeDevice, Grid readingGrid, int displayRow) 
	{
		this.iceCubeDevice  = icecubeDevice;
		this.displayRow = displayRow;
		formatter = readingGrid.getCellFormatter();
		formatter.setHorizontalAlignment(displayRow, 1, HasHorizontalAlignment.ALIGN_CENTER);
		formatter.setVerticalAlignment(displayRow, 1, HasVerticalAlignment.ALIGN_MIDDLE);	
		readingGrid.setWidget(displayRow, 0, new Label(icecubeDevice.getName()));
		readingGrid.setWidget(displayRow, 2, new Label(icecubeDevice.getComment()));
		if (!icecubeDevice.getType().equals("bool"))
		{
			readingLabel.setText(icecubeDevice.getValue());
			readingGrid.setWidget(displayRow, 1, readingLabel);
		}
		else
		{
			enabledCheckBox.setEnabled(false);
			if (Integer.parseInt(icecubeDevice.getValue()) == 0)
			{
				enabledCheckBox.setValue(false);
				formatter.setStyleName(displayRow, 0, "readFalse");
				formatter.setStyleName(displayRow, 2, "readFalse");
			}
			else
			{
				enabledCheckBox.setValue(true);
				formatter.setStyleName(displayRow, 0, "readTrue");
				formatter.setStyleName(displayRow, 2, "readTrue");
			}
			readingGrid.setWidget(displayRow, 1, enabledCheckBox);
		}

	}
	public void updateReadingDisplayFromDevice()
	{
		if (!iceCubeDevice.getType().equals("bool"))
		{
			readingLabel.setText(iceCubeDevice.getValue());
		}
		else
		{
			enabledCheckBox.setEnabled(false);
			if (Integer.parseInt(iceCubeDevice.getValue()) == 0)
			{
				formatter.setStyleName(displayRow, 0, "readFalse");
				formatter.setStyleName(displayRow, 2, "readFalse");
				enabledCheckBox.setValue(false);
			}
			else
			{
				formatter.setStyleName(displayRow, 0, "readTrue");
				formatter.setStyleName(displayRow, 2, "readTrue");
				enabledCheckBox.setValue(true);
			}
		}
	}

}
