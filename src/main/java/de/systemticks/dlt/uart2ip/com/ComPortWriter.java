package de.systemticks.dlt.uart2ip.com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;

public class ComPortWriter {

	private SerialPort port;
    private static Logger logger = LoggerFactory.getLogger(ComPortWriter.class);	

	
	public ComPortWriter(SerialPort _port)
	{
		this.port = _port;		
	}
	
	public void writeByteBuffer(byte[] messageToWrite)
	{
		//FIXME : Add error handling
		logger.info("Write control message to com port");
		
		port.openPort();
		
		if(port.isOpen())
		{
			int res = port.writeBytes(messageToWrite, messageToWrite.length);
			if(res == -1)
			{
				logger.error("Write to com port failed!");
			}
			//port.closePort();
		}
		else 
		{
			logger.error("Cannot open com port for writing!");			
		}
				
	}
	
}
