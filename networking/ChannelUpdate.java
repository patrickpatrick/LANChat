
package networking;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

public class ChannelUpdate implements Message {
    // OVERVIEW: A ChannelUpdate is a packet containing a message, the handle of
    // the client that sent it, and the time it was recieved by the server
    // The binary format is:
    // int: Indicates the type of packet (PacketType.CHANNEL_UPDATE)
    // int: Length of the client handle string in bytes
    // [handleLength] bytes: The client's handle name as a string
    // int: Length of the text message
    // [messageLength] bytes: The message string
    // long: The time the message was received by the server as a UNIX timestamp

    private MessageType type = MessageType.CHANNEL_UPDATE;
    public String clientHandle;
    public String message;
    public Date date;

    // constructors
    public ChannelUpdate(String clientHandle, String message, Date date) {
        // REQUIRES: clientHandle is not null, message is not null
        // EFFECTS: Constructs a new TextMessage with the given data
        this.clientHandle = clientHandle;
        this.message = message;
        this.date = date;
    }

    public ChannelUpdate(DataInputStream stream) throws IOException {
        // REQUIRES: stream is not null
        // EFFECTS: Parses a new TextMessage from the given stream, or throws IOException
        // if there was a problem parsing the required fields
        clientHandle = MessageParser.readString(stream);
        message = MessageParser.readString(stream);
        date = new Date(stream.readLong());
    }

    public byte[] getBytes() throws IOException {
        // EFFECTS: Returns the binary representation of this TextMessage as a byte array
        ByteArrayOutputStream byte_out = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byte_out);

        out.writeInt(type.id);
        MessageParser.writeString(out, clientHandle);
        MessageParser.writeString(out, message);
        out.writeLong(date.getTime());
        out.flush();
        
        return byte_out.toByteArray();
    }

    @Override
    public MessageType getType() {
        // EFFECTS: returns the type of this packet
        return type;
    }
}
