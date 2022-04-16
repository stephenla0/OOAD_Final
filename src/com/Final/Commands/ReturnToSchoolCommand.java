package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class ReturnToSchoolCommand implements Command {
    Receiver receiver;

    public ReturnToSchoolCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.returnToSchool();
    }
}
