package deaddream.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.badlogic.gdx.utils.Array;

public class UDPReceiver implements Runnable {
	private Array<DatagramPacket> gottenPackages;
	
	private DatagramSocket datagramSocket;
	
	public UDPReceiver(DatagramSocket datagramSocket, Array<DatagramPacket> gottenPackages) {
		//this.localHistory = localHistory;
		this.gottenPackages = gottenPackages;
		this.datagramSocket = datagramSocket;
	}
	
	@Override
	public void run() {
		gottenPackages.clear();
		while (true) {
			byte[] receivedData = new byte[1024];
			DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
			try {
				datagramSocket.receive(receivedPacket);
				addReceivedPackages(receivedPacket);
				//wait();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}			
	}
	
	private void addReceivedPackages(DatagramPacket packet) {
		synchronized (gottenPackages) {
			gottenPackages.add(packet);
		}
	}
}
