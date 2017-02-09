package se.esss.litterbox.its.archivergwt.client.mqttdata;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import se.esss.litterbox.its.archivergwt.shared.ArchiveJsonData;
import se.esss.litterbox.its.archivergwt.shared.ArchiveTopic;

public interface MqttServiceAsync 
{

	void publishMessage(String topic, byte[] message, boolean settingsEnabled, boolean debug, String debugResponse, AsyncCallback<String> callback);
	void getTopicList(boolean debug, String debugResponse, AsyncCallback<ArrayList<ArchiveTopic>> callback);
	void addTopic(ArchiveTopic archiveTopic, boolean settingsEnabled, boolean debug, String debugResponse, AsyncCallback<ArrayList<ArchiveTopic>> callback);
	void deleteTopic(int index, boolean settingsEnabled, boolean debug, String debugResponse, AsyncCallback<ArrayList<ArchiveTopic>> callback);
	void getArchiveData(ArrayList<ArchiveJsonData> archiveJsonDataList, boolean settingsEnabled, boolean debug, String debugResponse, AsyncCallback<ArrayList<ArchiveJsonData>> callback);
}
