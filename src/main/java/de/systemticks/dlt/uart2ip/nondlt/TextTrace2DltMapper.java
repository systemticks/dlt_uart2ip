package de.systemticks.dlt.uart2ip.nondlt;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.systemticks.dlt.uart2ip.api.ByteBufferHandler;
import de.systemticks.dlt.uart2ip.dlt.DltHelper;
import de.systemticks.dlt.uart2ip.dlt.DltMessage;
import de.systemticks.dlt.uart2ip.utils.ByteOperations;
import de.systemticks.dlt.uart2ip.utils.TimeHelper;

public class TextTrace2DltMapper implements ByteBufferHandler {

	private static Logger logger = LoggerFactory.getLogger(TextTrace2DltMapper.class);	

	private Pattern pattern;

	private ByteBufferHandler origin;

	// 2019-07-09/09:20:16 759 info NAD-IF nadif_network_sig_info_resp:NADIF_HAL_SVC_GET_SIG_INFO
	// --------- 1 ------- -2- --3- --4--  -------------------------5----------------------------
	
	private final static int GROUP_PID = 2;
	private final static int GROUP_LOG_LEVEL = 3;
	private final static int GROUP_APPL = 4;
	private final static int GROUP_RAW_MESSAGE = 5;
	
	private final static String OTP_PATTERN = 
			"(\\d{4}-\\d{2}-\\d{2}/\\d{2}:\\d{2}:\\d{2})\\s+(\\d*)\\s+(info|notice|debug|warning|err)\\s+([\\w|-]*)\\s+(.*)";

	public TextTrace2DltMapper(ByteBufferHandler origin) {
		this.pattern =  Pattern.compile(OTP_PATTERN);
		this.origin = origin;
	}
	
	@Override
	public void processByteBuffer(byte[] buffer) {
		
		String s = new String(buffer);

		Matcher matcher = pattern.matcher(new String(s));
		
		if(matcher.matches())
		{
			byte dltLogLevel;
			switch (matcher.group(GROUP_LOG_LEVEL)) {
			case "info":
				dltLogLevel = DltHelper.LOG_LEVEL_INFO;
				break;
			case "notice":
				dltLogLevel = DltHelper.LOG_LEVEL_VERBOSE;
				break;
			case "debug":
				dltLogLevel = DltHelper.LOG_LEVEL_DEBUG;
				break;
			case "warning":
				dltLogLevel = DltHelper.LOG_LEVEL_WARN;
				break;
			case "err":
				dltLogLevel = DltHelper.LOG_LEVEL_ERROR;
				break;

			default:
				dltLogLevel = DltHelper.LOG_LEVEL_INFO;
				break;
			}
			
			origin.processByteBuffer(makeDltMessage(matcher.group(GROUP_RAW_MESSAGE), matcher.group(GROUP_APPL), "SOC", dltLogLevel).getRawData() );
		}
					
		else
		{
			System.out.println("Skip : "+s);
		}
	}	
	
	public DltMessage makeDltMessage(String text, String appId, String ctxId, byte logLevel)
	{
		byte[] standardHeader = new byte[12];
		byte[] extendedHeader = new byte[10];

		// STANDARD HEADER
		ByteBuffer buf = ByteBuffer.wrap(standardHeader);
		// Byte 0 = Header Type
		buf.put((byte) (DltHelper.STD_HTYP_MSBF | DltHelper.STD_HTYP_WEID | DltHelper.STD_HTYP_WTMS | DltHelper.STD_HTYP_UEH | DltHelper.STD_HTYP_VERS));
		// Byte 1 = Counter
		buf.put((byte) 0);
		// Byte 2-3 = Message Length
		int payloadLength = 4 /* Type Info */ + 2 /* bytes for str length */ + text.length() /* The length of the text itself */ + 1;
		buf.putShort((short) (standardHeader.length + extendedHeader.length + payloadLength));
		// Byte 4-7 = ECU ID
		buf.put(stringIdAsBytes("SOC"));
		// Byte 8 - 11 = time-stamp
		buf.putInt(TimeHelper.getUptime());
		
		// EXTENDED HEADER
		ByteBuffer exBuf = ByteBuffer.wrap(extendedHeader);
		// Byte 0 = Message Info
		exBuf.put( (byte) (0x01 | (DltHelper.EXH_MSIN_MSTP_LOG << 1) | logLevel << 4)); 
		// Byte 1 = Number of Arguments
		exBuf.put((byte) 1);
		// Byte 2-5 appid
		exBuf.put(stringIdAsBytes(appId));
		// Bytes 6-9 ctx id		
		exBuf.put(stringIdAsBytes(ctxId));
		
		// PAYLOAD
		byte[] payload = new byte[payloadLength];
		ByteBuffer payloadBuf = ByteBuffer.wrap(payload);
		int typeInfo = 1 << 9;
		payloadBuf.putInt(typeInfo);
		payloadBuf.putShort( (short) (text.length() + 1));
		payloadBuf.put(text.getBytes());
		payloadBuf.put((byte) 0x00);
		
		byte[] message = null;
		
		try {
			message = ByteOperations.concat(standardHeader, extendedHeader, payload);
		} catch (IOException e1) {
			logger.error(e1.getMessage());
		}
				
		return new DltMessage(message);		
		
	}	
	
	//FIXME Code Duplication from DltControlMessage...
	public static byte[] stringIdAsBytes(String id)
	{
		return ByteBuffer.allocate(4).put(id.getBytes(), 0, Math.min(4, id.length())).array();
	}


	@Override
	public void tearDown() {
		// TODO Auto-generated method stub
		origin.tearDown();
	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub
		origin.setup();
	}
	
}
