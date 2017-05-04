package se.esss.litterbox.its.toshibagwt.client.bytegearboxservice;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

import se.esss.litterbox.its.toshibagwt.shared.bytegearboxgwt.ByteGearGwt;

public class ByteToothReadPanel extends VerticalPanel
{
	private ByteGearGwt byteGearGwt;
	private Grid byteGearGrid;
	private NumberFormat twoPlaces = NumberFormat.getFormat("#.##");
	private boolean showDesc = false;
	private boolean showType = false;
	private Button showDescButton = new Button("+");
	private Button showTypeButton = new Button("+");
	
	public ByteToothReadPanel(ByteGearGwt byteGearGwt) 
	{
		this.byteGearGwt = byteGearGwt;
		byteGearGrid = new Grid(byteGearGwt.getNumReadTooth() + 2,4);
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
		for (int ii = 0; ii < byteGearGwt.getNumReadTooth(); ++ii)
		{
			Label byteToothNameLabel = new Label(byteGearGwt.getReadToothList().get(ii).getName());
			byteToothNameLabel.setTitle(byteGearGwt.getReadToothList().get(ii).getDescription());
			byteToothNameLabel.setStyleName("byteToothName");
			byteGearGrid.setWidget(ii + 2, 0, byteToothNameLabel);
			
			if (byteGearGwt.getReadToothList().get(ii).getType().equals("BOOLEAN"))
			{
				CheckBox cb = new CheckBox();
				cb.setEnabled(false);
				cb.setValue(Boolean.parseBoolean(byteGearGwt.getReadToothList().get(ii).getValue()));
				cellFormatter.setHorizontalAlignment(ii + 2, 1, HasHorizontalAlignment.ALIGN_CENTER);
				byteGearGrid.setWidget(ii + 2, 1, cb);
			}
			else
			{
				Label valueLabel = new Label(byteGearGwt.getReadToothList().get(ii).getValue());
				String toothType = byteGearGwt.getReadToothList().get(ii).getType();
				if (toothType.equals("S7DT"))
				{
					try 
					{
						String dateString = DateTimeFormat.getFormat("yy-MM-dd HH:mm:ss").format(byteGearGwt.getReadToothList().get(ii).getDateFromStep7DateAndTime());
						valueLabel.setText(dateString);
						valueLabel.setWordWrap(true);
						valueLabel.setWidth("5.0em");
					} catch (Exception e) {GWT.log(e.getMessage());}
				}
				if (toothType.equals("FLOAT"))
				{
					valueLabel.setText(twoPlaces.format(Double.parseDouble(byteGearGwt.getReadToothList().get(ii).getValue())));
				}
				if (toothType.equals("DOUBLE"))
				{
					valueLabel.setText(twoPlaces.format(Double.parseDouble(byteGearGwt.getReadToothList().get(ii).getValue())));
				}
				cellFormatter.setHorizontalAlignment(ii + 2, 1, HasHorizontalAlignment.ALIGN_CENTER);
				byteGearGrid.setWidget(ii + 2, 1, valueLabel);
			}
			Label typeLabel = new Label(byteGearGwt.getReadToothList().get(ii).getType());
			typeLabel.setStyleName("byteToothType");
			typeLabel.setVisible(showType);
			byteGearGrid.setWidget(ii + 2, 2, typeLabel);
			cellFormatter.setHorizontalAlignment(ii + 2, 2, HasHorizontalAlignment.ALIGN_CENTER);

			Label descLabel = new Label(byteGearGwt.getReadToothList().get(ii).getDescription());
			descLabel.setStyleName("byteToothDesc");
			descLabel.setWordWrap(true);
			descLabel.setWidth("15.0em");
			descLabel.setVisible(showDesc);
			byteGearGrid.setWidget(ii + 2, 3, descLabel);
			cellFormatter.setHorizontalAlignment(ii + 2, 3, HasHorizontalAlignment.ALIGN_RIGHT);

			
		}
		add(byteGearGrid);
	}
	public void updateReadings()
	{
		for (int ii = 0; ii < byteGearGwt.getNumReadTooth(); ++ii)
		{
			
			if (byteGearGwt.getReadToothList().get(ii).getType().equals("BOOLEAN"))
			{
				CheckBox cb = (CheckBox) byteGearGrid.getWidget(ii + 2, 1);
				cb.setValue(Boolean.parseBoolean(byteGearGwt.getReadToothList().get(ii).getValue()));
			}
			else
			{
				Label valueLabel = (Label) byteGearGrid.getWidget(ii + 2, 1);
				String toothType = byteGearGwt.getReadToothList().get(ii).getType();
				valueLabel.setText(byteGearGwt.getReadToothList().get(ii).getValue());
				if (toothType.equals("S7DT"))
				{
					try 
					{
						String dateString = DateTimeFormat.getFormat("yy-MM-dd HH:mm:ss").format(byteGearGwt.getReadToothList().get(ii).getDateFromStep7DateAndTime());
						valueLabel.setText(dateString);
						valueLabel.setWordWrap(true);
						valueLabel.setWidth("5.0em");
					} catch (Exception e) {GWT.log(e.getMessage());}
				}
				if (toothType.equals("FLOAT"))
				{
					valueLabel.setText(twoPlaces.format(Double.parseDouble(byteGearGwt.getReadToothList().get(ii).getValue())));
				}
				if (toothType.equals("DOUBLE"))
				{
					valueLabel.setText(twoPlaces.format(Double.parseDouble(byteGearGwt.getReadToothList().get(ii).getValue())));
				}
			}
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
				for (int ii = 0; ii < byteGearGwt.getNumReadTooth(); ++ii)
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
				for (int ii = 0; ii < byteGearGwt.getNumReadTooth(); ++ii)
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
