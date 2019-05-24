package de.systemticks.dlt.uart2ip.com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;

import de.systemticks.dlt.uart2ip.api.ByteBufferHandler;
import de.systemticks.dlt.uart2ip.utils.ByteOperations;

public class ComPortReader {
	
	private SerialPort port;
	private boolean reading = true;
	private ByteBufferHandler rawBufferHandler;

    private static Logger logger = LoggerFactory.getLogger(ComPortReader.class);	

	public ComPortReader(SerialPort port, ByteBufferHandler rawBufferHandler)
	{
		this.port = port;
		this.rawBufferHandler = rawBufferHandler;
	}
		
	
	/**
	 * Read continuously the byte stream from COM port and write it into a file 
	 */

	public void readFromStream() {
		
		logger.info("Start reading from com port");
		
		port.openPort();
		
		if(!port.openPort())
		{
			logger.error("Cannot open com port "+port.getPortDescription()+" for reading");
			return;
		}
		
		rawBufferHandler.setup();
		
		try {
			while (reading) {
				while (port.bytesAvailable() == 0)
					Thread.sleep(20);

				byte[] readBuffer = new byte[port.bytesAvailable()];
				int numRead = port.readBytes(readBuffer, readBuffer.length);
				
				logger.debug(ByteOperations.bytesToHex(readBuffer));
				if(numRead >= 0)
				{
					rawBufferHandler.processByteBuffer(readBuffer);
				}
				else
				{
					logger.error("Error while reading from com port");
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		rawBufferHandler.tearDown();
		port.closePort();

	}
	
	
}
