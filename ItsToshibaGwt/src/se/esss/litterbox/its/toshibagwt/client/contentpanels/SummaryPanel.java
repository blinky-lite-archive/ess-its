package se.esss.litterbox.its.toshibagwt.client.contentpanels;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import se.esss.litterbox.its.toshibagwt.client.EntryPointApp;
import se.esss.litterbox.its.toshibagwt.client.gskel.GskelVerticalPanel;

public class SummaryPanel extends GskelVerticalPanel 
{
	
	public SummaryPanel(EntryPointApp entryPointApp) 
	{
		super(true, entryPointApp);
		VerticalPanel vp1 = new VerticalPanel();
		vp1.add(new StateButtonPanel(entryPointApp));
		vp1.add(new OpModePanel(entryPointApp));
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.add(vp1);
		hp1.add(new StatusPanel(entryPointApp));
		add(hp1);
		
	}
}