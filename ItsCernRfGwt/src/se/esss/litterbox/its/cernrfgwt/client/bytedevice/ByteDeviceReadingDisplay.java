package se.esss.litterbox.its.cernrfgwt.client.bytedevice;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;


public class ByteDeviceReadingDisplay 
{
	private ByteDevice byteDevice = null;
	private CheckBox enabledCheckBox = new CheckBox();
	private Label readingLabel = new Label();
	private HTMLTable.CellFormatter formatter;
	private int displayRow;
	
	public ByteDevice getByteDevice() {return byteDevice;}
	public ByteDeviceReadingDisplay(ByteDevice byteDevice, Grid readingGrid, int displayRow) 
	{
		this.byteDevice  = byteDevice;
		this.displayRow = displayRow;
		formatter = readingGrid.getCellFormatter();
		formatter.setHorizontalAlignment(displayRow, 1, HasHorizontalAlignment.ALIGN_CENTER);
		formatter.setVerticalAlignment(displayRow, 1, HasVerticalAlignment.ALIGN_MIDDLE);	
		readingGrid.setWidget(displayRow, 0, new Label(byteDevice.getName()));
		readingGrid.setWidget(displayRow, 2, new Label(byteDevice.getComment()));
		if (!byteDevice.getType().equals("bool"))
		{
			readingLabel.setText(byteDevice.getValue());
			readingGrid.setWidget(displayRow, 1, readingLabel);
		}
		else
		{
			enabledCheckBox.setEnabled(false);
			if (Integer.parseInt(byteDevice.getValue()) == 0)
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
		if (!byteDevice.getType().equals("bool"))
		{
			readingLabel.setText(byteDevice.getValue());
		}
		else
		{
			enabledCheckBox.setEnabled(false);
			if (Integer.parseInt(byteDevice.getValue()) == 0)
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
