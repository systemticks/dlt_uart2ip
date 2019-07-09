package de.systemticks.dlt.uart2ip.dlt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import de.systemticks.dlt.uart2ip.file.TmpFileWriter;
import de.systemticks.dlt.uart2ip.nondlt.TextTrace2DltMapper;

public class OTPTraceToDLT {

	@Test
	public void test() {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("src/test/resources/soc.logs"));
			TextTrace2DltMapper mapper = new TextTrace2DltMapper(new TmpFileWriter("src/test/resources/soc.dlts"));
			
			String line;
			mapper.setup();
			
			while (( line = br.readLine()) != null)
			{
				mapper.processByteBuffer(line.getBytes());
			}
			mapper.tearDown();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
