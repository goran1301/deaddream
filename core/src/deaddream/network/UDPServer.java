package deaddream.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * A UDP server tool
 * 
 * @author goran
 *
 */
public class UDPServer {
	
	DatagramSocket socket;
	InetAddress adress;
	int port;
	
	
	public UDPServer() throws Exception {
		port = 9999;
		socket = new DatagramSocket(port);		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void receiveTestData() throws Exception {
		byte[] requestDataBuffer = new byte[1024];
		DatagramPacket requestPacket = new DatagramPacket(requestDataBuffer, requestDataBuffer.length);
		socket.receive(requestPacket);
		
		String gottenData = new String(requestPacket.getData(), 0, requestPacket.getLength());
		int gottenNumber = Integer.parseInt(gottenData.trim());
		//data processing
		int processedData = gottenNumber * gottenNumber;
		
		byte[] responseData = String.valueOf(processedData).getBytes();
		DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, requestPacket.getAddress(), requestPacket.getPort());
		socket.send(responsePacket);
	}
	
}
