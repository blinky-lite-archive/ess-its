package se.esss.litterbox.its.toshibagwt.client.contentpanels;

import se.esss.litterbox.its.toshibagwt.client.EntryPointApp;
import se.esss.litterbox.its.toshibagwt.client.gskel.GskelVerticalPanel;

public class SummaryPanel extends GskelVerticalPanel 
{
	
	public SummaryPanel(EntryPointApp entryPointApp) 
	{
		super(true, entryPointApp);
		add(new StateButtonPanel(entryPointApp));
		add(new OpModePanel(entryPointApp));
		
	}
}