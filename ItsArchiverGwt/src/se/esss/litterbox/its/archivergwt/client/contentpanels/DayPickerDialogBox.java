package se.esss.litterbox.its.archivergwt.client.contentpanels;

import java.util.Date;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

public class DayPickerDialogBox  extends DialogBox
{
	private Label instructLabel = new Label("");
	public Label getInstructLabel() {return instructLabel;}
	DatePicker datePicker = new DatePicker();
	private TextBox dayTextBox = null;
	private Date pickedDate = null;
	public void setDayTextBox(TextBox dayTextBox) {this.dayTextBox = dayTextBox;}
	public void setPickedDate(Date pickedDate) {this.pickedDate = pickedDate;}

	public DayPickerDialogBox()
	{
		super();
		VerticalPanel vp1 = new VerticalPanel();
		vp1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vp1.add(instructLabel);
		datePicker.getValue();
		datePicker.addValueChangeHandler(new DatePickerValueChangeHandler(this));
		vp1.add(datePicker);
		setWidget(vp1);
	}
	@Override
	public void show()
	{
		datePicker.setValue(new Date());
		super.show();
	}
	class DatePickerValueChangeHandler implements ValueChangeHandler<Date>
	{
		DayPickerDialogBox dayPickerDialogBox;
		DatePickerValueChangeHandler(DayPickerDialogBox dayPickerDialogBox)
		{
			this.dayPickerDialogBox = dayPickerDialogBox;
		}
		@SuppressWarnings("deprecation")
		@Override
		public void onValueChange(ValueChangeEvent<Date> event) 
		{
			dayTextBox.setText(DateTimeFormat.getMediumDateFormat().format(datePicker.getValue()));
			dayPickerDialogBox.hide();
			pickedDate.setTime(datePicker.getValue().getTime());
		}
		
	}
}
