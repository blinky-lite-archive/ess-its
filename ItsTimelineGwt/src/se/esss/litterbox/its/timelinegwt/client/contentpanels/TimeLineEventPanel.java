package se.esss.litterbox.its.timelinegwt.client.contentpanels;

import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class TimeLineEventPanel extends CaptionPanel
{
	TextBox byteMaskTextBox;
	private CheckBox[] eventBit = new CheckBox[8];
	public TimeLineEventPanel(int ievent, int byteMask)
	{
		super(Integer.toString(ievent));
		byteMaskTextBox = new  TextBox();
		byteMaskTextBox.setText(Integer.toString(byteMask));
		byteMaskTextBox.setWidth("2.0em");
		Grid eventGrid = new Grid(8, 1);
		if (ievent == 1) eventGrid = new Grid(8, 2);
		HTMLTable.CellFormatter formatter = eventGrid.getCellFormatter();
		for (int ij = 0; ij < 8; ++ij)
		{
			eventBit[ij] = new CheckBox();
			if (ievent == 1)
			{
				eventGrid.setWidget(ij, 0, new Label("B" + Integer.toString(ij) + " "));
				formatter.setHorizontalAlignment(ij, 0, HasHorizontalAlignment.ALIGN_CENTER);
				formatter.setVerticalAlignment(ij, 0, HasVerticalAlignment.ALIGN_MIDDLE);	
				eventGrid.setWidget(ij, 1, eventBit[ij]);
				formatter.setHorizontalAlignment(ij, 1, HasHorizontalAlignment.ALIGN_CENTER);
				formatter.setVerticalAlignment(ij, 1, HasVerticalAlignment.ALIGN_MIDDLE);	
			}
			else
			{
				eventGrid.setWidget(ij, 0, eventBit[ij]);
				formatter.setHorizontalAlignment(ij, 0, HasHorizontalAlignment.ALIGN_CENTER);
				formatter.setVerticalAlignment(ij, 0, HasVerticalAlignment.ALIGN_MIDDLE);	
			}
		}
		byte mask = intToByte(byteMask)[0];
		for (int ij = 0; ij < 8; ++ij)
		{
			boolean bitOn = false;
			if (getBit(mask, ij) > 0) bitOn = true;
			eventBit[ij].setValue(bitOn);
		}

		add(eventGrid);
	}
	public void setEnabled(boolean enabled)
	{
		for (int ij = 0; ij < 8; ++ij)
		{
			eventBit[ij].setEnabled(enabled);
		}
	}
	private int getBit(byte myByte, int position)
	{
	   return (myByte >> position) & 1;
	}
	public byte[] intToByte(int itarget)
	{
		
	    int n = 0;
	    int goal = 0xFF << (8 * n);
	    int b0 = (itarget & goal) >> (8 * n);
	    
	    n = 1;
	    goal = 0xFF << (8 * n);
	    int b1 = (itarget & goal) >> (8 * n);
	    
	    byte[] twoBytes = new byte[2];
	    twoBytes[0] = (byte) b0;
	    twoBytes[1] = (byte) b1;
	    
	    return twoBytes;
	}
	public String readEvent() throws Exception
	{
		int mask = 0;
		int pow2 = 1;
		for (int ij = 0; ij < 8; ++ij)
		{
			if (eventBit[ij].getValue()) mask = mask + pow2;
			pow2 = pow2 * 2;
		}
		return Integer.toString(mask);
	}
}
