package com.Final.Commands;

import com.Final.Receiver;
import com.Final.Command;

public class CollectSTWcardCommand implements Command {
    Receiver receiver;

    public CollectSTWcardCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.collectSTWcard();
    }
}
