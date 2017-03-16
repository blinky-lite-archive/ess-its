package se.esss.litterbox.its.raspiwebcamgwt.client.contentpanels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;

import se.esss.litterbox.its.raspiwebcamgwt.client.EntryPointApp;
import se.esss.litterbox.its.raspiwebcamgwt.client.gskel.GskelVerticalPanel;
import se.esss.litterbox.its.raspiwebcamgwt.client.mqttdata.MqttData;

public class RaspiCamImagePanel  extends GskelVerticalPanel
{
	Image webCamImage = null;
	int version = 1;

	public Image getWebCamImage() {return webCamImage;}
	
	public RaspiCamImagePanel(EntryPointApp entryPointApp) 
	{
		super(false, entryPointApp);
		webCamImage = new Image("images/raspiWebCamImage.jpg");
		add(webCamImage);
		new RaspiWebCamMqttData(entryPointApp);

	}
	class RaspiWebCamMqttData extends MqttData
	{
		public RaspiWebCamMqttData(EntryPointApp entryPointApp) 
		{
			super("itsIceCube08Cam/image/date", MqttData.JSONDATA, 1000, entryPointApp);
		}

		@Override
		public void doSomethingWithData() 
		{
			try
			{
				this.getEntryPointApp().getDateLabel().setText(getJsonValue("date"));
				version++;
				webCamImage.setUrl("images/raspiWebCamImage.jpg" + "?v" + version);
			}
			catch (Exception e)
			{
				GWT.log(e.getMessage());
			}
			
		}
	}

}
