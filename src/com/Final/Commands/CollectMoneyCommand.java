package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class CollectMoneyCommand implements Command {
    Receiver receiver;

    public CollectMoneyCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.collectMoney();
    }
}
