package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class PayMoneyCommand implements Command {
    Receiver receiver;

    public PayMoneyCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.payMoney();
    }
}
