package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class SafeOrRiskyCommand implements Command {
    Receiver receiver;

    public SafeOrRiskyCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.safeOrRisky();
    }
}
