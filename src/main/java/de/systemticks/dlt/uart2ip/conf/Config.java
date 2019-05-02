package de.systemticks.dlt.uart2ip.conf;

public class Config {

	private String comPort;
	private int baudRate;
	private int dataBits;
	private int stopBits;
	private String parity;
	
	private int serverPort;
	private String tmpFile = "raw_uart.tmp";

	public Config() 
	{
		comPort = "COM4";
		baudRate = 115200;
		dataBits = 8;
		stopBits = 1;
		parity = "NO_PARITY";
		serverPort = 3490;
	}
	
	
	
	public String getTmpFile() {
		return tmpFile;
	}


	public void setTmpFile(String tmpFile) {
		this.tmpFile = tmpFile;
	}

	public String getComPort() {
		return comPort;
	}

	public void setComPort(String comPort) {
		this.comPort = comPort;
	}

	public int getBaudRate() {
		return baudRate;
	}

	public void setBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}

	public int getDataBits() {
		return dataBits;
	}

	public void setDataBits(int dataBits) {
		this.dataBits = dataBits;
	}

	public int getStopBits() {
		return stopBits;
	}

	public void setStopBits(int stopBits) {
		this.stopBits = stopBits;
	}

	public String getParity() {
		return parity;
	}

	public void setParity(String parity) {
		this.parity = parity;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
}
