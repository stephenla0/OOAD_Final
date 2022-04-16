package com.Final.Commands;

import com.Final.Receiver;
import com.Final.Command;

public class CollectLifeTileCommand implements Command {
    Receiver receiver;

    public CollectLifeTileCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.collectLifeTile();
    }
}
