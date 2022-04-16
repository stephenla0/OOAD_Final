package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class STWcollectCommand implements Command {
    Receiver receiver;

    public STWcollectCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.STWcollect();
    }
}
