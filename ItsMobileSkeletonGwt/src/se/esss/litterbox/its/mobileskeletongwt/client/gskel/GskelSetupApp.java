package se.esss.litterbox.its.mobileskeletongwt.client.gskel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import se.esss.litterbox.its.mobileskeletongwt.client.EntryPointApp;
import se.esss.litterbox.its.mobileskeletongwt.client.EntryPointAppService;
import se.esss.litterbox.its.mobileskeletongwt.client.EntryPointAppServiceAsync;
import se.esss.litterbox.its.mobileskeletongwt.client.bytegearboxservice.ByteGearBoxService;
import se.esss.litterbox.its.mobileskeletongwt.client.bytegearboxservice.ByteGearBoxServiceAsync;
import se.esss.litterbox.its.mobileskeletongwt.client.mqttdata.MqttService;
import se.esss.litterbox.its.mobileskeletongwt.client.mqttdata.MqttServiceAsync;

public class GskelSetupApp 
{
	private GskelTabLayoutPanel gskelTabLayoutPanel;
	private GskelVerticalPanel gskelVerticalPanel;
	private GskelOptionDialog optionDialog;
	private GskelMessageDialog messageDialog;
	private GskelFrameDialog frameDialog;
	private boolean mobile = false;
	private final EntryPointAppServiceAsync entryPointAppService = GWT.create(EntryPointAppService.class);
	private final ByteGearBoxServiceAsync byteGearBoxService = GWT.create(ByteGearBoxService.class);
	private final MqttServiceAsync mqttService = GWT.create(MqttService.class);
	private boolean settingsPermitted;
	private VerticalPanel mainVerticalPanel = new VerticalPanel();
	private HorizontalPanel titlePanel = new HorizontalPanel();
	private boolean tabbedLayout = false;

	private int gskelTabLayoutPanelHeightBarHeightPx = 30;


// Getters
	public boolean isMobile() {return mobile;}
	public GskelOptionDialog getOptionDialog() {return optionDialog;}
	public GskelMessageDialog getMessageDialog() {return messageDialog;}
	public GskelFrameDialog getFrameDialog() {return frameDialog;}
	public GskelTabLayoutPanel getGskelTabLayoutPanel() {return gskelTabLayoutPanel;}
	public EntryPointAppServiceAsync getEntryPointAppService() {return entryPointAppService;}
	public ByteGearBoxServiceAsync getByteGearBoxService() {return byteGearBoxService;}
	public MqttServiceAsync getMqttService() {return mqttService;}
	public boolean isSettingsPermitted() {return settingsPermitted;}
	public HorizontalPanel getTitlePanel() {return titlePanel;}
// Setters
	public void setSettingsPermitted(boolean settingsPermitted) {this.settingsPermitted = settingsPermitted;}
	
	public GskelSetupApp(EntryPointApp entryPointApp, boolean tabbedLayout)
	{
		RootLayoutPanel.get().setStyleName("RootPanel");
		this.tabbedLayout = tabbedLayout;
		
		mobile = false;
		String userAgent = Navigator.getUserAgent().toLowerCase();
		if (userAgent.indexOf("iphone") >= 0) mobile = true;
		if (userAgent.indexOf("ipad") >= 0) mobile = true;
		if (userAgent.indexOf("android") >= 0) mobile = true;


        optionDialog =  new GskelOptionDialog();
        messageDialog =  new GskelMessageDialog();
        frameDialog = new GskelFrameDialog(entryPointApp);
		
	    mainVerticalPanel.add(titlePanel);
		if (tabbedLayout)
		{
			gskelTabLayoutPanel = new GskelTabLayoutPanel(gskelTabLayoutPanelHeightBarHeightPx);
			mainVerticalPanel.add(gskelTabLayoutPanel);
		}
		RootLayoutPanel.get().add(mainVerticalPanel);
		Window.addResizeHandler(new GskelResizeHandler());
		resize();

	}
	public void addPanel(GskelVerticalPanel gskelVerticalPanel)
	{
		if (mainVerticalPanel.getWidgetCount() > 1) mainVerticalPanel.remove(1);
		this.gskelVerticalPanel = gskelVerticalPanel;
		if (gskelVerticalPanel.isScrollable())
		{
			mainVerticalPanel.add(gskelVerticalPanel.getScrollPanel());
		}
		else
		{
			mainVerticalPanel.add(gskelVerticalPanel);
		}
		resize();
	}
	public void addPanel(GskelVerticalPanel gskelVerticalPanel, String tabTitle)
	{
		gskelTabLayoutPanel.addGskelVerticalPanel(gskelVerticalPanel, tabTitle);
	}
	public void setLogoImage(String logoImageUrl)
	{
		messageDialog.getLogoImage().setUrl(logoImageUrl);
		optionDialog.getLogoImage().setUrl(logoImageUrl);
	}
	public int getGskelTabLayoutPanelWidth()
	{
		return Window.getClientWidth() - 5;
	}
	public int getGskelTabLayoutPanelHeight()
	{
		return Window.getClientHeight() - 25;
	}
	public void resize()
	{
		if (tabbedLayout)
		{
			gskelTabLayoutPanel.setSize(Window.getClientWidth(), Window.getClientHeight() - titlePanel.getOffsetHeight());
		}
		else
		{
			if (mainVerticalPanel.getWidgetCount() > 1)
			{
				if (gskelVerticalPanel.isScrollable())
				{
					
					String swidth = Integer.toString(Window.getClientWidth()) + "px";
					String sheight = Integer.toString(Window.getClientHeight() - titlePanel.getOffsetHeight()) + "px";
					gskelVerticalPanel.getScrollPanel().setSize(swidth, sheight);
				}
			}
		}
	}
	public class GskelResizeHandler implements ResizeHandler
	{
		@Override
		public void onResize(ResizeEvent event) 
		{
			resize();
		}
	}
}
