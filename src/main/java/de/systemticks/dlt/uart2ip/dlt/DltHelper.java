package de.systemticks.dlt.uart2ip.dlt;

public class DltHelper {

	
	
	// Use Extended Header
	public final static byte STD_HTYP_UEH = 0x01;
	// Use Big Endian (Most Significant Byte first)
	public final static byte STD_HTYP_MSBF = 0x02;
	// With ECU Id
	public final static byte STD_HTYP_WEID = 0x04;		
	// With session Id
	public final static byte STD_HTYP_WSID = 0x08;	
	// With time-stamp
	public final static byte STD_HTYP_WTMS = 0x10;	
	// Version
	public final static byte STD_HTYP_VERS = (1<<5);
	
	
	public final static byte EXH_MSIN_MSTP_CTRL = 0x03;
	public final static byte EXH_MSIN_MSTP_LOG = 0x00;
	
	
	public final static byte EXH_MSIN_MTIN_CTRL_REQUEST = 0x01;
	
	public final static int SERVICE_ID_SET_LOG_LEVEL = 0x01;
	
	// The log levels
	public final static byte LOG_LEVEL_FATAL = 1;
	public final static byte LOG_LEVEL_ERROR = 2;
	public final static byte LOG_LEVEL_WARN = 3;
	public final static byte LOG_LEVEL_INFO = 4;
	public final static byte LOG_LEVEL_DEBUG = 5;
	public final static byte LOG_LEVEL_VERBOSE = 6;
	
}
