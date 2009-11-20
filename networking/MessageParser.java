package networking;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MessageParser {
    // OVERVIEW: A factory class that parses the correct type of Packet from
    // a byte array based on the first 4 bytes

    public static Message parse(byte[] data) throws IOException {
        // REQUIRES: data is not null
        // EFFECTS: parses and constructs a new Message from data, or throws 
        // IOException if there was a parsing problem.
        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data));
        int messageType = stream.readInt();
        System.out.println("Message type field is " + messageType);
        switch (MessageType.get(messageType)) {
            case TEXT_MESSAGE:
                return new TextMessage(stream);
            case CHANNEL_UPDATE:
                return new ChannelUpdate(stream);
            case ANNOUNCE:
            	return new Announce(stream);
            case JOIN:
            	return new Join(stream);
            case REFUSE:
            	return new Refuse(stream);
        }
        return new UnknownMessage(stream);
    }

    public static String readString(DataInputStream stream) throws IOException {
        // REQUIRES: stream is not null
        // EFFECTS: reads a 32-bit integer length from the stream, then
        // reads length bytes from the stream and returns it as a String. Throws
        // IOException if unsuccessful in reading all bytes, or if the length field
        // is greater than the MTU (1024)
        int length = stream.readInt();

        if (length > 1024) {
            throw new IOException("Length of string (" + length + ") is greater than MTU");
        }

        byte[] data = new byte[length];
        stream.read(data, 0, length);
        return new String(data, "UTF-16");
    }

    public static void writeString(DataOutputStream stream, String string) throws IOException {
        // REQUIRES: stream and string are not null
        // EFFECTS: Writes the UTF-16 representation of string to stream
        byte[] bin = string.getBytes("UTF-16");
        stream.writeInt(bin.length);
        stream.write(bin);
    }
}
