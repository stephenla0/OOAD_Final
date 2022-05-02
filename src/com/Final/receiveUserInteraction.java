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

    public int getListSelectionRestricted(int min, int max, ArrayList<Integer> restrictions){
        if(restrictions == null || restrictions.isEmpty()){
            return getListSelection(min, max);
        }
        selection = max + 1;
        boolean selected = false;
        while (!selected) {
            boolean restricted = false;
            selection = input.nextInt();
            if(selection >= min && selection <= max){
                for(int i = 0; i < restrictions.size(); i++){
                    if(restrictions.get(i) == selection){
                        restricted = true;
                    }
                }
                if(!restricted){
                    selected = true;
                }
            }
            if(!selected) System.out.println("Please enter a valid selection.");
        }
        return selection;
    }

    public int getListSelection(int min, int max) {
        selection = max + 1;
        while (selection < min || selection > max) {
            selection = input.nextInt();
            if(selection < min || selection > max) System.out.println("Please enter a valid selection.");
        }
        input.nextLine();
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

    String getName(){
        String name = input.nextLine();
        for(int i = 0; i < simulation.players.size(); i++){
            if(name.equals(simulation.players.get(i).name)){
                System.out.println("Sorry, but no duplicate names are allowed. Please enter a new name.");
                getName();
            }
        }
        return name;
    }
}
