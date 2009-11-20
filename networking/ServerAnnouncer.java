package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;

public class ServerAnnouncer extends Thread {

    MulticastSocket socket;
    InetSocketAddress address;
    int port;
    int delay;
    Server server;

    public ServerAnnouncer(String multicastAddress, int announcePort, int delay, Server server) throws IOException {
        socket = new MulticastSocket(announcePort);
        socket.joinGroup(Inet4Address.getByName(multicastAddress));
        socket.setTimeToLive(32);
        this.setDaemon(true);
        this.address = new InetSocketAddress(multicastAddress, announcePort);
        this.delay = delay;
        this.server = server;
    }

    @Override
    public void run() {
        while(!this.isInterrupted()) {
            try {
                Announce announce = new Announce(server.serverName, socket.getLocalAddress().getHostName(), server.serverPort, server.getNumMembers(), server.needsPassword);
                byte[] data = announce.getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length, address);
                System.out.println("ServerAnnouncer: sending announce to " + packet.getAddress().getHostName() + ":" + packet.getPort());
                socket.send(packet);
            } catch (IOException ex) {
                System.out.println("ServerAnnouncer: failed sending announce: " + ex);
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                break;
            }
        }
    }

}
