package de.systemticks.dlt.uart2ip.dlt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.systemticks.dlt.uart2ip.api.RawBufferHandler;
import de.systemticks.dlt.uart2ip.com.ComPortReader;
import de.systemticks.dlt.uart2ip.com.ComPortWriter;

public class DltMessageServer implements RawBufferHandler {

	private ServerSocket serverSocket;
	private Socket clientSocket;
	private BufferedOutputStream bOutS;
	private boolean connected = false;
	private int port;
	private BufferedInputStream bIns;
	private RawBufferHandler controlMessageHandler;

    private static Logger logger = LoggerFactory.getLogger(DltMessageServer.class);	
	
	public DltMessageServer(int serverPort, RawBufferHandler controlMessageHandler) 
	{
		port = serverPort;
		this.controlMessageHandler = controlMessageHandler;
	}

	public void setup() {

		logger.info("start up DLT socket server");

		try {
			serverSocket = new ServerSocket(port);
			clientSocket = serverSocket.accept();
			bOutS = new BufferedOutputStream(clientSocket.getOutputStream());

			connected = true;
			logger.info("DLT client connected via port: "+port);
			
			//TODO : React on control messages
			bIns = new BufferedInputStream(clientSocket.getInputStream());
			
			while(true)
			{
				byte[] header = new byte[4];			
				bIns.read(header);
				short msgLen = ByteBuffer.wrap(header, 2, 2).getShort();
				byte[] rest = new byte[msgLen-header.length];
				bIns.read(rest);				

				logger.info("received control message from client");				
				logger.info(ComPortWriter.encodeHexString(header));				
				logger.info(ComPortWriter.encodeHexString(rest));
				
				byte[] msgToSend = concat(header, rest);
				controlMessageHandler.processByteBuffer(msgToSend);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	//FIXME : duplicated
	private byte[] concat(byte[] header, byte[] rest) throws IOException
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		outputStream.write( header );
		outputStream.write( rest );

		return outputStream.toByteArray( );		
	}

	
	public void tearDown() {
						
		logger.info("shutdown DLT socket server");
		try {
			if(connected)
			{
				bOutS.close();
				clientSocket.close();				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void processByteBuffer(byte[] message) {
		if (!connected) {
			return;
		}
		//FIXME - don't send in case socket connection died meanwhile
		try {
			logger.debug("Send message to DLT client");
			bOutS.write(message);
			bOutS.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			connected = false;
			logger.error(e.getMessage());
		}
	}

}
