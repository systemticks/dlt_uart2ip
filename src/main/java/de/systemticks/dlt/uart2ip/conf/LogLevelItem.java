package de.systemticks.dlt.uart2ip.conf;

public class LogLevelItem {

	private String appId;
	private String ctxId;
	private String ecuId;
	private byte level;
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getCtxId() {
		return ctxId;
	}
	public void setCtxId(String ctxId) {
		this.ctxId = ctxId;
	}
	public String getEcuId() {
		return ecuId;
	}
	public void setEcuId(String ecuId) {
		this.ecuId = ecuId;
	}
	public byte getLevel() {
		return level;
	}
	public void setLevel(byte level) {
		this.level = level;
	}
	
	
}
