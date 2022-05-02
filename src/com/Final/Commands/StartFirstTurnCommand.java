package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class StartFirstTurnCommand implements Command {
    Receiver receiver;

    public StartFirstTurnCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.startFirstTurn();
    }
}
