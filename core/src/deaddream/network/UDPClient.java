package deaddream.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import com.badlogic.gdx.utils.Array;

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
	private Array<byte[]> commands;
	private Array<DatagramPacket> receiveBuffer;
	Array<BaseCommandInterface> localCommandsHistory;
	private int remoteFrameId;
	Thread udpThread;
	
	/**
	 * Construcor
	 * @throws Exception
	 */
	public UDPClient() throws Exception {
		commands = new Array<byte[]>();
		socket = new DatagramSocket();
		receiveBuffer = new Array<DatagramPacket>();
		//adress = InetAddress.getByName("the-twilightfox.ddns.net");
		adress = InetAddress.getLocalHost();
		port = 9999;
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
	public Array<byte[]> makeTestDataTransfer(BaseCommandInterface command) throws Exception  {
		addHistoryCommand(command);
		commands.clear();
		
		byte[] formatedData = getResponceCommand().getBytes();
		System.out.println(String.valueOf(formatedData.length));
		DatagramPacket packet = new DatagramPacket(formatedData, formatedData.length, adress, port);
		
		//udpThread.wait();
		socket.send(packet);
		packetProcessing(command.getFrameId());
		synchronized (receiveBuffer) {
			receiveBuffer.clear();					
		}
		
		return commands;
	}
	
	private void packetProcessing(float needleFrameID) {
		for (DatagramPacket packet : receiveBuffer) {
			byte[] receivedCommandData = packet.getData();
			byte[] frameIdData = {receivedCommandData[0], receivedCommandData[1], receivedCommandData[2], receivedCommandData[3]};
			//jsonCommand = jsonCommand.trim();
			//System.out.println("Client received a command " + jsonCommand);
			updateRemoteFrameId(frameIdData);
			
			if (remoteFrameId == needleFrameID) {
				commands.add(receivedCommandData);
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
	
	private void updateRemoteFrameId(byte[] input){
		remoteFrameId = ByteBuffer.wrap(input).getInt();
	}
	
}
