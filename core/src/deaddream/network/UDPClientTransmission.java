package deaddream.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

import com.badlogic.gdx.utils.Array;

public class UDPClientTransmission {
	DatagramSocket socket;
	InetAddress adress;
	int port;
	private Array<DatagramPacket> receiveBuffer;
	Thread udpThread;
	private boolean transferDone = false;
	
	public UDPClientTransmission() throws Exception {
		port = 9999;
		//adress = InetAddress.getByName("the-twilightfox.ddns.net");
		adress = InetAddress.getLocalHost();
		socket = new DatagramSocket();		
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
		//System.out.println("CLIENT RECEIVE BUFFER SIZE: " + String.valueOf(receiveBuffer.size));
		
		DatagramPacket commandPacket = new DatagramPacket(command, command.length, adress, port);
		socket.send(commandPacket);
		
		synchronized (receiveBuffer) {
			if (receiveBuffer.size > 0) {
				transferDone = true;
			} 
			for (DatagramPacket packet : receiveBuffer) {
				//System.out.println("CLIENT RECEIVE BUFFER SIZE: " + packet.getLength() + " DATA SIZE " + packet.getData().length);
				byte[] gottenData = packet.getData();
				gottenData = Arrays.copyOfRange(gottenData, 0, packet.getLength());
				//System.out.println("CLIENT RECEIVE BUFFER SIZE: " + gottenData.length);
				transferDone = true;
				receivedBytesData.add(gottenData);
			}
			receiveBuffer.clear();					
		}
		
		//String gottenData = new String(requestPacket.getData(), 0, requestPacket.getLength());
		return receivedBytesData;
	}
	
	public boolean isTransferDone() {
		return transferDone;
	}
}
