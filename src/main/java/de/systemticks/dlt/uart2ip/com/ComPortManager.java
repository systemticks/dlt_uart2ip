package de.systemticks.dlt.uart2ip.com;

import com.fazecast.jSerialComm.SerialPort;

import de.systemticks.dlt.uart2ip.conf.Config;

public class ComPortManager {

	private SerialPort port;

	public SerialPort getOrCreatePort(Config conf) {
		
		if(port == null)
		{
			port = SerialPort.getCommPort(conf.getComPort());
			
			port.setBaudRate(conf.getBaudRate());
			port.setNumDataBits(conf.getDataBits());
			port.setNumStopBits(conf.getStopBits());
			
			if(conf.getParity().equals("NO_PARITY"))
			{
				port.setParity(SerialPort.NO_PARITY);
			}
			//TODO else
			port.setFlowControl(SerialPort.FLOW_CONTROL_XONXOFF_IN_ENABLED);
			port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, 1000, 1000);
		}
		
		return port;
	}	
	
}

	
	
