package deaddream.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import com.badlogic.gdx.utils.Array;

import deaddream.units.utilities.input.commands.BaseCommandInterface;

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
	private Array<byte[]> commands;
	Array<BaseCommandInterface> localCommandsHistory;
	private int remoteFrameId;
	private Array<DatagramPacket> receiveBuffer;
	Thread udpThread;
	
	public UDPServer() throws Exception {
		commands = new Array<byte[]>();
		port = 9999;
		socket = new DatagramSocket(port);		
		localCommandsHistory = new Array<BaseCommandInterface>();
		receiveBuffer = new Array<DatagramPacket>();
	}
	
	public void startReceive() {
		UDPReceiver udpReceiver = new UDPReceiver(socket, receiveBuffer);
		udpThread = new Thread(udpReceiver);
		udpThread.setName("receiver");
		udpThread.setDaemon(true);
		udpThread.start();
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public Array<byte[]> receiveTestData(BaseCommandInterface command) throws Exception {
		addHistoryCommand(command);
		commands.clear();
		
		for (DatagramPacket packet : receiveBuffer) {
			byte[] gottenData = packet.getData();
			if (gottenData != null) {
				byte[] frameIdData = {gottenData[0], gottenData[1], gottenData[2], gottenData[3]};
				updateRemoteFrameId(frameIdData);
				
				if (remoteFrameId == command.getFrameId()) {
					commands.add(gottenData);
				}
				//data processing
				
				byte[] responseData = getResponceCommand().getBytes();
				DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, packet.getAddress(), packet.getPort());
				socket.send(responsePacket);
				synchronized (receiveBuffer) {
					receiveBuffer.clear();					
				}
				System.out.println("RECEIVE BUFFER SIZE: " + String.valueOf(receiveBuffer.size));
			}
		}
		
		//String gottenData = new String(requestPacket.getData(), 0, requestPacket.getLength());
		return commands;
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
			if (localCommandsHistory.get(i).getFrameId() == remoteFrameId) {
				return localCommandsHistory.get(i);
			}
		}
		return localCommandsHistory.get(localCommandsHistory.size - 1);
	}
	
	private void updateRemoteFrameId(byte[] input){
		remoteFrameId = ByteBuffer.wrap(input).getInt();
	}
	
}
