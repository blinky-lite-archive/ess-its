package se.esss.litterbox.itsllrfgwt.client.contentpanels.icecube;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import se.esss.litterbox.itsllrfgwt.shared.IceCubeDevice;

public class IceCubeSettingDisplay 
{
	private IceCubeDevice iceCubeDevice = null;
	private CheckBox enabledCheckBox = new CheckBox();
	private TextBox settingTextBox = new TextBox();
	
	public IceCubeDevice getIceCubeDevice() {return iceCubeDevice;}
	public CheckBox getEnabledCheckBox() {return enabledCheckBox;}
	public TextBox getSettingTextBox() {return settingTextBox;}
	
	public IceCubeSettingDisplay(IceCubeDevice icecubeDevice, Grid settingGrid, int displayRow)
	{
		this.iceCubeDevice  = icecubeDevice;
		settingTextBox.setSize("3.0em", "1.0em");
		HTMLTable.CellFormatter formatter = settingGrid.getCellFormatter();
		formatter.setHorizontalAlignment(displayRow, 1, HasHorizontalAlignment.ALIGN_CENTER);
		formatter.setVerticalAlignment(displayRow, 1, HasVerticalAlignment.ALIGN_MIDDLE);	
		settingGrid.setWidget(displayRow, 0, new Label(icecubeDevice.getName()));
		settingGrid.setWidget(displayRow, 4, new Label(icecubeDevice.getComment()));
		if (!icecubeDevice.getType().equals("bool"))
		{
			settingGrid.setWidget(displayRow, 1, new Label(icecubeDevice.getMin()));
			settingTextBox.setText(icecubeDevice.getValue());
			settingGrid.setWidget(displayRow, 2, settingTextBox);
			settingGrid.setWidget(displayRow, 3, new Label(icecubeDevice.getMax()));
		}
		else
		{
			if (Integer.parseInt(icecubeDevice.getValue()) == 0)
			{
				enabledCheckBox.setValue(false);
			}
			else
			{
				enabledCheckBox.setValue(true);
			}
			settingGrid.setWidget(displayRow, 2, enabledCheckBox);
		}

	}
	public void updateSettingDisplayFromDevice()
	{
		if (!iceCubeDevice.getType().equals("bool"))
		{
			settingTextBox.setText(iceCubeDevice.getValue());
		}
		else
		{
			if (Integer.parseInt(iceCubeDevice.getValue()) == 0)
			{
				enabledCheckBox.setValue(false);
			}
			else
			{
				enabledCheckBox.setValue(true);
			}
		}
	}
	public void updateDeviceFromSettingDisplay()
	{
		if (iceCubeDevice.getType().equals("float"))
		{
			try
			{
				double val = Double.parseDouble(settingTextBox.getText());
				double minVal = Double.parseDouble(iceCubeDevice.getMin());
				double maxVal = Double.parseDouble(iceCubeDevice.getMax());
				if (val > maxVal) val = maxVal;
				if (val < minVal) val = minVal;
				iceCubeDevice.setValue(Double.toString(val));
				settingTextBox.setText(iceCubeDevice.getValue());
			} catch (NumberFormatException nfe)
			{
				settingTextBox.setText(iceCubeDevice.getValue());
			}
		}
		if (iceCubeDevice.getType().equals("int") || iceCubeDevice.getType().equals("byte"))
		{
			try
			{
				int val = Integer.parseInt(settingTextBox.getText());
				int minVal = Integer.parseInt(iceCubeDevice.getMin());
				int maxVal = Integer.parseInt(iceCubeDevice.getMax());
				if (val > maxVal) val = maxVal;
				if (val < minVal) val = minVal;
				iceCubeDevice.setValue(Integer.toString(val));
				settingTextBox.setText(iceCubeDevice.getValue());
			} catch (NumberFormatException nfe)
			{
				settingTextBox.setText(iceCubeDevice.getValue());
			}
		}
		if (iceCubeDevice.getType().equals("bool"))
		{
			int val = 0;
			if (enabledCheckBox.getValue()) val = 1;
			int minVal = Integer.parseInt(iceCubeDevice.getMin());
			int maxVal = Integer.parseInt(iceCubeDevice.getMax());
			if (val > maxVal) val = maxVal;
			if (val < minVal) val = minVal;
			iceCubeDevice.setValue(Integer.toString(val));
			if (Integer.parseInt(iceCubeDevice.getValue()) == 0)
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
