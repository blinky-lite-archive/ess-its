package se.esss.litterbox.its.archivergwt.client.mqttdata;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import se.esss.litterbox.its.archivergwt.shared.ArchiveTopic;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("mqtt")
public interface MqttService  extends RemoteService
{
	String publishMessage(String topic, byte[] message, boolean settingsEnabled, boolean debug, String debugResponse) throws Exception;
	ArrayList<ArchiveTopic> getTopicList(boolean debug, String debugResponse);
	ArrayList<ArchiveTopic> addTopic(ArchiveTopic archiveTopic, boolean settingsEnabled, boolean debug, String debugResponse) throws Exception;
	ArrayList<ArchiveTopic> deleteTopic(int index, boolean settingsEnabled, boolean debug, String debugResponse) throws Exception;
}
