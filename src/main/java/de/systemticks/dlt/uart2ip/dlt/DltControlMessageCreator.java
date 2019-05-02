package de.systemticks.dlt.uart2ip.dlt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import de.systemticks.dlt.uart2ip.utils.TimeHelper;

public class DltControlMessageCreator {

	public static byte[] createSetLogLevelPayload(String ecuId, String appId, String ctxId, byte logLevel) {
		// payload = service Id + parameters (appId, ctxId, logLevel, 4 bytes "remo")
		byte[] payload = new byte[17];
		byte[] reserved = "remo".getBytes();

		ByteBuffer.wrap(payload).putInt(DltHelper.SERVICE_ID_SET_LOG_LEVEL).put(appId.getBytes()).put(ctxId.getBytes())
			.put(logLevel).put(reserved);
		

		return makeControlMessage(payload);
	}

	public static byte[] makeControlMessage(byte[] payload)
	{
		byte[] standardHeader = new byte[12];
		byte[] extendedHeader = new byte[10];

		// STANDARD HEADER
		ByteBuffer buf = ByteBuffer.wrap(standardHeader);
		// Byte 0 = Header Type
		buf.put((byte) (DltHelper.STD_HTYP_WEID | DltHelper.STD_HTYP_WTMS | DltHelper.STD_HTYP_UEH | DltHelper.STD_HTYP_VERS));
		// Byte 1 = Counter
		buf.put((byte) 0);
		// Byte 2-3 = Message Length
		buf.putShort((short) (standardHeader.length + extendedHeader.length + payload.length));
		// Byte 4-7 = ECU ID
		buf.put(stringIdAsBytes("VUC"));
		// Byte 8 - 11 = time-stamp
		buf.putInt(TimeHelper.getUptime());
		
		// EXTENDED HEADER
		ByteBuffer exBuf = ByteBuffer.wrap(extendedHeader);
		// Byte 0 = Message Info
		exBuf.put( (byte) ((DltHelper.EXH_MSIN_MSTP_CTRL << 1) | DltHelper.EXH_MSIN_MTIN_CTRL_REQUEST << 4)); 
		// Byte 1= Number of Arguments
		exBuf.put((byte) 1);
		// Byte 2-5 appid
		exBuf.put(stringIdAsBytes("APP"));
		// Bytes 6-9 ctx id		
		exBuf.put(stringIdAsBytes("CON"));
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write( standardHeader );
			outputStream.write( extendedHeader );
			outputStream.write( payload );
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return outputStream.toByteArray( );		
		
	}
	
	public static byte[] stringIdAsBytes(String id)
	{
		return ByteBuffer.allocate(4).put(id.getBytes(), 0, Math.min(4, id.length())).array();
	}
	
}
