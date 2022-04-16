package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class SpinToWinCommand implements Command {
    Receiver receiver;

    public SpinToWinCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.spinToWin();
    }
}
