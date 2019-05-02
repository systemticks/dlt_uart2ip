package de.systemticks.dlt.uart2ip.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import de.systemticks.dlt.uart2ip.dlt.DltHelper;

public class PostProcessor {

	private static final byte[] EXT_DUMMY_HEADER = new byte[] { 0x00, 0x00, 0x41, 0x50, 0x49, 0x44, 0x43, 0x54, 0x49, 0x44};
	private final static int MINIMUM_STANDARD_HEDAER = 8; // type + counter + len + ecu id
//	private long start;
	
	public PostProcessor()
	{
//		start = System.currentTimeMillis();
	}
	
	public byte[] handleRawMessage(byte[] rawDltStream) throws IOException
	{		
		short msgLen = ByteBuffer.wrap(rawDltStream, 2, 2).getShort();
		boolean insertExtendedHeader = false;
		boolean insertTimestamp = false;
		
		// Check if extended header flag is used
		if( !((rawDltStream[0] & DltHelper.STD_HTYP_UEH) == DltHelper.STD_HTYP_UEH) )
		{
			// add a dummy header
			rawDltStream[0] = (byte) (rawDltStream[0] | DltHelper.STD_HTYP_UEH);			
			msgLen += EXT_DUMMY_HEADER.length;
			insertExtendedHeader = true;
		}

		// Check if time-stamp flag is used
		if( !((rawDltStream[0] & DltHelper.STD_HTYP_WTMS) == DltHelper.STD_HTYP_WTMS) )
		{
			// add a timestamp
			rawDltStream[0] = (byte) (rawDltStream[0] | DltHelper.STD_HTYP_WTMS);			
			msgLen += 4;
			insertTimestamp = true;
		}
		
		// re-assemble the message
		
		// set the new adapted message length
		ByteBuffer.wrap(rawDltStream, 2, 2).asShortBuffer().put(msgLen);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

		int currentIndex = 0;
		int remaining = rawDltStream.length;
		
		outputStream.write(rawDltStream, 0, MINIMUM_STANDARD_HEDAER);
		currentIndex += MINIMUM_STANDARD_HEDAER;
		remaining -= MINIMUM_STANDARD_HEDAER;
		
		if((rawDltStream[0] & DltHelper.STD_HTYP_WSID) == DltHelper.STD_HTYP_WSID)
		{
			outputStream.write(rawDltStream, currentIndex, 4);
			currentIndex += 4;
			remaining -= 4;
		}
		//FIXME: What if origin time-stamp was set?
		if(insertTimestamp)
		{
			outputStream.write(
						ByteBuffer.allocate(4).putInt(TimeHelper.getUptime()).array() ) ;
		}
		// overwrite existing time-stamp
		else
		{
			outputStream.write(
					ByteBuffer.allocate(4).putInt(TimeHelper.getUptime()).array() ) ;
			currentIndex += 4;
			remaining -= 4;			
		}
		if(insertExtendedHeader)
		{
			outputStream.write(EXT_DUMMY_HEADER);
		}
		
		outputStream.write(rawDltStream, currentIndex, remaining);
		
		return outputStream.toByteArray();
	}
	
}
