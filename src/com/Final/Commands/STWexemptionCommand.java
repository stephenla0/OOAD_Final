package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class STWexemptionCommand implements Command {
    Receiver receiver;

    public STWexemptionCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.STWexemption();
    }
}
