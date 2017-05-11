package se.esss.litterbox.its.mobileskeletongwt.client.bytegearboxservice;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import se.esss.litterbox.its.mobileskeletongwt.client.EntryPointApp;
import se.esss.litterbox.its.mobileskeletongwt.shared.bytegearboxgwt.ByteGearBoxGwt;
import se.esss.litterbox.its.mobileskeletongwt.shared.bytegearboxgwt.ByteGearGwt;


public  class ByteGearBoxData 
{
	private ByteGearBoxGwt byteGearBoxGwt;
	private EntryPointApp entryPointApp;
	private ByteGearBoxDataPanel byteGearBoxDataPanel;

	public EntryPointApp getEntryPointApp() {return entryPointApp;}
	public ByteGearBoxGwt getByteGearBoxGwt() {return byteGearBoxGwt;}
	public ByteGearBoxDataPanel getByteGearBoxDataPanel() {return byteGearBoxDataPanel;}

	public ByteGearBoxData(ByteGearBoxGwt byteGearBoxGwt, int timerPeriodMillis, EntryPointApp entryPointApp)
	{
		this.byteGearBoxGwt = byteGearBoxGwt;
		this.entryPointApp = entryPointApp;
		ByteDataUpdateTimer byteDataUpdateTimer = new ByteDataUpdateTimer(this);
		byteDataUpdateTimer.scheduleRepeating(timerPeriodMillis);
		byteGearBoxDataPanel = new ByteGearBoxDataPanel(this);
	}
	public void setWriteData(ByteGearGwt byteGearGwt)
	{
		try
		{
			String topic  = byteGearBoxGwt.getTopic() + "/set";
			byte[] newSettingMessage = new byte[byteGearBoxGwt.getWriteByteLength() * 2];
			for (int ii = 0; ii < byteGearBoxGwt.getWriteByteLength(); ++ii) newSettingMessage[ii] = byteGearBoxGwt.getWriteData()[ii];
			byteGearGwt.getWriteByteTooth("WR_DATA").setValue("false");
			for (int ii = 0; ii < byteGearBoxGwt.getWriteByteLength(); ++ii) newSettingMessage[ii + byteGearBoxGwt.getWriteByteLength()] = byteGearBoxGwt.getWriteData()[ii];
			entryPointApp.getSetup().getByteGearBoxService().publishMessage(topic, newSettingMessage, entryPointApp.getSetup().isSettingsPermitted(), new SetWriteDataAsyncCallback());
		} catch (Exception e){ GWT.log(e.getMessage());}
	}
	private static class ByteDataUpdateTimer extends Timer
	{
		ByteGearBoxData byteGearBoxData;
		ByteDataUpdateTimer(ByteGearBoxData byteGearBoxData)
		{
			this.byteGearBoxData = byteGearBoxData;
		}
		@Override
		public void run() 
		{
//			GWT.log("Checking for new data on " + byteGearBoxData.byteGearBoxGwt.getTopic());
			byteGearBoxData.entryPointApp.getSetup().getByteGearBoxService().getlastReadWriteUpdateDate(byteGearBoxData.getByteGearBoxGwt().getTopic(), new GetByteDataAsyncCallback(byteGearBoxData));
		}
	}
	private static class SetWriteDataAsyncCallback implements AsyncCallback<String>
	{

		@Override
		public void onFailure(Throwable caught) {GWT.log(caught.getMessage());}

		@Override
		public void onSuccess(String result) {}
		
	}
	private static class GetByteDataAsyncCallback implements AsyncCallback<Long[]>
	{
		ByteGearBoxData byteGearBoxData;
		GetByteDataAsyncCallback(ByteGearBoxData byteGearBoxData)
		{
			this.byteGearBoxData = byteGearBoxData;
		}
		@Override
		public void onFailure(Throwable caught) {GWT.log(caught.getMessage());}
		@Override
		public void onSuccess(Long[] readWriteUpdate) 
		{
			if (readWriteUpdate[0].longValue() > byteGearBoxData.getByteGearBoxGwt().getLastReadDataUpdate().getTime())
			{
				byteGearBoxData.entryPointApp.getSetup().getByteGearBoxService().getReadMessage(byteGearBoxData.getByteGearBoxGwt().getTopic(), new GetReadMessageAsyncCallback(byteGearBoxData));
			}
			if (readWriteUpdate[1].longValue() > byteGearBoxData.getByteGearBoxGwt().getLastWriteDataUpdate().getTime())
			{
				byteGearBoxData.entryPointApp.getSetup().getByteGearBoxService().getWriteMessage(byteGearBoxData.getByteGearBoxGwt().getTopic(), new GetWriteMessageAsyncCallback(byteGearBoxData));
			}
			
		}
	}
	private static class GetReadMessageAsyncCallback implements AsyncCallback<byte[]>
	{
		ByteGearBoxData byteGearBoxData;
		GetReadMessageAsyncCallback(ByteGearBoxData byteGearBoxData)
		{
			this.byteGearBoxData = byteGearBoxData;
		}
		@Override
		public void onFailure(Throwable caught) {GWT.log(caught.getMessage());}
		@Override
		public void onSuccess(byte[] readByteArray) 
		{
//			GWT.log("New Data on Read " + byteGearBoxData.byteGearBoxGwt.getTopic());
			byteGearBoxData.getByteGearBoxGwt().setReadData(readByteArray);
		}
	}
	private static class GetWriteMessageAsyncCallback implements AsyncCallback<byte[]>
	{
		ByteGearBoxData byteGearBoxData;
		GetWriteMessageAsyncCallback(ByteGearBoxData byteGearBoxData)
		{
			this.byteGearBoxData = byteGearBoxData;
		}
		@Override
		public void onFailure(Throwable caught) {GWT.log(caught.getMessage());}
		@Override
		public void onSuccess(byte[] readByteArray) 
		{
//			GWT.log("New Data on Write " + byteGearBoxData.byteGearBoxGwt.getTopic());
			byteGearBoxData.getByteGearBoxGwt().setWriteData(readByteArray);
		}
	}
}
