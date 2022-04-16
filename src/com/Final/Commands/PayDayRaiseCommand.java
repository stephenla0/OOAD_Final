package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class PayDayRaiseCommand implements Command {
    Receiver receiver;

    public PayDayRaiseCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.payDayRaise();
    }
}
