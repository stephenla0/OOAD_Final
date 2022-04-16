package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class SuePlayerCommand implements Command {
    Receiver receiver;

    public SuePlayerCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.suePlayer();
    }
}
