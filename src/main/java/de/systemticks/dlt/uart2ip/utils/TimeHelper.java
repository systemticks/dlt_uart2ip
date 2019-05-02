package de.systemticks.dlt.uart2ip.utils;

public class TimeHelper {

	private final static long START = System.currentTimeMillis();
	
	public static int getUptime()
	{
		return (int) ((System.currentTimeMillis() - START)*10); 
	}
	
}
