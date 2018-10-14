package deaddream.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.badlogic.gdx.utils.Array;

public class UDPServerTransmission {
	DatagramSocket socket;
	int port;
	private Array<DatagramPacket> receiveBuffer;
	Thread udpThread;
	private boolean transferDone = false;
	
	public UDPServerTransmission() throws Exception {
		port = 9999;
		socket = new DatagramSocket(port);		
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
	public Array<byte[]> exchange(byte[] command) throws Exception {
		Array<byte[]> receivedBytesData = new Array<byte[]>();
		System.out.println("SERVER RECEIVE BUFFER SIZE: " + String.valueOf(receiveBuffer.size));
		if (receiveBuffer.size > 0) {
			transferDone = true;
		} 
		for (DatagramPacket packet : receiveBuffer) {
			byte[] gottenData = packet.getData();
			transferDone = true;
			receivedBytesData.add(gottenData);
			//data processing
		    
			DatagramPacket commandPacket = new DatagramPacket(command, command.length, packet.getAddress(), packet.getPort());
			socket.send(commandPacket);
			
			
		}
		synchronized (receiveBuffer) {
			receiveBuffer.clear();					
		}
		
		//String gottenData = new String(requestPacket.getData(), 0, requestPacket.getLength());
		return receivedBytesData;
	}
	
	public boolean isTransferDone() {
		return transferDone;
	}
	
}
