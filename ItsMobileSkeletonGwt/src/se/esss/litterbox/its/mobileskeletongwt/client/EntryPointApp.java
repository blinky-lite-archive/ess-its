package se.esss.litterbox.its.mobileskeletongwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;

import se.esss.litterbox.its.mobileskeletongwt.client.callbacks.CheckIpAddresslAsyncCallback;
import se.esss.litterbox.its.mobileskeletongwt.client.contentpanels.GaugeShowCasePanel;
import se.esss.litterbox.its.mobileskeletongwt.client.contentpanels.LineChartShowCasePanel;
import se.esss.litterbox.its.mobileskeletongwt.client.contentpanels.ScatterChartShowCasePanel;
import se.esss.litterbox.its.mobileskeletongwt.client.contentpanels.TestPicPanel;
import se.esss.litterbox.its.mobileskeletongwt.client.gskel.GskelLoadWaiter;
import se.esss.litterbox.its.mobileskeletongwt.client.gskel.GskelSetupApp;
import se.esss.litterbox.its.mobileskeletongwt.client.bytegearboxservice.ByteGearBoxData;
import se.esss.litterbox.its.mobileskeletongwt.shared.bytegearboxgwt.ByteGearBoxGwt;

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
		
		Label titleLabel = new Label("GWT Skeleton");
		titleLabel.setStyleName("titleLabel");
		
		getSetup().getTitlePanel().add(titleLabel);
		getSetup().getEntryPointAppService().checkIpAddress(new CheckIpAddresslAsyncCallback(this));		
		getSetup().resize();
//		RootLayoutPanel.get().setWidth("980px");
//		RootLayoutPanel.get().setHeight("665px");
	}
	public void initializeTabs()
	{
		TestPicPanel tpp1 = new TestPicPanel(false, this, "images/gwtLogo.jpg");
		getSetup().addPanel(tpp1,"Shiftr");
		new TabLoadWaiter(100, 1);
	}
	private void loadTab2()
	{
		getSetup().addPanel(new GaugeShowCasePanel(this), "Gauge");
		new TabLoadWaiter(100, 2);
	}
	private void loadTab3()
	{
		getSetup().addPanel(new LineChartShowCasePanel(this), "Line");
		new TabLoadWaiter(100, 3);
	}
	void loadTab4()
	{
		getSetup().addPanel(new ScatterChartShowCasePanel(this), "Scatter");
		getSetup().getByteGearBoxService().getByteGearBoxGwt(new GetByteGearBoxGwtAsyncCallback(this));
	}
	class TabLoadWaiter extends GskelLoadWaiter
	{
		public TabLoadWaiter(int loopTimeMillis, int itask) {super(loopTimeMillis, itask);}
		@Override
		public boolean isLoaded() 
		{
			return true;
		}
		@Override
		public void taskAfterLoad() 
		{
			GWT.log(Integer.toString(getItask()));
			if (getItask() == 1) loadTab2();
			if (getItask() == 2) loadTab3();
			if (getItask() == 3) loadTab4();
		}
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
				entryPointApp.getSetup().addPanel(entryPointApp.getByteGearBoxData()[ii].getByteGearBoxDataPanel(), entryPointApp.getByteGearBoxData()[ii].getByteGearBoxGwt().getTopic());
			}
		}
		
	}
}
