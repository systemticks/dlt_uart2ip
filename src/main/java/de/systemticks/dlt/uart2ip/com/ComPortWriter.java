package de.systemticks.dlt.uart2ip.com;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;

import de.systemticks.dlt.uart2ip.api.RawBufferHandler;

public class ComPortWriter implements RawBufferHandler {

	private SerialPort port;
    private static Logger logger = LoggerFactory.getLogger(ComPortWriter.class);	

	
	public ComPortWriter(SerialPort _port)
	{
		this.port = _port;		
	}
	
	public void processByteBuffer(byte[] messageToWrite)
	{
		//FIXME : Add error handling
		logger.info("Write control message to com port: ");
		logger.info(encodeHexString(messageToWrite));
		
		port.openPort();
		
		if(port.isOpen())
		{
			int res = port.writeBytes(messageToWrite, messageToWrite.length);
			if(res == -1)
			{
				logger.error("Write to com port failed!");
			}
		}
		else 
		{
			logger.error("Cannot open com port for writing!");			
		}
				
	}
	
	public static String byteToHex(byte num) {
	    char[] hexDigits = new char[3];
	    hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
	    hexDigits[1] = Character.forDigit((num & 0xF), 16);
	    hexDigits[2] = ' ';
	    return new String(hexDigits);
	}
	public static String encodeHexString(byte[] byteArray) {
	    StringBuffer hexStringBuffer = new StringBuffer();
	    for (int i = 0; i < byteArray.length; i++) {
	        hexStringBuffer.append(byteToHex(byteArray[i]));
	    }
	    return hexStringBuffer.toString();
	}


	@Override
	public void tearDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub
		
	}	
}
