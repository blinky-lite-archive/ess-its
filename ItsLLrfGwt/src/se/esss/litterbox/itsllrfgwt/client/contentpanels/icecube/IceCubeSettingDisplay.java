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
	private IceCubeDevice icecubeDevice = null;
	private CheckBox enabledCheckBox = new CheckBox();
	private TextBox settingTextBox = new TextBox();
	
	public IceCubeSettingDisplay(IceCubeDevice icecubeDevice, Grid settingGrid, int displayRow)
	{
		this.icecubeDevice  = icecubeDevice;
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
	public void refreshSetting()
	{
		if (!icecubeDevice.getType().equals("bool"))
		{
			settingTextBox.setText(icecubeDevice.getValue());
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
		}
	}
}
