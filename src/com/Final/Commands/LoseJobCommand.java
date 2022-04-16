package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class LoseJobCommand implements Command {
    Receiver receiver;

    public LoseJobCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.loseJob();
    }
}
