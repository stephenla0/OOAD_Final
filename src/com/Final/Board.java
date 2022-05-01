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

        initializeTestBoardSpaceCards();
        initializeTestBoard();
        initializeTestHouses();
        initializeTestLifeTiles();
        initializeTestCareers();

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
        /*boardSpaceCards.add( new BoardSpaceCard (new BuyStarterHomeCommand(simulation.receiver)) );
        boardSpaceCards.add( new BoardSpaceCard (new BuyHomeCommand(simulation.receiver)) );
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
        boardSpaceCards.add( new BoardSpaceCard (new RetireCommand(simulation.receiver)) );*/
        boardSpaceCards.add( new BoardSpaceCard (new CollectLifeTileCommand(simulation.receiver)) );

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

    void initializeTestLifeTiles(){
        for (int i = 1; i < 6; i++){
            availableLifeTiles.add(new LifeTile(100*i));
        }
    }

    void initializeTestCareers(){
        for (int i = 1; i < 6; i++){
            availableCareers.add(new Career("Career " + (i+1), 100*i, 50*i, false));
        }
        for (int i = 1; i < 6; i++){
            availableCollegeCareers.add(new Career("College Career " + (i+1), 200*i, 100*i, true));
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
