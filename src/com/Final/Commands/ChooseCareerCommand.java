package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class ChooseCareerCommand implements Command {
    Receiver receiver;

    public ChooseCareerCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.chooseCareer();
    }
}
