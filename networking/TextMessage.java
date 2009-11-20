
package networking;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TextMessage implements Message {
    // OVERVIEW: A TextMessage is a packet containing the handle of the client that
    // sent it and a short text message.
    // The binary format is:
    // int: Indicates the type of packet (PacketType.TEXT_MESSAGE)
    // int: Length of the password in bytes
    // [passwordLength] bytes: The server password
    // int: Length of the client handle string in bytes
    // [handleLength] bytes: The client's handle name as a string
    // int: Length of the text message
    // [messageLength] bytes: The message string

    private MessageType type = MessageType.TEXT_MESSAGE;
    public String password;
    public String clientHandle;
    public String message;

    // constructors
    public TextMessage(String clientHandle, String message, String password) {
        // REQUIRES: clientHandle is not null, message is not null
        // EFFECTS: Constructs a new TextMessage with the given data
        this.password = password;
        this.clientHandle = clientHandle;
        this.message = message;
    }

    public TextMessage(DataInputStream stream) throws IOException {
        // REQUIRES: stream is not null
        // EFFECTS: Parses a new TextMessage from the given stream, or throws IOException
        // if there was a problem parsing the required fields
        password = MessageParser.readString(stream);
        clientHandle = MessageParser.readString(stream);
        message = MessageParser.readString(stream);
    }

    @Override
    public byte[] getBytes() throws IOException {
        // EFFECTS: Returns the binary representation of this TextMessage as a byte array
        ByteArrayOutputStream byte_out = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byte_out);

        out.writeInt(type.id);
        MessageParser.writeString(out, password);
        MessageParser.writeString(out, clientHandle);
        MessageParser.writeString(out, message);
        out.flush();
        
        return byte_out.toByteArray();
    }

    @Override
    public MessageType getType() {
        // EFFECTS: returns the type of this packet
        return type;
    }
}
