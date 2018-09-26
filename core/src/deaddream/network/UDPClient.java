package deaddream.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * A network UDP Client tool
 * 
 * @author goran
 *
 */
public class UDPClient {
	
	DatagramSocket socket;
	InetAddress adress;
	int port;
	
	/**
	 * Construcor
	 * @throws Exception
	 */
	public UDPClient() throws Exception {
		socket = new DatagramSocket();
		adress = InetAddress.getLocalHost();
		port = 9999;
	}
	
	/**
	 * Test dataTransfer
	 * @throws Exception
	 */
	public void makeTestDataTransfer() throws Exception  {
		int data = 8;
		byte[] formatedData = String.valueOf(data).getBytes();
		DatagramPacket packet = new DatagramPacket(formatedData, formatedData.length, adress, port);
		socket.send(packet);
		
		byte[] receivedData = new byte[1024];
		DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
		socket.receive(receivedPacket);
		
		String receivedDataString = new String(receivedPacket.getData());
		System.out.println("Client side: received a data: " + receivedDataString);
	}
}
