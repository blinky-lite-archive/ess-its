package se.esss.litterbox.its.dashboardgwt.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import se.esss.litterbox.its.dashboardgwt.client.contentpanels.GaugeDashboard;
import se.esss.litterbox.its.dashboardgwt.client.mqttdata.MqttService;
import se.esss.litterbox.its.dashboardgwt.client.mqttdata.MqttServiceAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EntryPointApp implements EntryPoint 
{
	public final MqttServiceAsync mqttService = GWT.create(MqttService.class);
	public final EntryPointAppServiceAsync entryPointAppService = GWT.create(EntryPointAppService.class);
	Label dateLabel;
	Label statusLabel = new Label("");
	public double klystronPower = 0;
	public double dutyFactor = 0.0;
	

	public void onModuleLoad() 
	{

		RootLayoutPanel.get().setStyleName("GskelVertPanel");
		VerticalPanel vp1 = new VerticalPanel();
		vp1.setStyleName("GskelVertPanel");
		vp1.setWidth("100%");
		Grid titleGrid = new Grid(1,2);
		titleGrid.setWidth("1600px");
		Image logoImage = new Image("images/gwtLogo.jpg");
		logoImage.setStyleName("logoLabel");

		Label dashboardTitleLabel = new Label("Integration Test Stand Operational Status");
		dashboardTitleLabel.setStyleName("titleLabel");
		statusLabel.setStyleName("statusLabel");
		dateLabel = new Label(new Date().toString());
		dateLabel.setStyleName("dateLabel");
		VerticalPanel vp2 = new VerticalPanel();
		vp2.add(dashboardTitleLabel);
		vp2.add(statusLabel);
		vp2.add(dateLabel);
		titleGrid.setWidget(0, 1, logoImage);
		titleGrid.setWidget(0, 0, vp2);
		CellFormatter cf = titleGrid.getCellFormatter();
		cf.setAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
		cf.setAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
		HorizontalPanel hp2 = new HorizontalPanel();
		hp2.add(new GaugeDashboard(this));
		Frame shiftrFrame = new Frame("https://shiftr.io/dmcginnis427/ess-integration-test-stand/embed?zoom=1");
		shiftrFrame.setSize(705 + "px", 705 + "px");
		hp2.add(shiftrFrame);
		vp1.add(titleGrid);
		vp1.add(hp2);
		RootLayoutPanel.get().add(vp1);

		DateTimer dt = new DateTimer();
		dt.scheduleRepeating(1000);
	}
	class DateTimer extends Timer
	{
		@Override
		public void run() 
		{
			dateLabel.setText(new Date().toString());
			statusLabel.setText("RF Power = " + NumberFormat.getFormat("0.0").format(klystronPower) + " kW at " + NumberFormat.getFormat("0.0").format(dutyFactor) + " % Duty Factor");
		}
		
	}
}
