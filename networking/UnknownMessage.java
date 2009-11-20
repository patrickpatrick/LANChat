
package networking;

import java.io.DataInputStream;
import java.io.IOException;

public class UnknownMessage implements Message {

    // constructors
    public UnknownMessage(DataInputStream stream) throws IOException {

    }

    public byte[] getBytes() throws IOException {
        throw new IOException("Can't construct this packet");
    }

    public MessageType getType() {
        // EFFECTS: returns the type of this packet
        return MessageType.UNKNOWN;
    }
}
