package de.systemticks.dlt.uart2ip.dlt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.systemticks.dlt.uart2ip.api.ByteBufferHandler;

public class DltFileWriter implements ByteBufferHandler{

	private static Logger logger = LoggerFactory.getLogger(DltFileWriter.class);	

	private String filename;
	private FileOutputStream fop;
	private byte[] DLT_PATTERN = new byte[] { 0x44, 0x4C, 0x54, 0x01 };
	private byte[] ECU = DltControlMessageCreator.stringIdAsBytes("VUC");

	public DltFileWriter(String filename)
	{
		this.filename = filename;
	}
	
	@Override
	public void setup() {
		// TODO Auto-generated method stub				
		File file = new File(filename);

		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			fop = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			logger.error(e1.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}		
	}
	
	
	@Override
	public void processByteBuffer(byte[] buffer) {
		// TODO Auto-generated method stub
		long timestamp = System.currentTimeMillis();
		int seconds = (int) (timestamp / 1000);
		int microseconds = (int) ((timestamp % 1000) * 1000);
		
		try {
			fop.write(DLT_PATTERN);
			//FIXME : Re-use an allocated ByteBuffer instead of creating a new one all the time
			fop.write(ByteBuffer.allocate(4).putInt(seconds).array());
			fop.write(ByteBuffer.allocate(4).putInt(microseconds).array());
			fop.write(ECU);
			fop.write(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		
	}

	@Override
	public void tearDown() {
		try {
			fop.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}


}
