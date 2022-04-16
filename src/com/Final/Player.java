package com.Final;
import java.util.ArrayList;


public class Player {
    String name;
    int money;
    boolean hasDegree;
    Career career;
    ArrayList<LifeTile> lifeTiles;
    House house;
    boolean hasSpouse;
    int children;
    ArrayList<DeckCard> deckCards;
    int numOfLoans;

    BoardSpace currentSpace;

    Player(String name, BoardSpace currentSpace){
        this.name = name;
        money = 0;
        hasDegree = false;
        career = null;
        lifeTiles = new ArrayList<>();
        house = null;
        hasSpouse = false;
        children = 0;
        deckCards = new ArrayList<>();
        numOfLoans = 0;
        this.currentSpace = currentSpace;
    }

    void setCurrentSpace(BoardSpace currentSpace){
        this.currentSpace = currentSpace;
    }
}
