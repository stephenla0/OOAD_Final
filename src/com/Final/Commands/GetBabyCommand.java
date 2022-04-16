package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class GetBabyCommand implements Command {
    Receiver receiver;

    public GetBabyCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.getBaby();
    }
}
