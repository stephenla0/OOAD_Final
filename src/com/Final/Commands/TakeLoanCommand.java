package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class TakeLoanCommand implements Command {
    Receiver receiver;

    public TakeLoanCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.takeLoan();
    }
}
