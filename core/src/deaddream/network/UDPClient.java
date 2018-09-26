package deaddream.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.badlogic.gdx.utils.Array;

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
	private Array<String> commands;
	
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
	public Array<String> makeTestDataTransfer(String command) throws Exception  {
		commands.clear();
		byte[] formatedData = command.getBytes();
		DatagramPacket packet = new DatagramPacket(formatedData, formatedData.length, adress, port);
		socket.send(packet);
		
		byte[] receivedData = new byte[1024];
		DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
		socket.receive(receivedPacket);
		
		String jsonCommand = new String(receivedPacket.getData());
		jsonCommand = jsonCommand.trim();
		System.out.println("Client got a command: " + jsonCommand);
		if (jsonCommand != null) {
			commands.add(jsonCommand);
		}
		return commands;
	}
}
