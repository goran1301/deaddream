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
		if (command == null) {
			command = "1";
		}
		commands.clear();
		byte[] requestDataBuffer = new byte[1024];
		DatagramPacket requestPacket = new DatagramPacket(requestDataBuffer, requestDataBuffer.length);
		socket.receive(requestPacket);
		//System.out.println("received");
		
		//String gottenData = new String(requestPacket.getData(), 0, requestPacket.getLength());
		String gottenData = new String(requestPacket.getData());
		String jsonCommand = gottenData.trim();
		if (jsonCommand != "1") {
			System.out.println("Host got a command: " + jsonCommand);
		}
		if (jsonCommand != null || jsonCommand != "1") {
			commands.add(jsonCommand);
		}
		//data processing
		
		
		byte[] responseData = command.getBytes();
		DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, requestPacket.getAddress(), requestPacket.getPort());
		socket.send(responsePacket);
		return commands;
	}
	
}
