package deaddream.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

import com.badlogic.gdx.utils.Array;

public class UDPServerTransmission {
	DatagramSocket socket;
	int port;
	private Array<DatagramPacket> receiveBuffer;
	Thread udpThread;
	private boolean transferDone = false;
	
	private InetAddress clientAdress;
	private int clientPort;
	
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
		//System.out.println("SERVER RECEIVE BUFFER SIZE: " + String.valueOf(receiveBuffer.size));
		
		synchronized (receiveBuffer) {
			for (DatagramPacket packet : receiveBuffer) {
				byte[] gottenData = packet.getData();
				transferDone = true;
				gottenData = Arrays.copyOfRange(gottenData, 0, packet.getLength());
				receivedBytesData.add(gottenData);
				//data processing
			    if (clientAdress == null) {
			    	clientAdress = packet.getAddress();
			    	clientPort = packet.getPort();
			    }
			}
			receiveBuffer.clear();					
		}
		
		if (clientAdress != null) {
			DatagramPacket commandPacket = new DatagramPacket(command, command.length, clientAdress, clientPort);
			socket.send(commandPacket);
		}
		
		//String gottenData = new String(requestPacket.getData(), 0, requestPacket.getLength());
		return receivedBytesData;
	}
	
	public void transferCheck() {
		if (receiveBuffer.size > 0) {
			transferDone = true;
		} 
	}
	
	public boolean isTransferDone() {
		return transferDone;
	}
	
}
