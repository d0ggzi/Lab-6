package commands;

import java.io.Serializable;

public class ServerMessage implements Serializable {
    private String messageText;

    public ServerMessage( String messageText) {
        this.messageText = messageText;
    }

    @Override
    public String toString() {
        return messageText;
    }
}
