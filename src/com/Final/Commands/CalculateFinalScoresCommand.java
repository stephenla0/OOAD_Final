package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class CalculateFinalScoresCommand implements Command {
    Receiver receiver;

    public CalculateFinalScoresCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.calculateFinalScores();
    }
}
