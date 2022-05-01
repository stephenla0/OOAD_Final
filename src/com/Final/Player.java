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
    int LTI;
    ArrayList<Integer> currentSTWtokens;
    boolean retiredAtEstates;
    BoardSpace currentSpace;
    int finalScore;

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
        LTI = 0;
        currentSTWtokens = new ArrayList<>();
        retiredAtEstates = false;
        finalScore=0;
    }

    void setCurrentSpace(BoardSpace currentSpace){
        this.currentSpace = currentSpace;
    }

    void takeLoan(){
        System.out.println(name + " needed to take a loan.");
        money += 10000;
        numOfLoans ++;
        System.out.println("Current loans taken: " + numOfLoans);
    }

    void pay(int money){
        boolean afford = false;
        if(this.money > money) afford = true;
        while(!afford){
            takeLoan();
            if(this.money > money) afford = true;
        }
    }

    void repayLoans(){
        System.out.println(name + " is repaying "+numOfLoans+" loans for $"+(numOfLoans*25000));
        money-=(numOfLoans*25000);
    }
}
