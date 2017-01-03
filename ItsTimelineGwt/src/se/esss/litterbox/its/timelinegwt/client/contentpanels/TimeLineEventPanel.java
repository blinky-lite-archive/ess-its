package se.esss.litterbox.its.timelinegwt.client.contentpanels;

import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.TextBox;

public class TimeLineEventPanel extends CaptionPanel
{
	TextBox byteMaskTextBox;
	public TimeLineEventPanel(int ievent, int byteMask)
	{
		super(Integer.toString(ievent));
		byteMaskTextBox = new  TextBox();
		byteMaskTextBox.setText(Integer.toString(byteMask));
		byteMaskTextBox.setWidth("2.0em");
		add(byteMaskTextBox);
	}
	public String readEvent() throws Exception
	{
		try
		{
			int ievent = Integer.parseInt(byteMaskTextBox.getText());
			if (ievent < 0)   throw new Exception();
			if (ievent > 255) throw new Exception();
			return Integer.toString(ievent);
		}
		catch (NumberFormatException nfe) {throw new Exception();}
	}
}
