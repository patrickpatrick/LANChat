
package networking;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Join implements Message {

    private MessageType type = MessageType.JOIN;
    public String clientHandle;
    public String password;

    // constructors
    public Join(String clientHandle, String password) {
        // REQUIRES: clientHandle is not null, password is not null
        // EFFECTS: Constructs a new Join with the given data
        this.clientHandle = clientHandle;
        this.password = password;
    }

    public Join(DataInputStream stream) throws IOException {
        // REQUIRES: stream is not null
        // EFFECTS: Parses a new Join from the given stream, or throws IOException
        // if there was a problem parsing the required fields
        clientHandle = MessageParser.readString(stream);
        password = MessageParser.readString(stream);
    }

    public byte[] getBytes() throws IOException {
        // EFFECTS: Returns the binary representation of this Join as a byte array
        ByteArrayOutputStream byte_out = new ByteArrayOutputStream();
        DataOutputStream stream = new DataOutputStream(byte_out);

        stream.writeInt(type.id);
        MessageParser.writeString(stream, clientHandle);
        MessageParser.writeString(stream, password);
        stream.flush();
        
        return byte_out.toByteArray();
    }

    @Override
    public MessageType getType() {
        // EFFECTS: returns the type of this packet
        return type;
    }
}
