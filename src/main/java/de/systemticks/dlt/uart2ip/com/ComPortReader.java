package de.systemticks.dlt.uart2ip.com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;

import de.systemticks.dlt.uart2ip.api.RawBufferHandler;

public class ComPortReader {
	
	private SerialPort port;
	private boolean reading = true;
	private RawBufferHandler rawBufferHandler;

	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static Logger logger = LoggerFactory.getLogger(ComPortReader.class);	

	private static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 3];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 3] = hexArray[v >>> 4];
	        hexChars[j * 3 + 1] = hexArray[v & 0x0F];
	        hexChars[j * 3 + 2] = ' ';
	    }
	    return new String(hexChars);
	}	

	public ComPortReader(SerialPort port, RawBufferHandler rawBufferHandler)
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
				
				logger.debug(ComPortReader.bytesToHex(readBuffer));
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
