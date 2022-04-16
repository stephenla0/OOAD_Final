package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class STWpayCommand implements Command {
    Receiver receiver;

    public STWpayCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.STWpay();
    }
}
