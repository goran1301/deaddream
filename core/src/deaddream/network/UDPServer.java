package deaddream.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.badlogic.gdx.utils.Array;

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
	private Array<String> commands;
	
	
	public UDPServer() throws Exception {
		commands = new Array<String>();
		port = 9999;
		socket = new DatagramSocket(port);		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public Array<String> receiveTestData(String command) throws Exception {
		commands.clear();
		byte[] requestDataBuffer = new byte[1024];
		DatagramPacket requestPacket = new DatagramPacket(requestDataBuffer, requestDataBuffer.length);
		socket.receive(requestPacket);
		
		//String gottenData = new String(requestPacket.getData(), 0, requestPacket.getLength());
		String gottenData = new String(requestPacket.getData());
		String jsonCommand = gottenData.trim();
		System.out.println("Host got a command: " + jsonCommand);
		if (jsonCommand != null) {
			commands.add(jsonCommand);
		}
		//data processing
		
		
		byte[] responseData = command.getBytes();
		DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, requestPacket.getAddress(), requestPacket.getPort());
		socket.send(responsePacket);
		return commands;
	}
	
}
