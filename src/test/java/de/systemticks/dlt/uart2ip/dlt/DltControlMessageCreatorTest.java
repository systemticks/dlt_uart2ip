package de.systemticks.dlt.uart2ip.dlt;

import static org.junit.Assert.*;

import org.junit.Test;

import de.systemticks.dlt.uart2ip.dlt.DltControlMessageCreator;
import de.systemticks.dlt.uart2ip.dlt.DltHelper;

public class DltControlMessageCreatorTest {

	@Test
	public void testCreateSetLogLevelPayload() 
	{
	
		byte[] message = DltControlMessageCreator.createSetLogLevelPayload("VUC", "APP", "CTX", DltHelper.LOG_LEVEL_INFO);
		

		assertNotNull(message);
	}

	@Test
	public void testStringIdAsBytes()
	{
		assertArrayEquals(new byte[] { 0x56, 0x55, 0x43, 0x00 }, DltControlMessageCreator.stringIdAsBytes("VUC"));
		assertArrayEquals(new byte[] { 0x56, 0x55, 0x43, 0x43 }, DltControlMessageCreator.stringIdAsBytes("VUCC"));
		assertArrayEquals(new byte[] { 0x56, 0x55, 0x43, 0x43 }, DltControlMessageCreator.stringIdAsBytes("VUCCC"));
	}

}
