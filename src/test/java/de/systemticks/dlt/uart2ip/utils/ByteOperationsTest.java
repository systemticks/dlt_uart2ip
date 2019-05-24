package de.systemticks.dlt.uart2ip.utils;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class ByteOperationsTest {

	@Test
	public void testConcat() {
		byte[] arg1 = new byte[]{0x01, 0x02, 0x03, 0x04};
		byte[] arg2 = new byte[]{0x05, 0x06, 0x07, 0x08, 0x09, 0x0A};
		
		try {
			byte[] concat = ByteOperations.concat(arg1, arg2);
			assertEquals(10, concat.length);
			
			for(int i=0; i<10; i++) {
				assertEquals(i+1, concat[i]);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testConcat2() {
		byte[] arg1 = new byte[]{0x01, 0x02, 0x03, 0x04};
		byte[] arg2 = new byte[]{0x05, 0x06, 0x07, 0x08};
		byte[] arg3 = new byte[]{0x09, 0x0A};
		
		try {
			byte[] concat = ByteOperations.concat(arg1, arg2, arg3);
			assertEquals(10, concat.length);
			
			for(int i=0; i<10; i++) {
				assertEquals(i+1, concat[i]);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
