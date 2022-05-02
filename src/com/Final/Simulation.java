package com.Final;
import com.Final.Commands.BuyLTICommand;
import com.Final.Commands.CollectSTWcardCommand;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class Simulation implements LoggerWriter{
    Board board;
    Receiver receiver;
    receiveUserInteraction input;
    ArrayList<Player> players;
    ArrayList<Player> retiredPlayers;
    Player activePlayer;
    Player cardUsePlayer;
    private int playerIndex;
    int round;
    Logger logger;

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
        logger=logger.getInstance();
        logger.startLog(round);

        //initializeTestPlayers(players);
    }

    void playIntroduction(){
        loggerOutln("Welcome to The Game of Life!", logger);
        loggerOutln("How many players are playing? (2-6)", logger);
        int playerCount = input.getListSelection(2,6);
        loggerOutln(playerCount+" players? Great! Let's get started.", logger);
        for(int i = 0; i < playerCount; i++){
            loggerOutln("Player "+(i+1)+", what is your name?", logger);
            String name = input.getName();
            loggerOutln("Your name is "+name+"? Great name!", logger);
            players.add(new Player(name, board.boardHead, this));
        }
        loggerOutln("Now that you're all here, let's set the game up!", logger);
        loggerOutln("Each of you will be getting three Share The Wealth Cards.", logger);
        loggerOutln("Be sure to be strategic in using these, as these can easily turn the tides of the game!", logger);
        loggerOutln("", logger);
        BoardSpace tempSpace = new BoardSpace(new BoardSpaceCard(new CollectSTWcardCommand(receiver)), false);
        for(int i = 0; i < players.size(); i++){
            activePlayer=players.get(i);
            loggerOutln(players.get(i).name+" received 3 Share The Wealth Cards.", logger);
            for(int j = 0; j < 3; j++){
                tempSpace.card.executeCommand();
            }
        }
        tempSpace = null;
        loggerOutln("", logger);
        loggerOutln("Now that you've all received your cards, let's determine the game order!", logger);
        Collections.shuffle(players);
        loggerOutln("The order has been shuffled, and will be as follows:", logger);
        for(int i = 0; i < players.size(); i++){
            loggerOutln((i+1)+": "+players.get(i).name, logger);
        }
        loggerOutln("", logger);
        loggerOutln("Lets begin the game!", logger);
        loggerOutln("", logger);
    }

    void initializeTestPlayers(ArrayList<Player> list){
        list.add(new Player("Player 1", board.boardHead, this));
        list.add(new Player("Player 2", board.boardHead, this));

        list.get(0).money = 100000;
        list.get(1).money = 100000;
        list.get(0).hasDegree = true;

        //board.initializeTestBoardCards();

        retiredPlayers.add(new Player("Player 3", board.boardHead, this));
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
        loggerOutln("",logger);
        loggerOutln(activePlayer.name + ", what would you like to do?", logger);
        loggerOutln("1: Roll", logger);
        loggerOutln("2: View Bank Account", logger);
        loggerOutln("3: View Share The Wealth Cards", logger);
        loggerOutln("4: View Life Tiles", logger);
        loggerOutln("5: View Career", logger);
        loggerOutln("6: View House", logger);
        loggerOutln("7: View Family", logger);
        if(activePlayer.LTI==0) {
            loggerOutln("8: Purchase Long-Term Investment", logger);
        }
        else{
            loggerOutln("8: View Long-Term Investment", logger);
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
        loggerOutln("You rolled a "+moveCount, logger);
        board.move(moveCount);
    }
    void viewBank(){
        loggerOutln("", logger);
        loggerOutln(activePlayer.name+" bank information:", logger);
        loggerOutln("Money: $"+activePlayer.money, logger);
        loggerOutln("Loans: "+activePlayer.numOfLoans, logger);
        loggerOutln("", logger);
        displayTurnOptions();
    }
    void viewSTW(){
        loggerOutln("", logger);
        loggerOutln(activePlayer.name+" Share The Wealth Cards:", logger);
        if(activePlayer.deckCards.isEmpty()) loggerOutln("No Cards Obtained.", logger);
        else {
            for (int i = 0; i < activePlayer.deckCards.size(); i++) {
                loggerOutln(activePlayer.deckCards.get(i).name + " ", logger);
            }
        }
        loggerOutln("", logger);
        displayTurnOptions();
    }
    void viewLifeTiles(){
        loggerOutln("", logger);
        loggerOutln(activePlayer.name+" LIFE Tiles:", logger);
        if(activePlayer.lifeTiles.isEmpty()) loggerOutln("No LIFE Tiles Obtained.", logger);
        else {
            for (int i = 0; i < activePlayer.lifeTiles.size(); i++) {
                loggerOutln("$" + activePlayer.lifeTiles.get(i).value + " ", logger);
            }
        }
        loggerOutln("", logger);
        displayTurnOptions();
    }
    void viewCareer(){
        loggerOutln("", logger);
        loggerOutln(activePlayer.name+" Career Information:", logger);
        if(activePlayer.career==null){
            loggerOutln("No Career Obtained.", logger);
            loggerOutln("", logger);
            displayTurnOptions();
        }
        if(activePlayer.career.needsCollege)loggerOutln("College Degree: Yes", logger);
        else loggerOutln("College Degree: No", logger);
        loggerOutln("Career: "+activePlayer.career.name, logger);
        loggerOutln("Salary: "+activePlayer.career.salary, logger);
        if(activePlayer.career.maxSalary==-1)loggerOutln("Maximum Salary: None", logger);
        else loggerOutln("Maximum Salary: "+activePlayer.career.maxSalary, logger);
        loggerOutln("Taxes: "+activePlayer.career.taxes, logger);
        loggerOutln("Number of Raises: "+activePlayer.career.numOfRaises, logger);
        loggerOutln("", logger);
        displayTurnOptions();
    }
    void viewHouse(){
        loggerOutln("", logger);
        loggerOutln(activePlayer.name+" House Information:", logger);
        if(activePlayer.career==null){
            loggerOutln("No Career Obtained.", logger);
            loggerOutln("", logger);
            displayTurnOptions();
        }
        loggerOutln("House: "+activePlayer.house.name, logger);
        if(activePlayer.house.starterHouse)loggerOutln("Starter House: Yes", logger);
        else loggerOutln("Starter House: No", logger);
        loggerOutln("Purchase Price: "+activePlayer.house.purchasePrice, logger);
        loggerOutln("Sell Price: "+activePlayer.house.sellPrice, logger);
        loggerOutln("", logger);
        displayTurnOptions();
    }
    void viewFamily(){
        loggerOutln("", logger);
        loggerOutln(activePlayer.name+" Family Information:", logger);
        if(activePlayer.hasSpouse)loggerOutln("Married: Yes", logger);
        else loggerOutln("Married: No", logger);
        loggerOutln("Children: "+activePlayer.children, logger);
        loggerOutln("", logger);
        displayTurnOptions();
    }

    void viewLTI(){
        loggerOutln("", logger);
        loggerOutln(activePlayer.name+" Long-Term Investment Information:", logger);
        loggerOutln("Current investment number: "+activePlayer.LTI, logger);
        loggerOutln("Times investment has been rolled: "+activePlayer.LTIhits, logger);
        loggerOutln("Money gained from investment: $"+((5000*activePlayer.LTIhits)-10000), logger);
        loggerOutln("", logger);
        displayTurnOptions();
    }

    void purchaseLTI(){
        loggerOutln("", logger);
        loggerOutln("Would you like to purchase a Long-Term Investment for $10,000?", logger);
        loggerOutln("Each time somebody rolls the number associated with your investment, you earn $5,000 from the bank!", logger);
        loggerOutln("However, each number can only be bought once, so make sure to buy the number you want fast!", logger);
        boolean selection = input.receiveBooleanSelection();
        if(selection){
            BoardSpace tempSpace = new BoardSpace(new BoardSpaceCard(new BuyLTICommand(receiver)), false);
            tempSpace.card.executeCommand();
            tempSpace=null;
        }
        else{
            loggerOutln("You declined to buy a Long-Term Investment.", logger);
        }
        loggerOutln("", logger);
        displayTurnOptions();
    }

    void newPlayerTurn(){
        newActivePlayer();
        loggerOutln("", logger);
        loggerOutln("It is now " + activePlayer.name + " turn", logger);
        displayTurnOptions();
    }

    void newActivePlayer(){
        playerIndex++;
        if(playerIndex >= players.size()){
            round++;
            playerIndex = 0;
            logger.close();
            logger.startLog(round);
        }
        activePlayer = players.get(playerIndex);
    }
}
