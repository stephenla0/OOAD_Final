package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class BuyHomeCommand implements Command {
    Receiver receiver;

    public BuyHomeCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.buyHome();
    }
}
