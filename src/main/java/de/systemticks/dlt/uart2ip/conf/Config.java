package de.systemticks.dlt.uart2ip.conf;

import java.util.ArrayList;
import java.util.List;

public class Config {

	private String comPort;
	private int baudRate;
	private int dataBits;
	private int stopBits;
	private String parity;
	private boolean forwardToClient;
	private boolean forwardToECU;
	private List<LogLevelItem> setLogLevels;
	
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
		forwardToClient = true;
		forwardToECU = false;
		setLogLevels = new ArrayList<>();
	}		
	
	public boolean isForwardToClient() {
		return forwardToClient;
	}

	public void setForwardToClient(boolean forwardToClient) {
		this.forwardToClient = forwardToClient;
	}

	public boolean isForwardToECU() {
		return forwardToECU;
	}

	public void setForwardToECU(boolean forwardToECU) {
		this.forwardToECU = forwardToECU;
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

	public void addLogLevel(LogLevelItem item) {		
		setLogLevels.add(item);		
	}

	public List<LogLevelItem> getSetLogLevels() {
		return setLogLevels;
	}
	
	
}
