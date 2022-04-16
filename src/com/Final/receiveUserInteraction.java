package com.Final;

import java.util.Scanner;
import java.util.ArrayList;

public class receiveUserInteraction {
    Scanner input;
    Simulation simulation;
    int selection;
    boolean terminate;
    Receiver receiver;

    receiveUserInteraction(Scanner scan, Simulation simulation) {
        this.input = scan;
        this.simulation = simulation;
        this.receiver = new Receiver(simulation, input, this);
        this.terminate = false;
    }

    public int getListSelection(int min, int max) {
        selection = max + 1;
        while (selection < min || selection > max) {
            selection = input.nextInt();
        }
        return selection;
    }

    void printBooleanSelection(){
        System.out.println("");
        System.out.println("Please enter your selection: (y/n)");
    }

    boolean receiveBooleanSelection(){
        printBooleanSelection();
        boolean selectionMade = false;
        char selection_char = 'a';
        while(!selectionMade) {
            selection_char = input.next().charAt(0);
            if((selection_char == 'y') || (selection_char == 'n')) selectionMade = true;
            else printBooleanSelection();
        }
        if(selection_char == 'y') return true;

        return false;
    }
}
