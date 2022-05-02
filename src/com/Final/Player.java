package com.Final;
import java.util.ArrayList;


public class Player implements LoggerWriter{
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
    int LTIhits;
    ArrayList<Integer> currentSTWtokens;
    boolean retiredAtEstates;
    BoardSpace currentSpace;
    int finalScore;
    Simulation simulation;

    Player(String name, BoardSpace currentSpace, Simulation simulation){
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
        LTIhits = 0;
        this.simulation = simulation;
    }

    void setCurrentSpace(BoardSpace currentSpace){
        this.currentSpace = currentSpace;
    }

    void takeLoan(){
        loggerOutln(name + " needed to take a loan.", simulation.logger);
        money += 10000;
        numOfLoans ++;
        loggerOutln("Current loans taken: " + numOfLoans, simulation.logger);
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
        loggerOutln(name + " is repaying "+numOfLoans+" loans for $"+(numOfLoans*25000), simulation.logger);
        money-=(numOfLoans*25000);
    }
}
