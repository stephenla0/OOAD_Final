package com.Final;

public class DeckCard {
    String name;
    DeckCard(int selection){
        switch(selection){
            case 0: name = "Collect Card"; break;
            case 1: name = "Pay Card"; break;
            case 2: name = "Exemption Card"; break;
            case 3: name = "Spin to Win (2)"; break;
            case 4: name = "Spin to Win (4)"; break;
            default: name = "Error"; break;
        }

    }
}
