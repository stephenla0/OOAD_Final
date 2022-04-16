package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class GetMarriedCommand implements Command {
    Receiver receiver;

    public GetMarriedCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.getMarried();
    }
}
