package de.systemticks.dlt.uart2ip.com;

import java.util.HashMap;
import java.util.Map;

import com.fazecast.jSerialComm.SerialPort;

import de.systemticks.dlt.uart2ip.conf.Config;

public class ComPortManager {

	//private SerialPort port;
	private Map<String, SerialPort> ports = new HashMap<>();
	
	public SerialPort getOrCreatePort(Config conf) {
		
		SerialPort port;
		
		if(ports.containsKey(conf.getComPort())) 
		{
			port = ports.get(conf.getComPort());
		}
		
		else
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
			
			ports.put(conf.getComPort(), port);
		}
		
		return port;
	}	
		
	
}

	
	
