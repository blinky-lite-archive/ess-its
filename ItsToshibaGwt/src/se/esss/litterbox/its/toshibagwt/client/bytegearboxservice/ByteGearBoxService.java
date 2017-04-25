package se.esss.litterbox.its.toshibagwt.client.bytegearboxservice;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import se.esss.litterbox.its.toshibagwt.shared.bytegearboxgwt.ByteGearBoxGwt;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("bytegearbox")
public interface ByteGearBoxService  extends RemoteService
{
	byte[][] getReadWriteMessage(String topic) throws Exception;
	byte[] getReadMessage(String topic) throws Exception;
	byte[] getWriteMessage(String topic) throws Exception;
	Long getlastUpdateDate(String topic) throws Exception;
	Long getlastReadUpdateDate(String topic) throws Exception;
	Long getlastWriteUpdateDate(String topic) throws Exception;
	Long[] getlastReadWriteUpdateDate(String topic) throws Exception;
	String publishMessage(String topic, byte[] message, boolean settingsEnabled) throws Exception;
	ByteGearBoxGwt[] getByteGearBoxGwt();
}
