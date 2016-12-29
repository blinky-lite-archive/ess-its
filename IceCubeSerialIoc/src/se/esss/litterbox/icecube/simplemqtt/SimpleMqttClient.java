package se.esss.litterbox.icecube.simplemqtt;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public abstract class SimpleMqttClient implements MqttCallback
{
	private MqttClient mqttClient;

	private String brokerUrl = null;
	private String brokerKey = null;
	private String brokerSecret = null;
	private String clientId; 
	private boolean printStatus = true;
	private boolean cleanSession;
	private PrintStream statusPrintStream = System.out;
	private int noBaseStatusTabs = 1;
	private String baseStatusTabby = "\t";
	private CountDownLatch disconnectLatch = new CountDownLatch(0);
	private ArrayList<MqttMessageInfo> mqttMessageInfoSubscribeList;

	public String getClientId() {return clientId;}
	public PrintStream getStatusPrintStream() {return statusPrintStream;}
	public int getNoBaseStatusTabs() {return noBaseStatusTabs;}
	public CountDownLatch getDisconnectLatch() {return disconnectLatch;}
	public boolean isPrintStatus() {return printStatus;}
	public boolean isCleanSession() {return cleanSession;}
	public void setPrintStatus(boolean printStatus) {this.printStatus = printStatus;}
	public void setStatusPrintStream(PrintStream statusPrintStream) {this.statusPrintStream = statusPrintStream;}
	public void setCleanSession(boolean cleanSession) {this.cleanSession = cleanSession;}
	public void setNoBaseStatusTabs(int noBaseStatusTabs) 
	{
		this.noBaseStatusTabs = noBaseStatusTabs;
		baseStatusTabby = "";
		if (noBaseStatusTabs > 0) for (int ii = 0; ii < noBaseStatusTabs; ++ii) baseStatusTabby = baseStatusTabby + "\t";
	}

	public SimpleMqttClient(String clientId, String brokerUrl, String brokerKey, String brokerSecret, boolean cleanSession) throws Exception
	{
		this.brokerUrl = brokerUrl;
		this.brokerKey = brokerKey;
		this.brokerSecret = brokerSecret;
		this.clientId = clientId; 
		this.cleanSession = cleanSession;
		mqttMessageInfoSubscribeList = new ArrayList<MqttMessageInfo>();
		connect();
	}
	public void setStatus(String status)
	{
		if (!printStatus) return;
		statusPrintStream.println(new Date().toString() + baseStatusTabby + status);
	}
	public void connect() throws Exception
	{
		MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
		
		mqttConnectOptions.setCleanSession(cleanSession);
		mqttConnectOptions.setKeepAliveInterval(30);
		mqttConnectOptions.setUserName(brokerKey);
		mqttConnectOptions.setPassword(brokerSecret.toCharArray());

		try 
		{
			mqttClient = new MqttClient(brokerUrl, clientId);
			mqttClient.setCallback(this);
			mqttClient.connect(mqttConnectOptions);
		} catch (MqttException e) {throw new Exception(e);}
		
		setStatus("Connected to " + brokerUrl);
	}
	public void subscribe(String topicString, int qos) throws Exception
	{
		setStatus("Subscribing to: " + topicString);
		mqttClient.subscribe(topicString, qos);
		mqttMessageInfoSubscribeList.add(new MqttMessageInfo(topicString, qos));
	}
	public void unsubscribeAll() throws Exception
	{
		mqttClient.unsubscribe("#");
		mqttMessageInfoSubscribeList = new ArrayList<MqttMessageInfo>();
		setStatus("Unsubscribed from all topics");
	}
	public void disconnect() throws Exception
	{
		mqttClient.disconnect();
	}
	public boolean isConnected()
	{
		return mqttClient.isConnected();
	}
	public void reconnect() throws MqttException 
	{
		try
		{
			connect();
		}
		catch (Exception e) {};
		if (mqttMessageInfoSubscribeList.size() > 0)
		{
			for (int ii = 0; ii < mqttMessageInfoSubscribeList.size(); ++ii)
			{
				mqttClient.subscribe(mqttMessageInfoSubscribeList.get(ii).getTopicString(), mqttMessageInfoSubscribeList.get(ii).getQos());
			}
		}
	}
	@Override
	public void connectionLost(Throwable arg0)
	{
		while (!isConnected())
		{
			try
			{
				Thread.sleep(5000);
				setStatus("Lost connection. Trying to reconnect." );
				reconnect();
			} catch (Exception e) {setStatus("Error: " + e.getMessage());}
		}
	}
	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {}
	@Override
	public void messageArrived(String topic, MqttMessage mqttMessage)
	{
		newMessage(topic, mqttMessage.getPayload());
	}
	public void publishMessage(String topicString, byte[] message, int qos, boolean retained) throws Exception
	{
		// setup topic
		// topics on m2m.io are in the form <domain>/<stuff>/<thing>
		MqttTopic mqttTopic = mqttClient.getTopic(topicString);
		
		MqttMessage mqttMessage = new MqttMessage(message);
		mqttMessage.setQos(qos);
		mqttMessage.setRetained(retained);

    	// Publish the message
		setStatus("Publishing to topic \"" + topicString + "\" qos " + qos);
    	MqttDeliveryToken mqttDeliveryToken = null;
    	try 
    	{
    		// publish message to broker
    		mqttDeliveryToken = mqttTopic.publish(mqttMessage);
	    	// Wait until the message has been delivered to the broker
    		mqttDeliveryToken.waitForCompletion();
			Thread.sleep(100);
		} catch (MqttException | InterruptedException e) {throw new Exception(e);}
		setStatus("Topic \"" + topicString + "\" qos " + qos + " published.");
	}
	public void setDisconnectLatch(int numLatches)
	{
		disconnectLatch = new CountDownLatch(numLatches);
	}
	public void waitforDisconnectLatch(int numSecsToWait) throws Exception
	{
		boolean gotDisconnectNotice = false;
		if (numSecsToWait > 0)
		{
			gotDisconnectNotice = disconnectLatch.await(numSecsToWait, TimeUnit.SECONDS);
		}
		else
		{
			disconnectLatch.await();
			gotDisconnectNotice = true;
		}
		disconnect();
		if (!gotDisconnectNotice) throw new Exception ("Did not get disconnect notice");
	}
	public abstract void newMessage(String topic, byte[] message);
}
