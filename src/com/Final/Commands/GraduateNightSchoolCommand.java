package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class GraduateNightSchoolCommand implements Command {
    Receiver receiver;

    public GraduateNightSchoolCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.graduateNightSchool();
    }
}
