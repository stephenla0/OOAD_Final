package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class GraduateCollegeCommand implements Command {
    Receiver receiver;

    public GraduateCollegeCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.graduateCollege();
    }
}
