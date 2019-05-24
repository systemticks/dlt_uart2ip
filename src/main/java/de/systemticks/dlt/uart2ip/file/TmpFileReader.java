package de.systemticks.dlt.uart2ip.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import de.systemticks.dlt.uart2ip.utils.ByteOperations;

public class TmpFileReader {

	private File tempFile;
	private BufferedInputStream bInS;

	// ECU ID is 4 byte: "VUC" with a terminating 0x00
	private static final byte[] ECU_ID_MARKER = new byte[] { 0x56, 0x55, 0x43, 0x00 };
	private static final int MIN_HEADER_SIZE_WITH_ECUID = 8;
	private final byte[] header = new byte[MIN_HEADER_SIZE_WITH_ECUID];

	public void open(String filename) throws FileNotFoundException {
		tempFile = new File(filename);
		bInS = new BufferedInputStream(new FileInputStream(tempFile));
	}

	public byte[] readHeader() throws IOException {
		
		if (bInS.markSupported() && bInS.available() >= header.length) {

			bInS.read(header);

			if (isHeaderOk()) {

				short msgLen = ByteBuffer.wrap(header, 2, 2).getShort();
				byte[] restOfMessage = new byte[msgLen - header.length];
				while(bInS.available() < (msgLen - header.length)) 
				{
					
				}
				bInS.read(restOfMessage);

				return ByteOperations.concat(header, restOfMessage);					
			}

			return null;
		} else {
			return null;
		}

	}
	

	private boolean isHeaderOk() {
		return (ECU_ID_MARKER[0] == header[4] && ECU_ID_MARKER[1] == header[5] && ECU_ID_MARKER[2] == header[6]
				&& ECU_ID_MARKER[3] == header[7]);
	}

}
