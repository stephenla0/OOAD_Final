package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class RetireCommand implements Command {
    Receiver receiver;

    public RetireCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.retire();
    }
}
