package de.systemticks.dlt.uart2ip.api;

import java.io.IOException;

public interface RawBufferHandler {

	public void processByteBuffer(byte[] buffer);
	
	public void tearDown();
	
	public void setup();
	
}
