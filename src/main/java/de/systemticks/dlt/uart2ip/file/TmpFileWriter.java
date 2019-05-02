package de.systemticks.dlt.uart2ip.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.systemticks.dlt.uart2ip.api.RawBufferHandler;

public class TmpFileWriter implements RawBufferHandler {

	private String filename;
	private FileOutputStream fop;

	public TmpFileWriter(String filename)
	{
		this.filename = filename;
	}
	
	@Override
	public void processByteBuffer(byte[] buffer) {
		// TODO Auto-generated method stub
		try {
			fop.write(buffer);
			fop.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void tearDown() {
		// TODO Auto-generated method stub
		try {
			fop.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
