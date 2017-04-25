package se.esss.litterbox.its.toshibagwt.client.bytegearboxservice;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

import se.esss.litterbox.its.toshibagwt.client.gskel.GskelSettingButtonGrid;
import se.esss.litterbox.its.toshibagwt.shared.bytegearboxgwt.ByteGearGwt;

public class ByteToothWritePanel extends VerticalPanel
{
	private ByteGearGwt byteGearGwt;
	private Grid byteGearGrid;
	ByteGearBoxData byteGearBoxData;
	SettingButtonGrid settingButtonGrid;
	
	public ByteToothWritePanel(ByteGearGwt byteGearGwt, ByteGearBoxData byteGearBoxData) 
	{
		this.byteGearGwt = byteGearGwt;
		this.byteGearBoxData = byteGearBoxData;
		byteGearGrid = new Grid(byteGearGwt.getNumWriteTooth() + 1,2);
		byteGearGrid.setWidget(0, 0, new Label("Name"));
		byteGearGrid.setWidget(0, 1, new Label("Value"));
		CellFormatter cellFormatter = byteGearGrid.getCellFormatter();
		for (int ii = 0; ii < byteGearGwt.getNumWriteTooth(); ++ii)
		{
			Label byteToothNameLabel = new Label(byteGearGwt.getWriteToothList().get(ii).getName());
			byteToothNameLabel.setTitle(byteGearGwt.getWriteToothList().get(ii).getDescription());
			byteToothNameLabel.setStyleName("byteGearNotSelected");
			byteGearGrid.setWidget(ii + 1, 0, byteToothNameLabel);
			
			if (byteGearGwt.getWriteToothList().get(ii).getType().equals("BOOLEAN"))
			{
				CheckBox cb = new CheckBox();
				cb.setEnabled(false);
				cb.setValue(Boolean.parseBoolean(byteGearGwt.getWriteToothList().get(ii).getValue()));
				byteGearGrid.setWidget(ii + 1, 1, cb);
				cellFormatter.setHorizontalAlignment(ii + 1, 1, HasHorizontalAlignment.ALIGN_CENTER);
			}
			else
			{
				TextBox valueTextBox = new TextBox();
				valueTextBox.setWidth("4.0em");
				valueTextBox.setHeight("0.5em");
				valueTextBox.setAlignment(TextAlignment.CENTER);
				valueTextBox.setText(byteGearGwt.getWriteToothList().get(ii).getValue());
				cellFormatter.setHorizontalAlignment(ii + 1, 1, HasHorizontalAlignment.ALIGN_CENTER);
				byteGearGrid.setWidget(ii + 1, 1, valueTextBox);
			}

		}
		settingButtonGrid = new SettingButtonGrid(byteGearBoxData.getEntryPointApp().getSetup().isSettingsPermitted());
		add(settingButtonGrid);
		add(byteGearGrid);
	}
	public void updateReadings()
	{
		if (settingButtonGrid.isSettingsEnabled()) return;
		for (int ii = 0; ii < byteGearGwt.getNumWriteTooth(); ++ii)
		{
			if (byteGearGwt.getWriteToothList().get(ii).getType().equals("BOOLEAN"))
			{
				CheckBox cb = (CheckBox) byteGearGrid.getWidget(ii + 1, 1);
				cb.setValue(Boolean.parseBoolean(byteGearGwt.getWriteToothList().get(ii).getValue()));
			}
			else
			{
				TextBox valueTextBox = (TextBox) byteGearGrid.getWidget(ii + 1, 1);
				valueTextBox.setText(byteGearGwt.getWriteToothList().get(ii).getValue());
			}
		}
	}
	private void enableInput(boolean enableInput)
	{
		for (int ii = 0; ii < byteGearGwt.getNumWriteTooth(); ++ii)
		{
			if (byteGearGwt.getWriteToothList().get(ii).getType().equals("BOOLEAN"))
			{
				CheckBox cb = (CheckBox) byteGearGrid.getWidget(ii + 1, 1);
				cb.setEnabled(enableInput);
			}
			else
			{
				TextBox valueTextBox = (TextBox) byteGearGrid.getWidget(ii + 1, 1);
				valueTextBox.setEnabled(enableInput);
			}
		}
	}
	private void setByteTooth()
	{
		for (int ii = 0; ii < byteGearGwt.getNumWriteTooth(); ++ii)
		{
			if (byteGearGwt.getWriteToothList().get(ii).getType().equals("BOOLEAN"))
			{
				CheckBox cb = (CheckBox) byteGearGrid.getWidget(ii + 1, 1);
				byteGearGwt.getWriteToothList().get(ii).setValue(Boolean.toString(cb.getValue()));
			}
			else
			{
				TextBox valueTextBox = (TextBox) byteGearGrid.getWidget(ii + 1, 1);
				byteGearGwt.getWriteToothList().get(ii).setValue(valueTextBox.getText());
			}
		}
		byteGearBoxData.setWriteData();
	}
	class SettingButtonGrid extends GskelSettingButtonGrid
	{

		public SettingButtonGrid(boolean settingsPermitted) 
		{
			super(settingsPermitted);
		}

		@Override
		public void enableSettingsInput(boolean enabled) 
		{
			enableInput(enabled);
		}

		@Override
		public void doSettings() 
		{
			setByteTooth();
		}
		
	}
}
