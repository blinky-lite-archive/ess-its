package se.esss.litterbox.its.cernrfgwt.client.bytedevice;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class ByteDeviceSettingDisplay 
{
	private ByteDevice byteDevice = null;
	private CheckBox enabledCheckBox = new CheckBox();
	private TextBox settingTextBox = new TextBox();
	private Label settingReadingLabel = new Label();
	
	public ByteDevice getByteDevice() {return byteDevice;}
	public CheckBox getEnabledCheckBox() {return enabledCheckBox;}
	public TextBox getSettingTextBox() {return settingTextBox;}
	public Label getSettingReadingLabel() {return settingReadingLabel;}
	
	public ByteDeviceSettingDisplay(ByteDevice byteDevice, Grid settingGrid, int displayRow)
	{
		this.byteDevice  = byteDevice;
		settingTextBox.setSize("3.0em", "1.0em");
		settingReadingLabel.setStyleName("readings");
		HTMLTable.CellFormatter formatter = settingGrid.getCellFormatter();
		formatter.setHorizontalAlignment(displayRow, 1, HasHorizontalAlignment.ALIGN_CENTER);
		formatter.setVerticalAlignment(displayRow, 1, HasVerticalAlignment.ALIGN_MIDDLE);	
		settingGrid.setWidget(displayRow, 0, new Label(byteDevice.getName()));
		settingGrid.setWidget(displayRow, 5, new Label(byteDevice.getComment()));
		if (!byteDevice.getType().equals("bool"))
		{
			settingGrid.setWidget(displayRow, 1, new Label(byteDevice.getMin()));
			settingTextBox.setText(byteDevice.getValue());
			settingGrid.setWidget(displayRow, 2, settingTextBox);
			settingReadingLabel.setText(byteDevice.getValue());
			settingGrid.setWidget(displayRow, 3, settingReadingLabel);
			settingGrid.setWidget(displayRow, 4, new Label(byteDevice.getMax()));
		}
		else
		{
			if (Integer.parseInt(byteDevice.getValue()) == 0)
			{
				enabledCheckBox.setValue(false);
				settingReadingLabel.setText("false");
			}
			else
			{
				enabledCheckBox.setValue(true);
				settingReadingLabel.setText("true");
			}
			settingGrid.setWidget(displayRow, 2, enabledCheckBox);
			settingGrid.setWidget(displayRow, 3, settingReadingLabel);
		}

	}
	public void updateSettingReadbackFromDevice()
	{
		if (!byteDevice.getType().equals("bool"))
		{
//			settingTextBox.setText(iceCubeDevice.getValue());
			settingReadingLabel.setText(byteDevice.getValue());
		}
		else
		{
			if (Integer.parseInt(byteDevice.getValue()) == 0)
			{
//				enabledCheckBox.setValue(false);
				settingReadingLabel.setText("false");
			}
			else
			{
//				enabledCheckBox.setValue(true);
				settingReadingLabel.setText("true");
			}
		}
	}
	public void checkReadingMatchSetting()
	{
		if (!byteDevice.getType().equals("bool"))
		{
			if (settingTextBox.getText().equals(settingReadingLabel.getText()))
			{
				settingReadingLabel.setStyleName("readings");
			}
			else
			{
				settingReadingLabel.setStyleName("readingMismatch");
			}
		}
		else
		{
		}
	}
	public void updateSettingFromReadback()
	{
		if (!byteDevice.getType().equals("bool"))
		{
			settingTextBox.setText(settingReadingLabel.getText()); 
		}
		else
		{
			if (settingReadingLabel.getText().equals("true")) enabledCheckBox.setValue(true);
			if (settingReadingLabel.getText().equals("false")) enabledCheckBox.setValue(false);
		}
	}
	public void updateDeviceFromSettingDisplay()
	{
		if (byteDevice.getType().equals("float"))
		{
			try
			{
				double val = Double.parseDouble(settingTextBox.getText());
				double minVal = Double.parseDouble(byteDevice.getMin());
				double maxVal = Double.parseDouble(byteDevice.getMax());
				if (val > maxVal) val = maxVal;
				if (val < minVal) val = minVal;
				byteDevice.setValue(Double.toString(val));
				settingTextBox.setText(byteDevice.getValue());
			} catch (NumberFormatException nfe)
			{
				settingTextBox.setText(byteDevice.getValue());
			}
		}
		if (byteDevice.getType().equals("int") || byteDevice.getType().equals("byte"))
		{
			try
			{
				int val = Integer.parseInt(settingTextBox.getText());
				int minVal = Integer.parseInt(byteDevice.getMin());
				int maxVal = Integer.parseInt(byteDevice.getMax());
				if (val > maxVal) val = maxVal;
				if (val < minVal) val = minVal;
				byteDevice.setValue(Integer.toString(val));
				settingTextBox.setText(byteDevice.getValue());
			} catch (NumberFormatException nfe)
			{
				settingTextBox.setText(byteDevice.getValue());
			}
		}
		if (byteDevice.getType().equals("bool"))
		{
			int val = 0;
			if (enabledCheckBox.getValue()) val = 1;
			int minVal = Integer.parseInt(byteDevice.getMin());
			int maxVal = Integer.parseInt(byteDevice.getMax());
			if (val > maxVal) val = maxVal;
			if (val < minVal) val = minVal;
			byteDevice.setValue(Integer.toString(val));
			if (Integer.parseInt(byteDevice.getValue()) == 0)
			{
				enabledCheckBox.setValue(false);
			}
			else
			{
				enabledCheckBox.setValue(true);
			}
		}
	}
}
