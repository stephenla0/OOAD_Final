package com.Final.Commands;

import com.Final.Command;
import com.Final.Receiver;

public class PayDayCommand implements Command {
    Receiver receiver;

    public PayDayCommand(Receiver receiver){
        this.receiver = receiver;
    }

    public void execute(){
        receiver.payDay();
    }
}
