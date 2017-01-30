package se.esss.litterbox.its.llrfgwt.client.bytedevice;

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
			setCheckBox();
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
			setCheckBox();
		}
	}
	private void setCheckBox()
	{
		enabledCheckBox.setEnabled(false);
		int val = Integer.parseInt(byteDevice.getValue());
		int min = Integer.parseInt(byteDevice.getMin());
		int max = Integer.parseInt(byteDevice.getMax());
		String formatStyle = "readGreen";
		if (val == 0) enabledCheckBox.setValue(false);
		if (val == 1) enabledCheckBox.setValue(true);
		if (val < min) formatStyle = "readRed";
		if (val > max) formatStyle = "readRed";
		formatter.setStyleName(displayRow, 0, formatStyle);
		formatter.setStyleName(displayRow, 2, formatStyle);
	}

}
