package de.systemticks.dlt.uart2ip.dlt;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.systemticks.dlt.uart2ip.utils.ByteOperations;
import de.systemticks.dlt.uart2ip.utils.TimeHelper;

public class DltControlMessageCreator {

	private static Logger logger = LoggerFactory.getLogger(DltControlMessageCreator.class);	
	
	// 4 Byte ServiceId
	// 4 Byte appId
	// 4 Byte ctxId
	// 1 Byte Log Level
	// 4 Byte reserverd
	private final static int SET_LOG_LEVE_PAYLOAD_SIZE = 17;

	public static byte[] createSetLogLevelPayload(String ecuId, String appId, String ctxId, byte logLevel) {
		// payload = service Id + parameters (appId, ctxId, logLevel, 4 bytes "remo")
		byte[] payload = new byte[SET_LOG_LEVE_PAYLOAD_SIZE];
		byte[] reserved = "remo".getBytes();

		ByteBuffer.wrap(payload).putInt(DltHelper.SERVICE_ID_SET_LOG_LEVEL).put(appId.getBytes()).put(ctxId.getBytes())
			.put(logLevel).put(reserved);

		
		return makeControlMessage(payload);
	}

	public static byte[] createSetLogLevelDltSll(String ecuId, String appId, String ctxId, byte logLevel) {

		StringBuffer buf = new StringBuffer("{dlt-sll ");
		buf.append(appId).append("/").append(ctxId).append("/").append(logLevel).append("}");
		
		return buf.toString().getBytes();
		
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
		
		byte[] message = null;
		
		try {
			message = ByteOperations.concat(standardHeader, extendedHeader, payload);
		} catch (IOException e1) {
			logger.error(e1.getMessage());
		}
				
		return message;		
		
	}
	
	public static byte[] stringIdAsBytes(String id)
	{
		return ByteBuffer.allocate(4).put(id.getBytes(), 0, Math.min(4, id.length())).array();
	}
	
}
