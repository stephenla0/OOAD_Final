package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class FamilyOrNormalCommand implements Command {
    Receiver receiver;

    public FamilyOrNormalCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.familyOrNormal();
    }
}
