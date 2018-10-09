package deaddream.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import deaddream.units.utilities.input.commands.BaseCommandInterface;

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
	private Array<DatagramPacket> receiveBuffer;
	private JsonReader reader;	
	Array<BaseCommandInterface> localCommandsHistory;
	private int remoteFrameId;
	Thread udpThread;
	
	/**
	 * Construcor
	 * @throws Exception
	 */
	public UDPClient() throws Exception {
		commands = new Array<String>();
		socket = new DatagramSocket();
		receiveBuffer = new Array<DatagramPacket>();
		//adress = InetAddress.getByName("the-twilightfox.ddns.net");
		adress = InetAddress.getLocalHost();
		port = 9999;
		reader = new JsonReader();
		localCommandsHistory = new Array<BaseCommandInterface>();
		remoteFrameId = -1;
	}
	
	public void startReceive() {
		UDPReceiver udpReceiver = new UDPReceiver(socket, receiveBuffer);
		udpThread = new Thread(udpReceiver);
		udpThread.setName("receiver");
		udpThread.setDaemon(true);
		udpThread.start();
	}
	
	
	/**
	 * Test dataTransfer
	 * @throws Exception
	 */
	public Array<String> makeTestDataTransfer(BaseCommandInterface command) throws Exception  {
		addHistoryCommand(command);
		String commandString = getResponceCommand().toJson();
		commands.clear();
		
		byte[] formatedData = commandString.getBytes();
		System.out.println(String.valueOf(formatedData.length));
		DatagramPacket packet = new DatagramPacket(formatedData, formatedData.length, adress, port);
		
		//udpThread.wait();
		socket.send(packet);
		packetProcessing(command.getFrameId());
		synchronized (receiveBuffer) {
			receiveBuffer.clear();					
		}
		//udpThread.notify();
		
		
		return commands;
	}
	
	private void packetProcessing(float needleFrameID) {
		for (DatagramPacket packet : receiveBuffer) {
			String jsonCommand = new String(packet.getData());
			jsonCommand = jsonCommand.trim();
			System.out.println("Client received a command " + jsonCommand);
			updateRemoteFrameId(jsonCommand);
			
			if (remoteFrameId == needleFrameID) {
				commands.add(jsonCommand);
			}
		}
	}
	
    private void addHistoryCommand(BaseCommandInterface command) {
		
		for (BaseCommandInterface historyCommand : localCommandsHistory) {
			if (historyCommand == command) {
				return;
			}
		}
		localCommandsHistory.add(command);
		return;
	}
	
	private BaseCommandInterface getResponceCommand() {

		for (int i = localCommandsHistory.size - 1; i >= 0; i--) {
			if (localCommandsHistory.get(i).getFrameId() == remoteFrameId + 1) {
				return localCommandsHistory.get(i);
			}
		}
		return localCommandsHistory.get(localCommandsHistory.size - 1);
	}
	
	private void updateRemoteFrameId(String input){
		JsonValue parsedJson = reader.parse(input);
		try {
			remoteFrameId = parsedJson.getInt("id");
			
		} catch (IllegalArgumentException e) {
			return;
		}
	}
	
}
