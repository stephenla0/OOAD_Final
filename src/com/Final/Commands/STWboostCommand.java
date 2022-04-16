package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class STWboostCommand implements Command {
    Receiver receiver;

    public STWboostCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.STWboost();
    }
}
