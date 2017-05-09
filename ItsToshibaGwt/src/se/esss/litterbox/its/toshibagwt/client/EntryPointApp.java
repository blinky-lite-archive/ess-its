package se.esss.litterbox.its.toshibagwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.RootLayoutPanel;

import se.esss.litterbox.its.toshibagwt.client.bytegearboxservice.ByteGearBoxData;
import se.esss.litterbox.its.toshibagwt.client.callbacks.CheckIpAddresslAsyncCallback;
import se.esss.litterbox.its.toshibagwt.client.contentpanels.SummaryPanel;
import se.esss.litterbox.its.toshibagwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.its.toshibagwt.shared.bytegearboxgwt.ByteGearBoxGwt;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EntryPointApp implements EntryPoint 
{
	private GskelSetupApp setup;
	private ByteGearBoxData[] byteGearBoxData;
	private int byteGearBoxDataTimerPeriodMillis = 1000;


	// Setters
	public void setByteGearBoxData(ByteGearBoxData[] byteGearBoxData) {this.byteGearBoxData = byteGearBoxData;}
	
	// Getters
	public GskelSetupApp getSetup() {return setup;}
	public ByteGearBoxData[] getByteGearBoxData() {return byteGearBoxData;}
	public int getByteGearBoxDataTimerPeriodMillis() {return byteGearBoxDataTimerPeriodMillis;}
	
	public void onModuleLoad() 
	{
		setup = new GskelSetupApp(this, true);
		getSetup().setLogoImage("images/gwtLogo.jpg");
		
		Label titleLabel = new Label("ITS ToshibaPLC");
		titleLabel.setStyleName("titleLabel");
		
		getSetup().getTitlePanel().add(titleLabel);
		getSetup().getEntryPointAppService().checkIpAddress(new CheckIpAddresslAsyncCallback(this));		
		getSetup().resize();
//		RootLayoutPanel.get().setWidth("980px");
//		RootLayoutPanel.get().setHeight("665px");
	}
	public void initializeTabs()
	{
		getSetup().getByteGearBoxService().getByteGearBoxGwt(new GetByteGearBoxGwtAsyncCallback(this));
	}
	public ByteGearBoxData getByteGearBoxData(String topic) throws Exception
	{
		for (int ii = 0; ii < byteGearBoxData.length; ++ii)
		{
			if (byteGearBoxData[ii].getByteGearBoxGwt().getTopic().equals(topic)) return byteGearBoxData[ii];
		}
		throw new Exception("ByteGearBoxData topic " + topic + " not found");
	}
	static class GetByteGearBoxGwtAsyncCallback implements AsyncCallback<ByteGearBoxGwt[]>
	{
		EntryPointApp entryPointApp;
		GetByteGearBoxGwtAsyncCallback(EntryPointApp entryPointApp)
		{
			this.entryPointApp = entryPointApp;
		}
		@Override
		public void onFailure(Throwable caught) {GWT.log(caught.getMessage());}

		@Override
		public void onSuccess(ByteGearBoxGwt[] byteGearBoxGwt) 
		{
			entryPointApp.setByteGearBoxData(new ByteGearBoxData[byteGearBoxGwt.length]);
			for (int ii = 0; ii < byteGearBoxGwt.length; ++ii)
			{
				entryPointApp.getByteGearBoxData()[ii] = new ByteGearBoxData(byteGearBoxGwt[ii], entryPointApp.getByteGearBoxDataTimerPeriodMillis(), entryPointApp);
			}
			entryPointApp.getSetup().addPanel(new SummaryPanel(entryPointApp), "Summary");
			for (int ii = 0; ii < byteGearBoxGwt.length; ++ii)
			{
				entryPointApp.getSetup().addPanel(entryPointApp.getByteGearBoxData()[ii].getByteGearBoxDataPanel(), entryPointApp.getByteGearBoxData()[ii].getByteGearBoxGwt().getTopic());
			}
		}
		
	}
}
