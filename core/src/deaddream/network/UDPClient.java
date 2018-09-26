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
		commands = new Array<String>();
		socket = new DatagramSocket();
		adress = InetAddress.getLocalHost();
		port = 9999;
	}
	
	/**
	 * Test dataTransfer
	 * @throws Exception
	 */
	public Array<String> makeTestDataTransfer(String command) throws Exception  {
		//System.out.println("client send 111111111111");
		if (command == null) {
			command = "1";
		}
		commands.clear();
		//System.out.println("client send 222222");
		byte[] formatedData = command.getBytes();
		DatagramPacket packet = new DatagramPacket(formatedData, formatedData.length, adress, port);
		socket.send(packet);
		
		byte[] receivedData = new byte[1024];
		DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
		socket.receive(receivedPacket);
		
		String jsonCommand = new String(receivedPacket.getData());
		jsonCommand = jsonCommand.trim();
		if (jsonCommand != "1") {
			System.out.println("Client got a command: " + jsonCommand);
		}
		
		if (jsonCommand != null || jsonCommand != "1") {
			commands.add(jsonCommand);
		}
		return commands;
	}
}
