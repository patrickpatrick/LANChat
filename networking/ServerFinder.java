package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;


public class ServerFinder extends Thread {
    // OVERVIEW: Listens for messages from the server that identify an open chat
    // and dispatches a notification to any listeners registered with it.
	
    public interface ServerListener {
		// OVERVIEW: A class that implements ServerListener can be notified when
        // a server has been found, and passed the address of the server
        public void serverFound(SocketAddress address, String serverName, int numMembers, boolean needsPassword);
    }

	private MulticastSocket socket;
	private String multicastAddress;
	private int listenPort;
    private List<ServerListener> listeners;
	
	public ServerFinder(String multicastAddress, int listenPort)
		throws IOException {
		socket = new MulticastSocket(listenPort);
        socket.joinGroup(Inet4Address.getByName(multicastAddress));
        this.multicastAddress = multicastAddress;
        this.listenPort = listenPort;
        listeners = new ArrayList<ServerListener>();
		this.setDaemon(true);
	}
	
    public synchronized void addServerListener(ServerListener listener) {
        listeners.add(listener);
    }

    public synchronized void removeServerListener(ServerListener listener) {
        listeners.remove(listener);
    }

    public synchronized void fireServerFound(SocketAddress address, String serverName, int numMembers, boolean needsPassword) {
        for(ServerListener listener : listeners) {
            listener.serverFound(address, serverName, numMembers, needsPassword);
        }
    }

	@Override
	public void run() {
		
		try {
			while(!Thread.interrupted()) {
				System.out.println("ServerFinder: listening for " + this.multicastAddress + ":" + this.listenPort);
   
				DatagramPacket p = new DatagramPacket(new byte[1024], 1024);
				socket.receive(p);
				
                Message message = MessageParser.parse(p.getData());
            	System.out.println("ServerFinder: received a " + message.getType() + " message");
            	
                if(message.getType() == MessageType.ANNOUNCE) {
                    Announce announce = (Announce)message;
                    InetSocketAddress addr = new InetSocketAddress(p.getAddress(), announce.serverPort);
                    fireServerFound(addr, announce.serverName, announce.numMembers, announce.needsPassword);
                }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
