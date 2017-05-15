package se.esss.litterbox.its.bytegearboxservergwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;

import se.esss.litterbox.its.bytegearboxservergwt.client.bytegearboxservice.ByteGearBoxData;
import se.esss.litterbox.its.bytegearboxservergwt.client.callbacks.CheckIpAddresslAsyncCallback;
import se.esss.litterbox.its.bytegearboxservergwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.its.bytegearboxservergwt.shared.bytegearboxgwt.ByteGearBoxGwt;
import se.esss.litterbox.its.bytegearboxservergwt.client.bytegearboxservice.ByteGearBoxPlotter;

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
		
		Label titleLabel = new Label("ITS ByteGearBox Server");
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
			entryPointApp.getSetup().addPanel(new ByteGearBoxPlotter(entryPointApp.getByteGearBoxData(), entryPointApp), "Plotter");
			for (int ii = 0; ii < byteGearBoxGwt.length; ++ii)
			{
				entryPointApp.getSetup().addPanel(entryPointApp.getByteGearBoxData()[ii].getByteGearBoxDataPanel(), entryPointApp.getByteGearBoxData()[ii].getByteGearBoxGwt().getTopic());
			}
		}
		
	}
}
