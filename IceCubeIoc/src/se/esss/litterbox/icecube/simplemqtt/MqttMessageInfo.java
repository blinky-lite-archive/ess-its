package se.esss.litterbox.icecube.simplemqtt;

public class MqttMessageInfo 
{
	private String topicString;
	private int qos;
	
	public String getTopicString() {return topicString;}
	public int getQos() {return qos;}
	
	public void setTopicString(String topicString) {this.topicString = topicString;}
	public void setQos(int qos) {this.qos = qos;}
	
	public MqttMessageInfo(String topicString, int qos)
	{
		this.topicString = topicString;
		this.qos = qos;
	}
	public MqttMessageInfo(MqttMessageInfo mqttMessageInfo)
	{
		this.topicString = mqttMessageInfo.topicString;
		this.qos = mqttMessageInfo.qos;
	}
}
