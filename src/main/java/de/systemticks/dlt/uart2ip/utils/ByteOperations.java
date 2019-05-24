package de.systemticks.dlt.uart2ip.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ByteOperations {

	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 3];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 3] = hexArray[v >>> 4];
	        hexChars[j * 3 + 1] = hexArray[v & 0x0F];
	        hexChars[j * 3 + 2] = ' ';
	    }
	    return new String(hexChars);
	}	
	
	public static byte[] concat(byte[] arr1, byte[] arr2) throws IOException
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		outputStream.write( arr1 );
		outputStream.write( arr2 );

		return outputStream.toByteArray( );		
	}

	public static byte[] concat(byte[] arr1, byte[] arr2, byte[] arr3) throws IOException
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		outputStream.write( arr1 );
		outputStream.write( arr2 );
		outputStream.write( arr3 );

		return outputStream.toByteArray( );		
	}

	
}
