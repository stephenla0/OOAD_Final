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

    void checkLTI(int spin){
        for(int i=0; i < simulation.players.size(); i++){
            if(simulation.players.get(i).LTI == spin){
                System.out.println(simulation.players.get(i)+" won $5,000 from their Long-Term Investment on space "+spin);
                simulation.players.get(i).money+=5000;
                simulation.players.get(i).LTIhits++;
            }
        }
    }

    void move(int spin){
        checkLTI(spin);
        for(int i = 0; i < spin; i++){
            BoardSpace currentSpace = simulation.activePlayer.currentSpace;
            if(currentSpace.interruptMovement) currentSpace.card.executeCommand();
            if(currentSpace.left == null && currentSpace.right == null){
                simulation.activePlayer.currentSpace = simulation.activePlayer.currentSpace.forward;
            }
            else{
                System.out.println("Select which direction you want to go: (1: left, 2: forward, 3: right)");
                ArrayList<Integer> restricted = new ArrayList<>();
                if(currentSpace.left != null) {
                    System.out.println("1: "+currentSpace.left.message);
                    restricted.add(1);
                }
                if(currentSpace.forward != null) {
                    System.out.println("2: "+currentSpace.left.message);
                    restricted.add(2);
                }
                if(currentSpace.right != null) {
                    System.out.println("3: "+currentSpace.left.message);
                    restricted.add(3);
                }
                int selection = simulation.input.getListSelectionRestricted(1, 3, restricted);
                switch(selection){
                    case 1:{
                        simulation.activePlayer.currentSpace=simulation.activePlayer.currentSpace.left;
                        System.out.println(simulation.activePlayer.name+ " chose to go left on the "+simulation.activePlayer.currentSpace.message);
                        break;
                    }
                    case 2:{
                        simulation.activePlayer.currentSpace=simulation.activePlayer.currentSpace.forward;
                        System.out.println(simulation.activePlayer.name+ " chose to go forward on the "+simulation.activePlayer.currentSpace.message);
                        break;
                    }
                    case 3:{
                        simulation.activePlayer.currentSpace=simulation.activePlayer.currentSpace.right;
                        System.out.println(simulation.activePlayer.name+ " chose to go right on the "+simulation.activePlayer.currentSpace.message);
                        break;
                    }
                }
            }
        }
        simulation.activePlayer.currentSpace.card.executeCommand();
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
            boardSpaces.add(new BoardSpace(boardSpaceCards.get(i), true));
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
        /* Initialization commands */
        /*0*/boardSpaceCards.add( new BoardSpaceCard (new StartFirstTurnCommand(simulation.receiver)) );
        /*1*/boardSpaceCards.add( new BoardSpaceCard (new StartCollegeCommand(simulation.receiver)) );
        /*2*/boardSpaceCards.add( new BoardSpaceCard (new GraduateCollegeCommand(simulation.receiver)) );

        /* Board Space landing commands */
        /*3*/boardSpaceCards.add( new BoardSpaceCard (new CollectLifeTileCommand(simulation.receiver)) );
        /*4*/boardSpaceCards.add( new BoardSpaceCard (new CollectMoneyCommand(simulation.receiver)) );
        /*5*/boardSpaceCards.add( new BoardSpaceCard (new PayMoneyCommand(simulation.receiver)) );
        /*6*/boardSpaceCards.add( new BoardSpaceCard (new PayDayCommand(simulation.receiver)) );
        /*7*/boardSpaceCards.add( new BoardSpaceCard (new PayDayRaiseCommand(simulation.receiver)) );
        /*8*/boardSpaceCards.add( new BoardSpaceCard (new PayTaxesCommand(simulation.receiver)) );
        /*9*/boardSpaceCards.add( new BoardSpaceCard (new CollectTaxRefundCommand(simulation.receiver)) );
        /*10*/boardSpaceCards.add( new BoardSpaceCard (new LoseJobCommand(simulation.receiver)) );
        /*11*/boardSpaceCards.add( new BoardSpaceCard (new SpinToWinCommand(simulation.receiver)) );
        /*12*/boardSpaceCards.add( new BoardSpaceCard (new SuePlayerCommand(simulation.receiver)) );

        /* Managing commands */
        /*13*/boardSpaceCards.add( new BoardSpaceCard (new BuyStarterHomeCommand(simulation.receiver)) );
        /*14*/boardSpaceCards.add( new BoardSpaceCard (new BuyHomeCommand(simulation.receiver)) );
        /*15*/boardSpaceCards.add( new BoardSpaceCard (new ChooseCareerCommand(simulation.receiver)) );
        /*16*/boardSpaceCards.add( new BoardSpaceCard (new GetBabyCommand(simulation.receiver)) );
        /*17*/boardSpaceCards.add( new BoardSpaceCard (new GetMarriedCommand(simulation.receiver)) );
        /*18*/boardSpaceCards.add( new BoardSpaceCard (new ReturnToSchoolCommand(simulation.receiver)) );
        /*19*/boardSpaceCards.add( new BoardSpaceCard (new GraduateNightSchoolCommand(simulation.receiver)) );
        /*20*/boardSpaceCards.add( new BoardSpaceCard (new RetireCommand(simulation.receiver)) );

    }

    BoardSpaceCard getCommand(int select){
        switch(select){
            case 0: return new BoardSpaceCard (new StartFirstTurnCommand(simulation.receiver));
            case 1: return new BoardSpaceCard (new StartCollegeCommand(simulation.receiver));
            case 2: return new BoardSpaceCard (new GraduateCollegeCommand(simulation.receiver));
            case 3: return new BoardSpaceCard (new CollectLifeTileCommand(simulation.receiver));
            case 4: return new BoardSpaceCard (new CollectMoneyCommand(simulation.receiver));
            case 5: return new BoardSpaceCard (new PayMoneyCommand(simulation.receiver));
            case 6: return new BoardSpaceCard (new PayDayCommand(simulation.receiver));
            case 7: return new BoardSpaceCard (new PayDayRaiseCommand(simulation.receiver));
            case 8: return new BoardSpaceCard (new PayTaxesCommand(simulation.receiver));
            case 9: return new BoardSpaceCard (new CollectTaxRefundCommand(simulation.receiver));
            case 10: return new BoardSpaceCard (new LoseJobCommand(simulation.receiver));
            case 11: return new BoardSpaceCard (new SpinToWinCommand(simulation.receiver));
            case 12: return new BoardSpaceCard (new SuePlayerCommand(simulation.receiver));
            case 13: return new BoardSpaceCard (new BuyStarterHomeCommand(simulation.receiver));
            case 14: return new BoardSpaceCard (new BuyHomeCommand(simulation.receiver));
            case 15: return new BoardSpaceCard (new ChooseCareerCommand(simulation.receiver));
            case 16: return new BoardSpaceCard (new GetBabyCommand(simulation.receiver));
            case 17: return new BoardSpaceCard (new GetMarriedCommand(simulation.receiver));
            case 18: return new BoardSpaceCard (new ReturnToSchoolCommand(simulation.receiver));
            case 19: return new BoardSpaceCard (new GraduateNightSchoolCommand(simulation.receiver));
            case 20: return new BoardSpaceCard (new RetireCommand(simulation.receiver));
            default: return null;
        }
    }

    BoardSpace createSpace(int num, boolean stop, BoardSpace prevSpace, int value){
        BoardSpace newSpace = new BoardSpace(getCommand(num), stop);
        newSpace.value=value;
        boardSpaces.add(newSpace);
        prevSpace.forward = newSpace;
        prevSpace=newSpace;
        return prevSpace;
    }

    void initializeBoard(){
        BoardSpace newSpace = new BoardSpace(boardSpaceCards.get(0), true);
        BoardSpace prevSpace;
        BoardSpace prevBranchSpace;
        boardSpaces.add(newSpace);
        boardHead = newSpace;

        /* College Path*/
        newSpace = new BoardSpace(getCommand(1), true);
        boardSpaces.add(newSpace);
        boardHead.left = newSpace;
        prevSpace=newSpace;

        newSpace = createSpace(4, false, newSpace, 10000);
        newSpace = createSpace(5, false, newSpace, 5000);
        newSpace = createSpace(3, false, newSpace, 0);
        newSpace = createSpace(4, false, newSpace, 14000);
        newSpace = createSpace(3, false, newSpace, 0);
        newSpace = createSpace(5, false, newSpace, 10000);
        newSpace = createSpace(3, false, newSpace, 0);
        newSpace = createSpace(4, false, newSpace, 20000);
        newSpace = createSpace(3, false, newSpace, 0);
        prevBranchSpace = createSpace(2, true, newSpace, 0);

        /*Career Path*/
        newSpace = new BoardSpace(getCommand(15), true);
        boardSpaces.add(newSpace);
        boardHead.right = newSpace;
        prevSpace=newSpace;

        newSpace = createSpace(6, false, newSpace, 0);
        newSpace = createSpace(5, false, newSpace, 5000);
        newSpace = createSpace(4, false, newSpace, 10000);
        newSpace = createSpace(6, false, newSpace, 0);
        prevBranchSpace.forward=newSpace;

        /* continue straight */
        newSpace = createSpace(3, false, newSpace, 0);
        newSpace = createSpace(5, false, newSpace, 5000);
        newSpace = createSpace(3, false, newSpace, 0);
        newSpace = createSpace(4, false, newSpace, 10000);
        newSpace = createSpace(5, false, newSpace, 15000);
        newSpace = createSpace(3, false, newSpace, 0);
        newSpace = createSpace(3, false, newSpace, 0);
        newSpace = createSpace(6, false, newSpace, 0);
        newSpace = createSpace(4, false, newSpace, 20000);

        newSpace = createSpace(17, true, newSpace, 0);

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
