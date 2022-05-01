package com.Final;
import com.Final.Commands.BuyLTICommand;
import com.Final.Commands.CollectSTWcardCommand;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class Simulation {
    Board board;
    Receiver receiver;
    receiveUserInteraction input;
    ArrayList<Player> players;
    ArrayList<Player> retiredPlayers;
    Player activePlayer;
    Player cardUsePlayer;
    private int playerIndex;
    int round;

    Simulation(Scanner scan){
        input = new receiveUserInteraction(scan, this);
        receiver = new Receiver(this, scan, input);
        board = new Board(this);
        playerIndex = -1;
        players = new ArrayList<>();
        retiredPlayers = new ArrayList<>();
        activePlayer = null;
        cardUsePlayer = null;
        round = 1;

        //initializeTestPlayers(players);
    }

    void playIntroduction(){
        System.out.println("Welcome to The Game of Life!");
        System.out.println("How many players are playing? (2-6)");
        int playerCount = input.getListSelection(2,6);
        System.out.println(playerCount+" players? Great! Let's get started.");
        for(int i = 0; i < playerCount; i++){
            System.out.println("Player "+(i+1)+", what is your name?");
            String name = input.getName();
            System.out.println("Your name is "+name+"? Great name!");
            players.add(new Player(name, board.boardHead));
        }
        System.out.println("Now that you're all here, let's set the game up!");
        System.out.println("Each of you will be getting three Share The Wealth Cards.");
        System.out.println("Be sure to be strategic in using these, as these can easily turn the tides of the game!");
        System.out.println("");
        BoardSpace tempSpace = new BoardSpace(new BoardSpaceCard(new CollectSTWcardCommand(receiver)), false);
        for(int i = 0; i < players.size(); i++){
            activePlayer=players.get(i);
            System.out.println(players.get(i).name+" received 3 Share The Wealth Cards.");
            for(int j = 0; j < 3; j++){
                tempSpace.card.executeCommand();
            }
        }
        tempSpace = null;
        System.out.println("");
        System.out.println("Now that you've all received your cards, let's determine the game order!");
        Collections.shuffle(players);
        System.out.println("The order has been shuffled, and will be as follows:");
        for(int i = 0; i < players.size(); i++){
            System.out.println((i+1)+": "+players.get(i).name);
        }
        System.out.println("");
        System.out.println("Lets begin the game!");
        System.out.println("");
    }

    void initializeTestPlayers(ArrayList<Player> list){
        list.add(new Player("Player 1", board.boardHead));
        list.add(new Player("Player 2", board.boardHead));

        list.get(0).money = 100000;
        list.get(1).money = 100000;
        list.get(0).hasDegree = true;

        board.initializeTestBoardCards();

        retiredPlayers.add(new Player("Player 3", board.boardHead));
        retiredPlayers.get(0).lifeTiles.add(board.availableLifeTiles.get(0));
        retiredPlayers.get(0).retiredAtEstates=true;
        board.availableLifeTiles.remove(0);
    }

    void startSim(){
        playIntroduction();
        while(!players.isEmpty()){
            newPlayerTurn();
        }
    }

    void startTestSim(){
        for(int i = 0; i < (2*board.boardSpaceCards.size()); i++){
            newPlayerTurn();
        }
    }

    void displayTurnOptions(){
        int selection;
        System.out.println(activePlayer.name + ", what would you like to do?");
        System.out.println("1: Roll");
        System.out.println("2: View Bank Account");
        System.out.println("3: View Share The Wealth Cards");
        System.out.println("4: View Life Tiles");
        System.out.println("5: View Career");
        System.out.println("6: View House");
        System.out.println("7: View Family");
        if(activePlayer.LTI==0) {
            System.out.println("8: Purchase Long-Term Investment");
        }
        else{
            System.out.println("8: View Long-Term Investment");
        }
            selection = input.getListSelection(1,8);
        switch(selection){
            case 1: roll(); break;
            case 2: viewBank(); break;
            case 3: viewSTW(); break;
            case 4: viewLifeTiles(); break;
            case 5: viewCareer(); break;
            case 6: viewHouse();break;
            case 7: viewFamily();break;
            case 8: {
                if(activePlayer.LTI == 0) {purchaseLTI(); break;}
                else viewLTI(); break;
            }
        }
    }

    void roll(){
        int moveCount = board.spinWheel();
        System.out.println("You rolled a "+moveCount);
        board.move(moveCount);
    }
    void viewBank(){
        System.out.println("");
        System.out.println(activePlayer.name+" bank information:");
        System.out.println("Money: $"+activePlayer.money);
        System.out.println("Loans: "+activePlayer.numOfLoans);
        System.out.println("");
        displayTurnOptions();
    }
    void viewSTW(){
        System.out.println("");
        System.out.println(activePlayer.name+" Share The Wealth Cards:");
        if(activePlayer.deckCards.isEmpty()) System.out.println("No Cards Obtained.");
        else {
            for (int i = 0; i < activePlayer.deckCards.size(); i++) {
                System.out.println(activePlayer.deckCards.get(i).name + " ");
            }
        }
        System.out.println("");
        displayTurnOptions();
    }
    void viewLifeTiles(){
        System.out.println("");
        System.out.println(activePlayer.name+" LIFE Tiles:");
        if(activePlayer.lifeTiles.isEmpty()) System.out.println("No LIFE Tiles Obtained.");
        else {
            for (int i = 0; i < activePlayer.lifeTiles.size(); i++) {
                System.out.println("$" + activePlayer.lifeTiles.get(i).value + " ");
            }
        }
        System.out.println("");
        displayTurnOptions();
    }
    void viewCareer(){
        System.out.println("");
        System.out.println(activePlayer.name+" Career Information:");
        if(activePlayer.career==null){
            System.out.println("No Career Obtained.");
            System.out.println("");
            displayTurnOptions();
        }
        if(activePlayer.career.needsCollege)System.out.println("College Degree: Yes");
        else System.out.println("College Degree: No");
        System.out.println("Career: "+activePlayer.career.name);
        System.out.println("Salary: "+activePlayer.career.salary);
        if(activePlayer.career.maxSalary==-1)System.out.println("Maximum Salary: None");
        else System.out.println("Maximum Salary: "+activePlayer.career.maxSalary);
        System.out.println("Taxes: "+activePlayer.career.taxes);
        System.out.println("Number of Raises: "+activePlayer.career.numOfRaises);
        System.out.println("");
        displayTurnOptions();
    }
    void viewHouse(){
        System.out.println("");
        System.out.println(activePlayer.name+" House Information:");
        if(activePlayer.career==null){
            System.out.println("No Career Obtained.");
            System.out.println("");
            displayTurnOptions();
        }
        System.out.println("House: "+activePlayer.house.name);
        if(activePlayer.house.starterHouse)System.out.println("Starter House: Yes");
        else System.out.println("Starter House: No");
        System.out.println("Purchase Price: "+activePlayer.house.purchasePrice);
        System.out.println("Sell Price: "+activePlayer.house.sellPrice);
        System.out.println("");
        displayTurnOptions();
    }
    void viewFamily(){
        System.out.println("");
        System.out.println(activePlayer.name+" Family Information:");
        if(activePlayer.hasSpouse)System.out.println("Married: Yes");
        else System.out.println("Married: No");
        System.out.println("Children: "+activePlayer.children);
        System.out.println("");
        displayTurnOptions();
    }

    void viewLTI(){
        System.out.println("");
        System.out.println(activePlayer.name+" Long-Term Investment Information:");
        System.out.println("Current investment number: "+activePlayer.LTI);
        System.out.println("Times investment has been rolled: "+activePlayer.LTIhits);
        System.out.println("Money gained from investment: $"+((5000*activePlayer.LTIhits)-10000));
        System.out.println("");
        displayTurnOptions();
    }

    void purchaseLTI(){
        System.out.println("");
        System.out.println("Would you like to purchase a Long-Term Investment for $10,000?");
        System.out.println("Each time somebody rolls the number associated with your investment, you earn $5,000 from the bank!");
        System.out.println("However, each number can only be bought once, so make sure to buy the number you want fast!");
        boolean selection = input.receiveBooleanSelection();
        if(selection){
            BoardSpace tempSpace = new BoardSpace(new BoardSpaceCard(new BuyLTICommand(receiver)), false);
            tempSpace.card.executeCommand();
            tempSpace=null;
        }
        else{
            System.out.println("You declined to buy a Long-Term Investment.");
        }
        System.out.println("");
        displayTurnOptions();
    }

    void newPlayerTurn(){
        newActivePlayer();
        System.out.println("");
        System.out.println("It is now " + activePlayer.name + " turn");
        displayTurnOptions();
    }

    void newActivePlayer(){
        playerIndex++;
        if(playerIndex >= players.size()){
            round++;
            playerIndex = 0;
        }
        activePlayer = players.get(playerIndex);
    }
}
