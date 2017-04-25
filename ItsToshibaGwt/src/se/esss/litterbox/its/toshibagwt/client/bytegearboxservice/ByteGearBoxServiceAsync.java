package se.esss.litterbox.its.toshibagwt.client.bytegearboxservice;

import com.google.gwt.user.client.rpc.AsyncCallback;

import se.esss.litterbox.its.toshibagwt.shared.bytegearboxgwt.ByteGearBoxGwt;

public interface ByteGearBoxServiceAsync 
{
	void publishMessage(String topic, byte[] message, boolean settingsEnabled, AsyncCallback<String> callback);
	void getByteGearBoxGwt(AsyncCallback<ByteGearBoxGwt[]> callback);
	void getReadWriteMessage(String topic, AsyncCallback<byte[][]> callback);
	void getReadMessage(String topic, AsyncCallback<byte[]> callback);
	void getWriteMessage(String topic, AsyncCallback<byte[]> callback);
	void getlastUpdateDate(String topic, AsyncCallback<Long> callback);
	void getlastReadUpdateDate(String topic, AsyncCallback<Long> callback);
	void getlastWriteUpdateDate(String topic, AsyncCallback<Long> callback);
	void getlastReadWriteUpdateDate(String topic, AsyncCallback<Long[]> callback);
}
