package se.esss.litterbox.its.raspiwebcamgwt.client.contentpanels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;

import se.esss.litterbox.its.raspiwebcamgwt.client.EntryPointApp;
import se.esss.litterbox.its.raspiwebcamgwt.client.gskel.GskelVerticalPanel;
import se.esss.litterbox.its.raspiwebcamgwt.client.mqttdata.MqttData;

public class RaspiCamImagePanel  extends GskelVerticalPanel
{
	Image webCamImage = null;

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
				getEntryPointApp().getDateLabel().setText(getJsonValue("date"));
				long imageCounter = Long.parseLong(getJsonValue("counter"));
				webCamImage.setUrl("images/raspiWebCamImage" + Long.toString(imageCounter - 2) + ".jpg");
				Image.prefetch("images/raspiWebCamImage" + Long.toString(imageCounter - 1) + ".jpg");
			}
			catch (Exception e)
			{
				GWT.log(e.getMessage());
			}
			
		}
	}

}
