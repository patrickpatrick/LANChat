
package networking;

import java.io.IOException;

public interface Message {

    public MessageType getType();

    public byte[] getBytes() throws IOException;
}
