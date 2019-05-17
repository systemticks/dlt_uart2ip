package de.systemticks.dlt.uart2ip.com;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;

import de.systemticks.dlt.uart2ip.api.ByteBufferHandler;
import de.systemticks.dlt.uart2ip.utils.ByteOperations;

public class ComPortWriter implements ByteBufferHandler {

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
		logger.info(ByteOperations.bytesToHex(messageToWrite));
		
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
	
	@Override
	public void tearDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub
		
	}	
}
