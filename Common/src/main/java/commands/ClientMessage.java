package commands;

import commands.Command;
import organizations.Organization;

import java.io.Serializable;
import java.util.Arrays;

public class ClientMessage implements Serializable {
    public Command command;
    public String arg;
    public Organization org;

    public ClientMessage(Command c) {
        this.command = c;
    }

    public ClientMessage(Command c, String arg) {
        this.command = c;
        this.arg = arg;
    }

    public ClientMessage(Command c, Organization obj) {
        this.command = c;
        this.org = obj;
    }

    public ClientMessage(Command c, String arg, Organization obj) {
        this.command = c;
        this.arg = arg;
        this.org = obj;
    }


    @Override
    public String toString() {
        return "command=" + command +
                ", arg=" + arg;
    }
}
