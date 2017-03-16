package se.esss.litterbox.its.raspiwebcamgwt.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

import se.esss.litterbox.its.raspiwebcamgwt.client.contentpanels.RaspiCamImagePanel;
import se.esss.litterbox.its.raspiwebcamgwt.client.gskel.GskelSetupApp;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EntryPointApp implements EntryPoint 
{
	private GskelSetupApp setup;
	private Label dateLabel = new Label(new Date().toString());
	private RaspiCamImagePanel raspiCamImagePanel;

	// Getters
	public GskelSetupApp getSetup() {return setup;}
	public Label getDateLabel() {return dateLabel;}
	public RaspiCamImagePanel getRaspiCamImagePanel() {return raspiCamImagePanel;}
	
	public void onModuleLoad() 
	{
		setup = new GskelSetupApp(this, false);
		getSetup().setLogoImage("images/gwtLogo.jpg");
		getSetup().getTitlePanel().setWidth("100%");
		
		Label titleLabel = new Label("ITS Nosey!");
		titleLabel.setStyleName("titleLabel");
		titleLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		dateLabel.setStyleName("dateLabel");
		dateLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Grid titleGrid = new Grid(1,2);
		titleGrid.setWidth("100%");
		VerticalPanel titleCell = new VerticalPanel();
		titleCell.setWidth("100%");
		titleCell.add(titleLabel);
		titleCell.add(dateLabel);
		Image logoImage = new Image("images/gwtLogo.jpg");
		logoImage.setSize("73px", "40px");
		titleGrid.setWidget(0, 0, titleCell);
		titleGrid.setWidget(0, 1, logoImage);
		CellFormatter titleGridCellFormatter = titleGrid.getCellFormatter();
		titleGridCellFormatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		titleGridCellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		
		getSetup().getTitlePanel().add(titleGrid);

		initializeTabs();
		getSetup().resize();
	}
	public void initializeTabs()
	{
		raspiCamImagePanel = new RaspiCamImagePanel(this);
		getSetup().addPanel(raspiCamImagePanel);
	}
}
