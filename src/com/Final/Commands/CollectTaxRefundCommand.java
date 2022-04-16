package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class CollectTaxRefundCommand implements Command {
    Receiver receiver;

    public CollectTaxRefundCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.collectTaxRefund();
    }
}
