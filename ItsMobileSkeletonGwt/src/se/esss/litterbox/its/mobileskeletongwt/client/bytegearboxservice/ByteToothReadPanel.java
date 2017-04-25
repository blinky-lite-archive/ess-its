package se.esss.litterbox.its.mobileskeletongwt.client.bytegearboxservice;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

import se.esss.litterbox.its.mobileskeletongwt.shared.bytegearboxgwt.ByteGearGwt;

public class ByteToothReadPanel extends VerticalPanel
{
	private ByteGearGwt byteGearGwt;
	private Grid byteGearGrid;
	
	public ByteToothReadPanel(ByteGearGwt byteGearGwt) 
	{
		this.byteGearGwt = byteGearGwt;
		byteGearGrid = new Grid(byteGearGwt.getNumReadTooth() + 1,2);
		byteGearGrid.setWidget(0, 0, new Label("Name"));
		byteGearGrid.setWidget(0, 1, new Label("Value"));
		CellFormatter cellFormatter = byteGearGrid.getCellFormatter();
		for (int ii = 0; ii < byteGearGwt.getNumReadTooth(); ++ii)
		{
			Label byteToothNameLabel = new Label(byteGearGwt.getReadToothList().get(ii).getName());
			byteToothNameLabel.setTitle(byteGearGwt.getReadToothList().get(ii).getDescription());
			byteToothNameLabel.setStyleName("byteGearNotSelected");
			byteGearGrid.setWidget(ii + 1, 0, byteToothNameLabel);
			
			if (byteGearGwt.getReadToothList().get(ii).getType().equals("BOOLEAN"))
			{
				CheckBox cb = new CheckBox();
				cb.setEnabled(false);
				cb.setValue(Boolean.parseBoolean(byteGearGwt.getReadToothList().get(ii).getValue()));
				cellFormatter.setHorizontalAlignment(ii + 1, 1, HasHorizontalAlignment.ALIGN_CENTER);
				byteGearGrid.setWidget(ii + 1, 1, cb);
			}
			else
			{
				Label valueLabel = new Label(byteGearGwt.getReadToothList().get(ii).getValue());
				valueLabel.setWidth("4.0em");
				cellFormatter.setHorizontalAlignment(ii + 1, 1, HasHorizontalAlignment.ALIGN_CENTER);
				byteGearGrid.setWidget(ii + 1, 1, valueLabel);
			}

		}
		add(byteGearGrid);
	}
	public void updateReadings()
	{
		for (int ii = 0; ii < byteGearGwt.getNumReadTooth(); ++ii)
		{
			if (byteGearGwt.getReadToothList().get(ii).getType().equals("BOOLEAN"))
			{
				CheckBox cb = (CheckBox) byteGearGrid.getWidget(ii + 1, 1);
				cb.setValue(Boolean.parseBoolean(byteGearGwt.getReadToothList().get(ii).getValue()));
			}
			else
			{
				Label valueLabel = (Label) byteGearGrid.getWidget(ii + 1, 1);
				valueLabel.setText(byteGearGwt.getReadToothList().get(ii).getValue());
			}
		}
	}
}
