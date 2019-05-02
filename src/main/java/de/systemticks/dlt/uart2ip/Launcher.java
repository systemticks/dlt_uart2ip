package de.systemticks.dlt.uart2ip;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;

import de.systemticks.dlt.uart2ip.com.ComPortManager;
import de.systemticks.dlt.uart2ip.com.ComPortReader;
import de.systemticks.dlt.uart2ip.com.ComPortWriter;
import de.systemticks.dlt.uart2ip.conf.Config;
import de.systemticks.dlt.uart2ip.dlt.DltControlMessageCreator;
import de.systemticks.dlt.uart2ip.dlt.DltHelper;
import de.systemticks.dlt.uart2ip.dlt.DltMessageServer;
import de.systemticks.dlt.uart2ip.file.TmpFileReader;
import de.systemticks.dlt.uart2ip.file.TmpFileWriter;
import de.systemticks.dlt.uart2ip.utils.PostProcessor;

public class Launcher {

    private static Logger logger = LoggerFactory.getLogger(Launcher.class);	
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		logger.info("Starting up ...");
		Config conf = new Config();

		// Establish a socket connection and wait for incoming client, such as EB solys or DLT Viewer
		DltMessageServer server = new DltMessageServer(conf.getServerPort());		
		new Thread(() -> {
			server.setup();
		}).start();		
		
		// Initialize the COM port for reading and writing
		ComPortManager portManager= new ComPortManager();
		SerialPort port = portManager.getOrCreatePort(conf);
				
		// Reads from UART and writes into a temporary file				
		ComPortReader comReader = new ComPortReader(port, new TmpFileWriter(conf.getTmpFile()));
		new Thread(() -> {
			comReader.readFromStream();
			server.tearDown();
			logger.info("Shutting down ...");
			System.exit(0);
		}).start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Set the initial logging configuration
		ComPortWriter comWriter = new ComPortWriter(port);
		byte[] msgToSend = DltControlMessageCreator.createSetLogLevelPayload("VUC", "IPCS", "CTX1", DltHelper.LOG_LEVEL_INFO);
		comWriter.writeByteBuffer(msgToSend);
		
		// Read from temporary file and write to socket
		TmpFileReader reader = new TmpFileReader();
		PostProcessor post = new PostProcessor();
		try {
			reader.open(conf.getTmpFile());
			while(true)
			{				
				byte[] dltMsg = reader.readHeader();
				if(dltMsg != null)
				{
					//FIXME: think about decorator pattern
					//server.handleDltMessage(dltMsg);
					server.processByteBuffer(post.handleRawMessage(dltMsg));
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		}

	}

}
