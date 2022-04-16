package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class PayTaxesCommand implements Command {
    Receiver receiver;

    public PayTaxesCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.payTaxes();
    }
}
