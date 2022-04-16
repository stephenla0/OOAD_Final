package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class BuyStarterHomeCommand implements Command {
    Receiver receiver;

    public BuyStarterHomeCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.buyStarterHome();
    }
}
