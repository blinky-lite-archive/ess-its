package se.esss.litterbox.its.watersystem2gwt.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import se.esss.litterbox.its.watersystem2gwt.client.contentpanels.WaterGaugePanel;
import se.esss.litterbox.its.watersystem2gwt.client.gskel.GskelSetupApp;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EntryPointApp implements EntryPoint 
{
	private GskelSetupApp setup;
	private Label dateLabel = new Label(new Date().toString());

	public Label getDateLabel() {return dateLabel;}

	// Getters
	public GskelSetupApp getSetup() {return setup;}
	
	public void onModuleLoad() 
	{
		setup = new GskelSetupApp(this, false);
		getSetup().setLogoImage("images/gwtLogo.jpg");
		
		Label titleLabel = new Label("ITS Thirsty!");
		titleLabel.setStyleName("titleLabel");
		dateLabel.setStyleName("dateLabel");
		Grid titleGrid = new Grid(1,2);
		VerticalPanel titleCell = new VerticalPanel();
		titleCell.setWidth("932px");
		titleCell.add(titleLabel);
		titleCell.add(dateLabel);
		Image logoImage = new Image("images/gwtLogo.jpg");
		logoImage.setSize("73px", "40px");
		titleGrid.setWidget(0, 0, titleCell);
		titleGrid.setWidget(0, 1, logoImage);
		CellFormatter titleGridCellFormatter = titleGrid.getCellFormatter();
		titleGridCellFormatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		titleGridCellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		
		getSetup().getTitlePanel().setWidth("1005px");
		getSetup().getTitlePanel().add(titleGrid);
//		getSetup().getEntryPointAppService().checkIpAddress(new CheckIpAddresslAsyncCallback(this));		
		getSetup().resize();
		initializeTabs();
	}
	public void initializeTabs()
	{
		WaterGaugePanel wgp = new WaterGaugePanel(this);
		setup.addPanel(wgp);
	}
}
