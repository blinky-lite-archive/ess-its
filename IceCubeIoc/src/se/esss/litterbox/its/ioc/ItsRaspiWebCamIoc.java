package se.esss.litterbox.its.ioc;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;

import javax.imageio.ImageIO;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import se.esss.litterbox.icecube.ioc.IceCubePeriodicPollIoc;

public class ItsRaspiWebCamIoc extends IceCubePeriodicPollIoc
{
	private String cameraCommand = "raspistill -t 3000 -o raspiWebCamImage.jpg";
	String mainTopic = "";
	public ItsRaspiWebCamIoc(String clientId, String mainTopic, String mqttBrokerInfoFilePath) throws Exception 
	{
		super(clientId, mqttBrokerInfoFilePath);
		this.mainTopic = mainTopic;
	}
	@SuppressWarnings("unchecked")
	@Override
	public byte[] getDataFromGizmo() 
	{
		try 
		{
			System.out.println("Executing: " + cameraCommand);

			runExternalProcess(cameraCommand, true, false);
			byte[] imageInByte = null;
			BufferedImage originalImage = ImageIO.read(new File("raspiWebCamImage.jpg"));

			// convert BufferedImage to byte array
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "jpg", baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();
			Thread.sleep(1000);
			String dateString = new Date().toString();	
			boolean retained = true;
			JSONObject outputData = new JSONObject();
			outputData.put("date", dateString);
			publishMessage(mainTopic + "/image/date", outputData.toJSONString().getBytes(), 0, retained);

			
			return imageInByte;
		} catch (Exception e) 
		{System.out.println("Error: " + e.getMessage());}
		return null;
	}

	@Override
	public void handleBrokerMqttMessage(String topic, byte[] message) 
	{
		if (topic.equals(mainTopic + "/set"))
		{
			cameraCommand = "";
			try
			{
				JSONParser parser = new JSONParser();		
				JSONObject jsonData = (JSONObject) parser.parse(new String(message));
				String info = (String) jsonData.get("width");
				if (info != null) cameraCommand  = cameraCommand + " -w " + info;
				info = (String) jsonData.get("height");
				if (info != null) cameraCommand  = cameraCommand + " -h " + info;
				info = (String) jsonData.get("rot");
				if (info != null) cameraCommand  = cameraCommand + " -rot " + info;
				info = (String) jsonData.get("exp");
				if (info != null) cameraCommand  = cameraCommand + " -exp " + info;
				info = (String) jsonData.get("timeout");
				if (info != null) cameraCommand  = cameraCommand + " -t " + info;
				info = (String) jsonData.get("interval");
				if (info != null) setPeriodicPollPeriodmillis(Integer.parseInt(info));
			}
			catch (ParseException | NumberFormatException e) {System.out.println("Error: " + e.getMessage());}
			cameraCommand = "raspistill" + cameraCommand + " -o raspiWebCamImage.jpg";
			System.out.println("Received new camer command: " + cameraCommand);
		}
		
	}
	public static void main(String[] args) throws Exception 
	{
		String iocName = args[0];
		String mainTopic = args[1];
		int periodicPoll = Integer.parseInt(args[2]);
		ItsRaspiWebCamIoc ioc = new ItsRaspiWebCamIoc(iocName, mainTopic, "itsmqttbroker.dat");
		ioc.setPeriodicPollPeriodmillis(periodicPoll);
		ioc.startIoc(mainTopic + "/set", mainTopic + "/image/jpg");
	}

}
