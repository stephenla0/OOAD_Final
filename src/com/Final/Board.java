package com.Final;
import com.Final.Commands.*;

import java.util.ArrayList;
import java.util.Collections;

public class Board {
    Simulation simulation;
    ArrayList<BoardSpace> boardSpaces;
    ArrayList<BoardSpaceCard> boardSpaceCards;
    BoardSpace boardHead;
    ArrayList<Career> availableCareers;
    ArrayList<Career> availableCollegeCareers;
    ArrayList<House> availableHouses;
    ArrayList<House> availableStarterHouses;
    ArrayList<LifeTile> availableLifeTiles;
    ArrayList<DeckCard> availableDeckCards;
    ArrayList<Integer> availableLTI;

    Board(Simulation simulation){
        this.simulation = simulation;
        boardSpaces = new ArrayList<>();
        boardSpaceCards = new ArrayList<>();
        boardHead = null;
        availableCareers = new ArrayList<>();
        availableCollegeCareers = new ArrayList<>();
        availableHouses = new ArrayList<>();
        availableStarterHouses = new ArrayList<>();
        availableLifeTiles = new ArrayList<>();
        availableDeckCards = new ArrayList<>();
        availableLTI = new ArrayList<>();

        initializeLTI();
        initializeHouses();
        initializeCareers();
        initializeSTW();
        initializeLifeTiles();
        initializeBoardSpaceCards();
        initializeBoard();
        /*initializeTestBoardSpaceCards();
        initializeTestBoard();
        initializeTestHouses();
        initializeTestLifeTiles();
        initializeTestCareers();*/

        initializeShuffle();
    }

    int spinWheel(){
        return Utility.rndFromRange(1, 10);
    }

    BoardSpace move(BoardSpace space, int spin){
        for(int i = 0; i < spin; i++){
            space = space.forward;
        }
        return space;
    }

    String selectPath(){
        return null;
    }

    void initializeLTI(){
        for(int i = 1; i < 11; i++){
            availableLTI.add(i);
        }
    }

    void initializeTestBoardSpaceCards(){
        boardSpaceCards.add( new BoardSpaceCard (new BuyStarterHomeCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new BuyHomeCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new BuyHomeCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new CollectLifeTileCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new ChooseCareerCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new PayDayCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new PayDayRaiseCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new PayTaxesCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new CollectTaxRefundCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new TakeLoanCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new CollectSTWcardCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new BuyLTICommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new CollectMoneyCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new PayMoneyCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new CollectMoneyCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new PayMoneyCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new SpinToWinCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new SpinToWinCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new GetMarriedCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new GetBabyCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new ChooseCareerCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new LoseJobCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new SuePlayerCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new ReturnToSchoolCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new GraduateNightSchoolCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new CollectLifeTileCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new RetireCommand(simulation.receiver)) );

        /** developing
         **/
    }
    void initializeTestBoard(){
        for(int i = 0; i < boardSpaceCards.size(); i++){
            boardSpaces.add(new BoardSpace(boardSpaceCards.get(i), "black", true));
            boardSpaces.get(i).value=7000;
        }
        for(int i = 0; i < boardSpaceCards.size() - 1; i++){
            boardSpaces.get(i).forward=boardSpaces.get(i+1);
        }
        boardHead = boardSpaces.get(0);
    }

    void initializeTestHouses(){
        for (int i = 0; i < 6; i++){
            availableStarterHouses.add(new House("Starter House " + (i + 1), 100, 200, true));
        }

        for (int i = 0; i < 6; i++){
            availableHouses.add(new House("House " + (i + 1), 200, 400, false));
        }
    }

    void initializeHouses(){
        availableStarterHouses.add(new House("Mobile Home", 80000, 80000, true));
        availableStarterHouses.add(new House("Condo", 100000, 105000, true));
        availableStarterHouses.add(new House("Ranch Style", 140000, 160000, true));
        availableStarterHouses.add(new House("Log Cabin", 120000, 140000, true));
        availableStarterHouses.add(new House("Small Cape", 160000, 180000, true));
        availableStarterHouses.add(new House("Tudor Style", 180000, 200000, true));

        availableHouses.add(new House("Double Wide +RV", 300000, 300000, false));
        availableHouses.add(new House("Executive Cape", 400000, 400000, false));
        availableHouses.add(new House("Modern Victorian", 500000, 500000, false));
        availableHouses.add(new House("Luxury Mountain Retreat", 600000, 600000, false));
        availableHouses.add(new House("Penthouse Suite", 700000, 700000, false));
        availableHouses.add(new House("Mansion", 800000, 800000, false));

    }

    void initializeCareers(){
        availableCareers.add(new Career("Salesperson", 20000, 50000, 5000, false));
        availableCareers.add(new Career("Hair Stylist", 30000, 60000, 10000, false));
        availableCareers.add(new Career("Mechanic", 30000, 60000, 10000, false));
        availableCareers.add(new Career("Police Officer", 40000, 70000, 15000, false));
        availableCareers.add(new Career("Entertainer", 50000, -1, 20000, false));
        availableCareers.add(new Career("Athlete", 60000, -1, 25000, false));

        availableCollegeCareers.add(new Career("Teacher", 40000, 70000, 15000, true));
        availableCollegeCareers.add(new Career("Computer Designer", 50000, 80000, 20000, true));
        availableCollegeCareers.add(new Career("Accountant", 70000, 110000, 30000, true));
        availableCollegeCareers.add(new Career("Veterinarian", 80000, 120000, 35000, true));
        availableCollegeCareers.add(new Career("Lawyer", 90000, -1, 40000, true));
        availableCollegeCareers.add(new Career("Doctor", 100000, -1, 45000, true));
    }

    void initializeSTW(){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                availableDeckCards.add(new DeckCard(j));
            }
        }
        for(int i = 0; i < 5; i++){
            availableDeckCards.add(new DeckCard(4));
        }
    }

    void initializeLifeTiles(){
        for(int i = 0; i < 7; i++){
            availableLifeTiles.add(new LifeTile(10000));
            if(i<6)availableLifeTiles.add(new LifeTile(20000));
            if(i<5)availableLifeTiles.add(new LifeTile(30000));
            if(i<4)availableLifeTiles.add(new LifeTile(40000));
            if(i<3)availableLifeTiles.add(new LifeTile(50000));
        }
    }

    void initializeBoardSpaceCards(){
        boardSpaceCards.add( new BoardSpaceCard (new BuyHomeCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new BuyLTICommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new BuyStarterHomeCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new ChooseCareerCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new CollectLifeTileCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new CollectMoneyCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new CollectSTWcardCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new CollectTaxRefundCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new GetBabyCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new GetMarriedCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new GraduateNightSchoolCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new LoseJobCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new PayDayCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new PayDayRaiseCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new PayMoneyCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new PayTaxesCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new RetireCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new ReturnToSchoolCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new SpinToWinCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new StartCollegeCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new StartFirstTurnCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new SuePlayerCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new TakeLoanCommand(simulation.receiver)) );



    }

    void initializeBoard(){

    }

    void initializeTestLifeTiles(){
        for (int i = 1; i < 6; i++){
            availableLifeTiles.add(new LifeTile(100*i));
        }
    }

    void initializeTestCareers(){
        for (int i = 1; i < 6; i++){
            availableCareers.add(new Career("Career " + (i+1), 100*i, 10000, 50*i, false));
        }
        for (int i = 1; i < 6; i++){
            availableCollegeCareers.add(new Career("College Career " + (i+1), 200*i, 10000, 100*i, true));
        }
    }

    void initializeTestBoardCards(){
        for(int i = 0; i < simulation.players.size(); i++){
            Player player = simulation.players.get(i);
            for(int j = 0; j < 5; j++){
                player.deckCards.add(new DeckCard(j));
                availableDeckCards.add(new DeckCard(j));
            }
        }
    }

    void initializeShuffle(){
        Collections.shuffle(availableCareers);
        Collections.shuffle(availableCollegeCareers);
        Collections.shuffle(availableHouses);
        Collections.shuffle(availableStarterHouses);
        Collections.shuffle(availableLifeTiles);
        Collections.shuffle(availableDeckCards);
    }

}
