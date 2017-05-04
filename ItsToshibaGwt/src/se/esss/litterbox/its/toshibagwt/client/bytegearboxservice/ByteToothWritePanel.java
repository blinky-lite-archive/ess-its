package se.esss.litterbox.its.toshibagwt.client.bytegearboxservice;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

import se.esss.litterbox.its.toshibagwt.client.gskel.GskelSettingButtonGrid;
import se.esss.litterbox.its.toshibagwt.shared.bytegearboxgwt.ByteGearGwt;

import com.google.gwt.user.client.ui.VerticalPanel;

public class ByteToothWritePanel extends VerticalPanel
{
	private ByteGearGwt byteGearGwt;
	private Grid byteGearGrid;
	ByteGearBoxData byteGearBoxData;
	SettingButtonGrid settingButtonGrid;
	private NumberFormat twoPlaces = NumberFormat.getFormat("#.##");
	private boolean showDesc = false;
	private boolean showType = false;
	private Button showDescButton = new Button("+");
	private Button showTypeButton = new Button("+");
	
	public ByteToothWritePanel(ByteGearGwt byteGearGwt, ByteGearBoxData byteGearBoxData) 
	{
		this.byteGearGwt = byteGearGwt;
		this.byteGearBoxData = byteGearBoxData;
		byteGearGrid = new Grid(byteGearGwt.getNumWriteTooth() + 2,4);
		byteGearGrid.setWidget(1, 0, new Label("Name"));
		byteGearGrid.setWidget(1, 1, new Label("Value"));
		byteGearGrid.setWidget(1, 2, new Label("Type"));
		byteGearGrid.setWidget(1, 3, new Label("Desc"));
		byteGearGrid.getWidget(1, 2).setVisible(showType);
		byteGearGrid.getWidget(1, 3).setVisible(showDesc);
		byteGearGrid.setWidget(0, 2, showTypeButton);
		byteGearGrid.setWidget(0, 3, showDescButton);
		showDescButton.addClickHandler(new ShowButtonClickHandler("desc"));
		showTypeButton.addClickHandler(new ShowButtonClickHandler("type"));

		CellFormatter cellFormatter = byteGearGrid.getCellFormatter();
		for (int ii = 0; ii < 4; ++ii) cellFormatter.setHorizontalAlignment(0, ii, HasHorizontalAlignment.ALIGN_CENTER);
		for (int ii = 0; ii < 4; ++ii) cellFormatter.setHorizontalAlignment(1, ii, HasHorizontalAlignment.ALIGN_CENTER);
		for (int ii = 0; ii < byteGearGwt.getNumWriteTooth(); ++ii)
		{
			Label byteToothNameLabel = new Label(byteGearGwt.getWriteToothList().get(ii).getName());
			byteToothNameLabel.setTitle(byteGearGwt.getWriteToothList().get(ii).getDescription());
			byteToothNameLabel.setStyleName("byteGearNotSelected");
			byteToothNameLabel.setStyleName("byteToothName");
			byteGearGrid.setWidget(ii + 2, 0, byteToothNameLabel);
			
			if (byteGearGwt.getWriteToothList().get(ii).getType().equals("BOOLEAN"))
			{
				CheckBox cb = new CheckBox();
				cb.setEnabled(false);
				cb.setValue(Boolean.parseBoolean(byteGearGwt.getWriteToothList().get(ii).getValue()));
				byteGearGrid.setWidget(ii + 2, 1, cb);
				cellFormatter.setHorizontalAlignment(ii + 2, 1, HasHorizontalAlignment.ALIGN_CENTER);
			}
			else
			{
				TextBox valueTextBox = new TextBox();
				valueTextBox.setWidth("4.0em");
				valueTextBox.setHeight("0.5em");
				valueTextBox.setAlignment(TextAlignment.CENTER);
				valueTextBox.setText(byteGearGwt.getWriteToothList().get(ii).getValue());
				cellFormatter.setHorizontalAlignment(ii + 1, 1, HasHorizontalAlignment.ALIGN_CENTER);
				byteGearGrid.setWidget(ii + 2, 1, valueTextBox);
			}

			Label typeLabel = new Label(byteGearGwt.getWriteToothList().get(ii).getType());
			typeLabel.setStyleName("byteToothType");
			typeLabel.setVisible(showType);
			byteGearGrid.setWidget(ii + 2, 2, typeLabel);
			cellFormatter.setHorizontalAlignment(ii + 2, 2, HasHorizontalAlignment.ALIGN_CENTER);

			Label descLabel = new Label(byteGearGwt.getWriteToothList().get(ii).getDescription());
			descLabel.setStyleName("byteToothDesc");
			descLabel.setWordWrap(true);
			descLabel.setWidth("15.0em");
			descLabel.setVisible(showDesc);
			byteGearGrid.setWidget(ii + 2, 3, descLabel);
			cellFormatter.setHorizontalAlignment(ii + 2, 3, HasHorizontalAlignment.ALIGN_RIGHT);
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
				CheckBox cb = (CheckBox) byteGearGrid.getWidget(ii + 2, 1);
				cb.setValue(Boolean.parseBoolean(byteGearGwt.getWriteToothList().get(ii).getValue()));
			}
			else
			{
				String toothType = byteGearGwt.getWriteToothList().get(ii).getType();
				TextBox valueTextBox = (TextBox) byteGearGrid.getWidget(ii + 2, 1);
				valueTextBox.setText(byteGearGwt.getWriteToothList().get(ii).getValue());
				if (toothType.equals("FLOAT"))
				{
					valueTextBox.setText(twoPlaces.format(Double.parseDouble(byteGearGwt.getWriteToothList().get(ii).getValue())));
				}
				if (toothType.equals("DOUBLE"))
				{
					valueTextBox.setText(twoPlaces.format(Double.parseDouble(byteGearGwt.getWriteToothList().get(ii).getValue())));
				}
			}
		}
	}
	private void enableInput(boolean enableInput)
	{
		for (int ii = 0; ii < byteGearGwt.getNumWriteTooth(); ++ii)
		{
			if (byteGearGwt.getWriteToothList().get(ii).getType().equals("BOOLEAN"))
			{
				CheckBox cb = (CheckBox) byteGearGrid.getWidget(ii + 2, 1);
				cb.setEnabled(enableInput);
			}
			else
			{
				String toothType = byteGearGwt.getWriteToothList().get(ii).getType();
				TextBox valueTextBox = (TextBox) byteGearGrid.getWidget(ii + 2, 1);
				valueTextBox.setEnabled(enableInput);
				if (toothType.equals("FLOAT"))
				{
					valueTextBox.setText(twoPlaces.format(Double.parseDouble(byteGearGwt.getWriteToothList().get(ii).getValue())));
				}
				if (toothType.equals("DOUBLE"))
				{
					valueTextBox.setText(twoPlaces.format(Double.parseDouble(byteGearGwt.getWriteToothList().get(ii).getValue())));
				}
			}
		}
	}
	private void setByteTooth()
	{
		for (int ii = 0; ii < byteGearGwt.getNumWriteTooth(); ++ii)
		{
			if (byteGearGwt.getWriteToothList().get(ii).getType().equals("BOOLEAN"))
			{
				CheckBox cb = (CheckBox) byteGearGrid.getWidget(ii + 2, 1);
				byteGearGwt.getWriteToothList().get(ii).setValue(Boolean.toString(cb.getValue()));
			}
			else
			{
				TextBox valueTextBox = (TextBox) byteGearGrid.getWidget(ii + 2, 1);
				byteGearGwt.getWriteToothList().get(ii).setValue(valueTextBox.getText());
			}
		}
		try
		{
			byteGearGwt.getWriteByteTooth("WR_DATA").setValue("true");
			byteGearBoxData.setWriteData(byteGearGwt);
		}catch (Exception e) {GWT.log(e.getMessage());}
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
	class ShowButtonClickHandler implements ClickHandler
	{
		String columnType;
		ShowButtonClickHandler(String columnType)
		{
			this.columnType = columnType;
		}
		@Override
		public void onClick(ClickEvent event) 
		{
			if (columnType.equals("type"))
			{
				showType = !showType;
				byteGearGrid.getWidget(1, 2).setVisible(showType);;
				for (int ii = 0; ii < byteGearGwt.getNumWriteTooth(); ++ii)
				{
					byteGearGrid.getWidget(ii + 2, 2).setVisible(showType);
				}
				if (showType)
				{
					showTypeButton.setText("-");
				}
				else
				{
					showTypeButton.setText("+");
				}
			}
			if (columnType.equals("desc"))
			{
				showDesc = !showDesc;
				byteGearGrid.getWidget(1, 3).setVisible(showType);;
				for (int ii = 0; ii < byteGearGwt.getNumWriteTooth(); ++ii)
				{
					byteGearGrid.getWidget(ii + 2, 3).setVisible(showDesc);
				}
				if (showDesc)
				{
					showDescButton.setText("-");
				}
				else
				{
					showDescButton.setText("+");
				}
			}
			
		}
	}
}
