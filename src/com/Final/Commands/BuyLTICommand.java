package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class BuyLTICommand implements Command {
    Receiver receiver;

    public BuyLTICommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.buyLTI();
    }
}
