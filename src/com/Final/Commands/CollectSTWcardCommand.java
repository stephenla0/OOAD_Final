package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class CollectSTWcardCommand implements Command {
    Receiver receiver;

    public CollectSTWcardCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.collectSTWcard();
    }
}
