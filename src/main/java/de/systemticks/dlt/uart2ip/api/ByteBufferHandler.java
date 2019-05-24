package de.systemticks.dlt.uart2ip.api;

public interface ByteBufferHandler {

	public void processByteBuffer(byte[] buffer);
	
	public void tearDown();
	
	public void setup();
	
}
