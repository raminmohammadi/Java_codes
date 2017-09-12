import java.io.IOException;
import java.util.Calendar;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;
import jpcap.PacketReceiver;
import jpcap.packet.ARPPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;

public class NetworkCapture {

	private final NetworkData nd;

	public NetworkCapture(NetworkData nd) throws IOException {
		NetworkInterface[] devices = JpcapCaptor.getDeviceList();

		//check how many device do we have 
		// for(NetworkInterface device : devices){
		// System.out.println(device.);
		// }


		// we will open specific network device which using
		// JPCAPCAPTOR.openDevice ( Device type 'wireless' or 'Ethernet' , Max
		// byte capture at once , promiscuous mode, timeout for capture)
		JpcapCaptor jpcap = JpcapCaptor.openDevice(devices[2], 2000, false, 20);
		this.nd = nd;

		// capture packets for specific # of iterations ( -1: infinite , 2:
		// ignore timeout , 1: # of iterations)

		jpcap.loopPacket(1000, new Tcpdump(nd));

	}

	public void listIntarfaces() {
		NetworkInterface[] devices = JpcapCaptor.getDeviceList();
		for (int i = 0; i < devices.length; i++) {
			System.out.println(i + " :" + devices[i].name + "(" + devices[i].description + ")");
			System.out
			.println("    data link:" + devices[i].datalink_name + "(" + devices[i].datalink_description + ")");
			System.out.print("    MAC address:");
			for (byte b : devices[i].mac_address)
				System.out.print(Integer.toHexString(b & 0xff) + ":");
			System.out.println();
			for (NetworkInterfaceAddress a : devices[i].addresses)
				System.out.println("    address:" + a.address + " " + a.subnet + " " + a.broadcast);
		}
	}

	static class Tcpdump implements PacketReceiver {
		NetworkData nd;

		public Tcpdump(NetworkData nd) {
			this.nd = nd;
		}

		@Override

		public void receivePacket(Packet packet) {
			DataRecord dr = null;
			if(dr!=null){ 
				
			}
			else{
				if (packet instanceof IPPacket) {
					try {

						IPPacket ip = (IPPacket) packet;
						if (ip.protocol == IPPacket.IPPROTO_TCP) {
							TCPPacket tp = (TCPPacket) packet;
							dr = new DataRecord(tp.src_ip.getHostName(), tp.dst_ip.getHostAddress(), tp.dst_port,
									Calendar.getInstance().getTimeInMillis(), tp.len, "TCP");

						} else if (ip.protocol == IPPacket.IPPROTO_UDP) {
							UDPPacket up = (UDPPacket) packet;
							dr = new DataRecord(up.src_ip.getHostName(), up.dst_ip.getHostAddress(), up.dst_port,
									Calendar.getInstance().getTimeInMillis(), up.len, "UDP");
						}
						this.nd.addDataRecords(dr);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (packet instanceof ARPPacket) {
					System.out.println("its down here ");
					ARPPacket ap = (ARPPacket) packet;
					dr = new DataRecord(ap.getSenderHardwareAddress().toString(), ap.getTargetHardwareAddress().toString(),
							ap.plen, Calendar.getInstance().getTimeInMillis(), ap.len, "ARP");

					this.nd.addDataRecords(dr);

				}
			}

		}
	}

	public static void main(String[] args) throws IOException {
		NetworkData nd = new NetworkData();

		new NetworkCapture(nd);

		RecordReader rr = new RecordReader();
		rr.save("records", nd);
		System.out.println(nd.getTotalRecords());
	}
}
