package networking;

public class Server {
// DUMMY CLASS
    String serverName;
    int serverPort;
    int numMembers;
    boolean needsPassword;

    public Server(String serverName, int serverPort, boolean needsPassword) {
    	this.serverName = serverName;
    	this.serverPort = serverPort;
    	this.numMembers = 0;
    	this.needsPassword = needsPassword;
    }
    public synchronized int getNumMembers() {
        return numMembers;
    }
    
    public synchronized void setNumMembers(int numMembers) {
    	this.numMembers = numMembers;
    }
}
