package com.Final;

public class BoardSpaceCard {
    String name;
    Command command;

    BoardSpaceCard(Command command){
        this.command = command;
    }

    public void executeCommand(){
        command.execute();
    }
}
