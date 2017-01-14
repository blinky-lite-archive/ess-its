package se.esss.litterbox.its.cernrfgwt.client.googleplots;

import com.google.gwt.user.client.Timer;

public abstract class GooglePlotLoadWaiter extends Timer
{
	public GooglePlotLoadWaiter(int loopTimeMillis)
	{
		scheduleRepeating(loopTimeMillis);
	}
	public abstract boolean isLoaded();
	public abstract void taskAfterLoad();
	@Override
	public void run() 
	{
		if (!isLoaded()) return;
		cancel();
		taskAfterLoad();
	}

}
