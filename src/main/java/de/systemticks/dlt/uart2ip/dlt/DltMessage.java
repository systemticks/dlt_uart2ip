package de.systemticks.dlt.uart2ip.dlt;

public class DltMessage {

	private byte[] rawData;

	public DltMessage(byte[] rawData)
	{
		this.rawData = rawData;
	}

	public byte[] getRawData() {
		return rawData;
	}

	
}
