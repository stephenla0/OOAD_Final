package com.Final;
import com.Final.Commands.*;

import java.util.ArrayList;
import java.util.Collections;

public class Board implements LoggerWriter{
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
                loggerOutln(simulation.players.get(i)+" won $5,000 from their Long-Term Investment on space "+spin, simulation.logger);
                simulation.players.get(i).money+=5000;
                simulation.players.get(i).LTIhits++;
            }
        }
    }

    void move(int spin){
        checkLTI(spin);
        for(int i = 0; i < spin; i++){
            if(simulation.activePlayer.currentSpace.interruptMovement) simulation.activePlayer.currentSpace.card.executeCommand();
            if(simulation.activePlayer.currentSpace.message != null && simulation.activePlayer.currentSpace.message.equals("Retire!"))break;
            if(simulation.activePlayer.currentSpace.left == null && simulation.activePlayer.currentSpace.right == null){
                simulation.activePlayer.currentSpace = simulation.activePlayer.currentSpace.forward;
            }
            else{
                loggerOutln("Select which direction you want to go: (1: left, 2: forward, 3: right)", simulation.logger);
                ArrayList<Integer> restricted = new ArrayList<>();
                if(simulation.activePlayer.currentSpace.left != null) {
                    loggerOutln("1: "+simulation.activePlayer.currentSpace.left.message, simulation.logger);
                    restricted.add(1);
                }
                if(simulation.activePlayer.currentSpace.forward != null) {
                    loggerOutln("2: "+simulation.activePlayer.currentSpace.left.message, simulation.logger);
                    restricted.add(2);
                }
                if(simulation.activePlayer.currentSpace.right != null) {
                    loggerOutln("3: "+simulation.activePlayer.currentSpace.left.message, simulation.logger);
                    restricted.add(3);
                }
                int selection = simulation.input.getListSelectionRestricted(1, 3, restricted);
                switch(selection){
                    case 1:{
                        simulation.activePlayer.currentSpace=simulation.activePlayer.currentSpace.left;
                        loggerOutln(simulation.activePlayer.name+ " chose to go left on the "+simulation.activePlayer.currentSpace.message, simulation.logger);
                        break;
                    }
                    case 2:{
                        simulation.activePlayer.currentSpace=simulation.activePlayer.currentSpace.forward;
                        loggerOutln(simulation.activePlayer.name+ " chose to go forward on the "+simulation.activePlayer.currentSpace.message, simulation.logger);
                        break;
                    }
                    case 3:{
                        simulation.activePlayer.currentSpace=simulation.activePlayer.currentSpace.right;
                        loggerOutln(simulation.activePlayer.name+ " chose to go right on the "+simulation.activePlayer.currentSpace.message, simulation.logger);
                        break;
                    }
                }
            }
        }
        simulation.activePlayer.currentSpace.card.executeCommand();
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
        /*21*/boardSpaceCards.add( new BoardSpaceCard (new BuyLTICommand(simulation.receiver)) );
        /*22*/boardSpaceCards.add( new BoardSpaceCard (new CollectSTWcardCommand(simulation.receiver)) );
        /*23*/boardSpaceCards.add( new BoardSpaceCard (new FamilyOrNormalCommand(simulation.receiver)) );
        /*24*/boardSpaceCards.add( new BoardSpaceCard (new SafeOrRiskyCommand(simulation.receiver)) );

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
            case 21: return new BoardSpaceCard (new BuyLTICommand(simulation.receiver));
            case 22: return new BoardSpaceCard (new CollectSTWcardCommand(simulation.receiver));
            case 23: return new BoardSpaceCard (new FamilyOrNormalCommand(simulation.receiver));
            case 24: return new BoardSpaceCard (new SafeOrRiskyCommand(simulation.receiver));
            default: return null;
        }
    }

    BoardSpace createSpace(int num, boolean stop, BoardSpace prevSpace, int value, String message){
        BoardSpace newSpace = new BoardSpace(getCommand(num), stop);
        newSpace.value=value;
        newSpace.message=message;
        boardSpaces.add(newSpace);
        prevSpace.forward = newSpace;
        prevSpace=newSpace;
        return prevSpace;
    }

    void initializeBoard(){
        /* Based off the actual board */
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

        newSpace = createSpace(4, false, newSpace, 20000, "Scholarship! Collect $20,000.");
        newSpace = createSpace(5, false, newSpace, 5000, "School Supplies, Pay $5,000.");
        newSpace = createSpace(3, false, newSpace, 0, "Make new friends for life.");
        newSpace = createSpace(4, false, newSpace, 10000, "Part time job. Collect $10,000.");
        newSpace = createSpace(3, false, newSpace, 0, "Semester in London.");
        newSpace = createSpace(5, false, newSpace, 5000, "Spring Break in Florida. Pay $5,000.");
        newSpace = createSpace(3, false, newSpace, 0, "Honor Roll!");
        newSpace = createSpace(4, false, newSpace, 5000, "Receive a graduation gift. Collect $5,000.");
        newSpace = createSpace(3, false, newSpace, 0, "Graduation Day!");
        prevBranchSpace = createSpace(2, true, newSpace, 0, "STOP: College Career Choice");

        /*Career Path*/
        newSpace = new BoardSpace(getCommand(15), true);
        boardSpaces.add(newSpace);
        boardHead.right = newSpace;
        prevSpace=newSpace;

        newSpace = createSpace(6, false, newSpace, 0, "Pay Day");
        newSpace = createSpace(5, false, newSpace, 5000,"Rent apartment. Pay $5,000.");
        newSpace = createSpace(4, false, newSpace, 10000,"Inheritance, Collect $10,000.");
        newSpace = createSpace(6, false, newSpace, 0,"Pay Day");
        prevBranchSpace.forward=newSpace;

        /* continue straight */
        newSpace = createSpace(3, false, newSpace, 0,"Adopt a pet from animal shelter.");
        newSpace = createSpace(22, false, newSpace, 0, "Take a share of the wealth card.");
        newSpace = createSpace(3, false, newSpace, 0, "Get engaged.");
        newSpace = createSpace(5, false, newSpace, 5000, "Snowboarding accident!");
        newSpace = createSpace(4, false, newSpace, 10000, "Get early wedding gift.");
        newSpace = createSpace(3, false, newSpace, 0, "Volunteer at a soup kitchen.");
        newSpace = createSpace(3, false, newSpace, 0, "Engagement party.");
        newSpace = createSpace(6, false, newSpace, 0, "Pay Day");
        newSpace = createSpace(4, false, newSpace, 10000, "Win a race.");

        /* get married */
        newSpace = createSpace(17, true, newSpace, 0, "Get married!");

        /*continue straight */
        newSpace = createSpace(5, false, newSpace, 20000, "Wedding reception.");
        newSpace = createSpace(3, false, newSpace, 0, "Happy honeymoon!");
        newSpace = createSpace(22, false, newSpace, 0, "Take a share of the wealth card.");
        newSpace = createSpace(5, false, newSpace, 10000, "Car accident.");
        newSpace = createSpace(5, false, newSpace, 20000, "Job relocation.");
        newSpace = createSpace(7, false, newSpace, 0, "Pay Day with raise");
        newSpace = createSpace(8, false, newSpace, 0, "Taxes due!");

        /* buy starter home */
        newSpace = createSpace(13, true, newSpace, 0, "STOP: Buy a starter home.");

        /* continue straight */
        newSpace = createSpace(4, false, newSpace, 50000, "Win the lottery!");
        newSpace = createSpace(10, false, newSpace, 0, "Leave your job.");
        newSpace = createSpace(5, false, newSpace, 5000, "Buy plasma tv.");
        newSpace = createSpace(16, false, newSpace, 1, "Baby boy!");
        newSpace = createSpace(5, false, newSpace, 5000, "Finish babies room.");
        newSpace = createSpace(6, false, newSpace, 0, "Pay Day");
        newSpace = createSpace(16, false, newSpace, 1, "Baby girl!");
        newSpace = createSpace(3, false, newSpace, 0, "Vote.");
        newSpace = createSpace(4, false, newSpace, 100000, "Win the ultimate idol tv show.");
        newSpace = createSpace(16, false, newSpace, 2, "Twins!");
        newSpace = createSpace(5, false, newSpace, 20000, "Get the best seats at the big game.");
        newSpace = createSpace(3, false, newSpace, 0, "Attend hollywood movie premier.");
        newSpace = createSpace(16, false, newSpace, 1, "Baby girl!");
        newSpace = createSpace(22, false, newSpace, 0, "Take a share of the wealth card.");
        newSpace = createSpace(6, false, newSpace, 0, "Pay Day");
        newSpace = createSpace(3, false, newSpace, 0, "Learn sign language");
        newSpace = createSpace(12, false, newSpace, 0, "Lawsuit! Sue another player.");
        newSpace = createSpace(11, false, newSpace, 0, "Spin To Win!");
        newSpace = createSpace(10, false, newSpace, 0, "Lose your job.");

        /*return to school or continue path of life*/
        newSpace = createSpace(18, true, newSpace, 0, "STOP: Pay $50000 to return to school or continue on the path of life.");
        prevBranchSpace=newSpace;

        /* go back to school */
        newSpace = createSpace(5, false, newSpace, 5000, "Get a tutor.");
        prevBranchSpace.forward=null;
        prevBranchSpace.left=newSpace;
        newSpace = createSpace(5, false, newSpace, 5000, "Buy books and supplies.");
        newSpace = createSpace(4, false, newSpace, 20000, "Employer provides a scholarship.");
        newSpace = createSpace(5, false, newSpace, 10000, "Upgrade your computer.");
        newSpace = createSpace(3, false, newSpace, 0, "Join the honor society.");
        newSpace = createSpace(5, false, newSpace, 5000, "Go to a summer seminar.");

        newSpace = createSpace(19, true, newSpace, 0, "STOP: Choose new career or pay raise.");
        prevSpace=newSpace;

        /* continue on path of life */
        newSpace = createSpace(5, false, newSpace, 40000, "House flooded.");
        prevBranchSpace.right = newSpace;
        newSpace = createSpace(3, false, newSpace, 0, "In-laws visit.");
        newSpace = createSpace(7, false, newSpace, 0, "Pay Day with pay raise.");
        newSpace = createSpace(3, false, newSpace, 0, "Coach children's sports team.");
        newSpace = createSpace(16, false, newSpace, 2, "Adopt twins.");
        newSpace = createSpace(10, false, newSpace, 0, "Lose your job.");
        newSpace = createSpace(8, false, newSpace, 0, "Taxes due.");
        newSpace = createSpace(12, false, newSpace, 0, "Lawsuit! Sue another player.");
        /* merge branches */
        prevSpace.forward = newSpace;

        newSpace = createSpace(3, false, newSpace, 0, "Run for congress.");
        newSpace = createSpace(6, false, newSpace, 0, "Pay Day.");
        newSpace = createSpace(16, false, newSpace, 1, "Baby Boy!");
        newSpace = createSpace(5, false, newSpace, 25000, "Take family cruise vacation.");
        newSpace = createSpace(22, false, newSpace, 0, "Take a share of the wealth card.");
        newSpace = createSpace(4, false, newSpace, 100000, "Win on a tv gameshow.");
        newSpace = createSpace(12, false, newSpace, 0, "Lawsuit! Sue another player.");
        newSpace = createSpace(5, false, newSpace, 20000, "Art auction.");
        newSpace = createSpace(7, false, newSpace, 0, "Pay Day with raise.");
        newSpace = createSpace(3, false, newSpace, 0, "Visit the Grand Canyon.");
        newSpace = createSpace(8, false, newSpace, 0, "Taxes Due.");
        newSpace = createSpace(5, false, newSpace, 5000, "Sports camp for the kids.");
        newSpace = createSpace(5, false, newSpace, 40000, "Donate to african orphans.");
        newSpace = createSpace(12, false, newSpace, 0, "Lawsuit! Sue another player.");
        newSpace = createSpace(11, false, newSpace, 0, "Spin To Win.");
        newSpace = createSpace(5, false, newSpace, 40000, "Buy an SUV.");
        newSpace = createSpace(6, false, newSpace, 0, "Pay Day.");
        newSpace = createSpace(10, false, newSpace, 0, "Lose your job.");
        newSpace = createSpace(22, false, newSpace, 0, "Take a share of the wealth card.");
        newSpace = createSpace(5, false, newSpace, 100000, "TV dance show winner.");
        newSpace = createSpace(12, false, newSpace, 0, "Lawsuit! Sue another player.");
        newSpace = createSpace(5, false, newSpace, 5000, "Summer School.");

        /* branch between family life or regular */
        newSpace = createSpace(23, true, newSpace, 0, "STOP: Continue on the path of life or take the family path.");
        prevBranchSpace=newSpace;

        /*family path*/
        newSpace = createSpace(16, false, newSpace, 1, "Baby Girl.");
        prevBranchSpace.forward=null;
        prevBranchSpace.left = newSpace;
        newSpace = createSpace(4, false, newSpace, 5000, "Open Daycare.");
        newSpace = createSpace(6, false, newSpace, 0, "Pay Day.");
        newSpace = createSpace(16, false, newSpace, 1, "Baby Boy!");
        newSpace = createSpace(5, false, newSpace, 5000, "Get family physicals.");
        newSpace = createSpace(16, false, newSpace, 2, "Twins!");
        prevSpace=newSpace;

        /* regular*/
        newSpace = createSpace(5, false, newSpace, 30000, "Buy a home gym.");
        prevBranchSpace.right = newSpace;
        newSpace = createSpace(7, false, newSpace, 0, "Pay Day with raise.");
        newSpace = createSpace(3, false, newSpace, 0, "Learn CPR.");
        newSpace = createSpace(5, false, newSpace, 30000, "Buy foreign sports car.");
        newSpace = createSpace(11, false, newSpace, 0, "Spin To Win.");
        prevSpace.forward=newSpace;

        newSpace = createSpace(4, false, newSpace, 500000, "Find buried treasure.");
        newSpace = createSpace(16, false, newSpace, 1, "Baby boy.");
        newSpace = createSpace(6, false, newSpace, 0, "Pay Day.");
        newSpace = createSpace(5, false, newSpace, 120000, "Buy lakeside cabin.");
        newSpace = createSpace(12, false, newSpace, 0, "Lawsuit! Sue another player.");
        newSpace = createSpace(3, false, newSpace, 0, "Adopt a pet from the animal shelter.");
        newSpace = createSpace(4, false, newSpace, 100000, "Win nobel prize.");

        newSpace = createSpace(14, true, newSpace, 0, "STOP! Buy a better home.");

        newSpace = createSpace(5, false, newSpace, 125000, "Tornado hits house.");
        newSpace = createSpace(6, false, newSpace, 0, "Pay Day.");
        newSpace = createSpace(4, false, newSpace, 200000, "Write best selling book.");
        newSpace = createSpace(5, false, newSpace, 50000, "College!");
        newSpace = createSpace(12, false, newSpace, 0, "Lawsuit! Sue another player.");
        newSpace = createSpace(5, false, newSpace, 30000, "Buy a sailboat.");
        newSpace = createSpace(7, false, newSpace, 0, "Pay Day with raise.");
        newSpace = createSpace(8, false, newSpace, 0, "Tax refund.");

        newSpace = createSpace(24, true, newSpace, 0, "STOP: Continue on safe route or take risky road.");
        prevBranchSpace = newSpace;

        /*risky*/
        newSpace = createSpace(11, false, newSpace, 0, "Spin To Win.");
        prevBranchSpace.forward= null;
        prevBranchSpace.left = newSpace;
        newSpace = createSpace(11, false, newSpace, 0, "Spin To Win.");
        newSpace = createSpace(5, false, newSpace, 35000, "Sponsor sports tournament.");
        newSpace = createSpace(11, false, newSpace, 0, "Spin To Win.");
        newSpace = createSpace(11, false, newSpace, 100000, "Have cosmetic surgery.");
        newSpace = createSpace(11, false, newSpace, 0, "Spin To Win.");
        newSpace = createSpace(12, false, newSpace, 0, "Lawsuit! Sue another player.");
        newSpace = createSpace(11, false, newSpace, 0, "Spin To Win.");
        prevSpace=newSpace;

        /*safe*/
        newSpace = createSpace(5, false, newSpace, 25000, "Take family on a theme park vacation.");
        prevBranchSpace.right=newSpace;
        newSpace = createSpace(3, false, newSpace, 0, "Visit the pyramids in Egypt.");
        newSpace = createSpace(6, false, newSpace, 0, "Pay Day.");
        newSpace = createSpace(3, false, newSpace, 100000, "Visit old soldiers home.");
        newSpace = createSpace(5, false, newSpace, 80000, "Redecorate your home.");
        prevSpace.forward=newSpace;


        newSpace = createSpace(3, false, newSpace, 0, "You're a grandparent!");
        newSpace = createSpace(11, false, newSpace, 0, "Spin To Win.");
        newSpace = createSpace(6, false, newSpace, 0, "Pay Day.");
        newSpace = createSpace(5, false, newSpace, 125000, "Sponsor public arts exhibit.");
        newSpace = createSpace(3, false, newSpace, 0, "Host family reunion!");
        newSpace = createSpace(3, false, newSpace, 0, "You're a grandparent!.");
        newSpace = createSpace(5, false, newSpace, 65000, "Hire a maid and butler service");
        newSpace = createSpace(3, false, newSpace, 0, "Go hiking in the European Alps.");
        newSpace = createSpace(7, false, newSpace, 0, "Pay Day with raise.");
        newSpace = createSpace(11, false, newSpace, 0, "Spin To Win.");
        newSpace = createSpace(3, false, newSpace, 0, "Visit the great wall of China.");
        newSpace = createSpace(5, false, newSpace, 50000, "Have a life-saving operation.");
        newSpace = createSpace(12, false, newSpace, 0, "Lawsuit! Sue another player.");
        newSpace = createSpace(5, false, newSpace, 50000, "Have family website designed.");
        newSpace = createSpace(3, false, newSpace, 0, "You're a grandparent!");
        newSpace = createSpace(6, false, newSpace, 0, "Pay Day.");
        newSpace = createSpace(5, false, newSpace, 35000, "Host entertainment awards party.");
        newSpace = createSpace(4, false, newSpace, 50000, "Pension!");
        newSpace = createSpace(20, false, newSpace, 0, "Retire!");
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
