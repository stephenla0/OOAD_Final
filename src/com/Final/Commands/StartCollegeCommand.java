package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class StartCollegeCommand implements Command {
    Receiver receiver;

    public StartCollegeCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.startCollege();
    }
}
