package com.Final;

import java.lang.reflect.Array;
import java.lang.Math;
import java.util.Scanner;
import java.util.ArrayList;

public class Receiver {
    Simulation sim;
    Scanner scan;
    receiveUserInteraction input;

    Receiver(Simulation sim, Scanner scan, receiveUserInteraction input){
        this.sim = sim;
        this.scan = scan;
        this.input = input;
    }

    private boolean purchase(int money){
        if(sim.activePlayer.money >= money){
            sim.activePlayer.money -= money;
            return true;
        }
        return false;
    }

    private void sell(int money){
        sim.activePlayer.money += money;
    }

    public void collectLifeTile(){
        if(!sim.board.availableLifeTiles.isEmpty()){
            LifeTile tile = sim.board.availableLifeTiles.get(Utility.rndFromRange(0, sim.board.availableLifeTiles.size()-1));
            sim.activePlayer.lifeTiles.add(tile);
            System.out.println("");
            System.out.println(sim.activePlayer.name+" collected a LIFE Tile worth $" + tile.value);
            return;
        }
        else if(!sim.retiredPlayers.isEmpty()){
            ArrayList<Integer> indexes = new ArrayList<>();
            for(int i = 0; i < sim.retiredPlayers.size(); i++){
                if(sim.retiredPlayers.get(i).retiredAtEstates) indexes.add(i);
            }
            if(indexes.isEmpty()){
                System.out.println("There are no more LIFE Tiles left and no people to steal from.");
                return;
            }
            System.out.println("There are no more LIFE Tiles left but there are people to steal from.");
            System.out.println("Please enter your selection to steal from.");
            for(int i = 0; i<indexes.size(); i++){
                System.out.println((i+1)+": "+sim.retiredPlayers.get(indexes.get(i)).name);
            }
            int selection =
        }
    }

    public void payTaxes(){
        printBalance(sim.activePlayer);
        int tax = sim.activePlayer.career.taxes;
        sim.activePlayer.money -= tax;
        System.out.println(sim.activePlayer.name+" had to pay $" + tax + " in taxes.");
        printBalance(sim.activePlayer);
    }

    public void collectTaxRefund(){
        printBalance(sim.activePlayer);
        int tax = sim.activePlayer.career.taxes;
        sim.activePlayer.money += tax;
        System.out.println(sim.activePlayer.name+" got $" + tax + " in a tax refund.");
        printBalance(sim.activePlayer);
    }

    public void loseJob(){
        System.out.println(sim.activePlayer.name+" lost their job "+sim.activePlayer.career.name);
        sim.activePlayer.career.numOfRaises=0;
        if(sim.activePlayer.hasDegree){
            sim.board.availableCollegeCareers.add(sim.activePlayer.career);
        }
        else{
            sim.board.availableCareers.add(sim.activePlayer.career);
        }
        sim.activePlayer.career=null;
        chooseCareer();
    }

    public void getBaby(){
        if(!sim.activePlayer.hasSpouse){
            System.out.println(sim.activePlayer.name+" can not have a baby since you do not have a spouse");
            return;
        }
        sim.activePlayer.children++;
        System.out.println(sim.activePlayer.name+" and your spouse had a baby! "+sim.activePlayer.name+ " now have " + sim.activePlayer.children + " children.");
    }

    private void getPaid(){
        printBalance(sim.activePlayer);
        int pay = (sim.activePlayer.career.salary + (sim.activePlayer.career.numOfRaises * 100));
        sim.activePlayer.money += pay;
        System.out.println(sim.activePlayer.name+" got paid $" + pay);
        printBalance(sim.activePlayer);
    }

    public void payDay(){
        getPaid();
    }
    public void payDayRaise(){
        sim.activePlayer.career.numOfRaises++;
        getPaid();
    }
    /** Everything for choosing a career ************************************************************************************/

    private ArrayList<Career> getCareerOptions(boolean hasDegree){
        ArrayList<Career> careerOptions = new ArrayList<>();
        while (careerOptions.size() < 3){
            boolean inList = false;

            Career career;
            if(hasDegree) {
                career = sim.board.availableCollegeCareers.get(Utility.rndFromRange(0, sim.board.availableCollegeCareers.size()-1));
            }
            else{
                career = sim.board.availableCareers.get(Utility.rndFromRange(0, sim.board.availableCareers.size()-1));
            }

            for(int i = 0; i < careerOptions.size(); i++){
                if(careerOptions.get(i) == career) inList = true;
            }
            if(!inList) careerOptions.add(career);
        }
        return careerOptions;
    }

    private void selectCareer(ArrayList<Career> careers){
        System.out.println("Please enter your selection: ");
        for (int i = 0; i < careers.size(); i++) {
            System.out.println((i+1) + ": " + careers.get(i).name + " with a salary of $" + careers.get(i).salary
            + " and taxes of $" + careers.get(i).taxes);
        }
        Career selectedCareer = careers.get((input.getListSelection(1, 3)-1));
        System.out.println(sim.activePlayer.name+" chose " + selectedCareer.name);
        sim.activePlayer.career=selectedCareer;
        if(selectedCareer.needsCollege){
            sim.board.availableCollegeCareers.remove(selectedCareer);
        }
        else{
            sim.board.availableCareers.remove(selectedCareer);
        }
    }

    public void chooseCareer(){
        ArrayList<Career> careers = getCareerOptions(sim.activePlayer.hasDegree);
        selectCareer(careers);
    }

    /****************************************************************************************/

    /** Everything for buying a house ************************************************************************************/

    private ArrayList<House> getHouseOptions(boolean isStarter){
        ArrayList<House> houseOptions = new ArrayList<>();
        while (houseOptions.size() < 3){
            boolean inList = false;

            House house;
            if(isStarter) {
                house = sim.board.availableStarterHouses.get(Utility.rndFromRange(0, sim.board.availableStarterHouses.size()-1));
            }
            else{
                house = sim.board.availableHouses.get(Utility.rndFromRange(0, sim.board.availableHouses.size()-1));
            }

            for(int i = 0; i < houseOptions.size(); i++){
                if(houseOptions.get(i) == house) inList = true;
            }
            if(!inList) houseOptions.add(house);
        }
        return houseOptions;
    }

    private boolean checkIfAffordAnyHouses(ArrayList<House> houseOptions){
        for (int i = 0; i < houseOptions.size(); i++){
            if(sim.activePlayer.money >= houseOptions.get(i).purchasePrice) return true;
        }
        return false;
    }

    private void selectHouse(ArrayList<House> houseOptions, boolean canAfford){
        if(!canAfford){
            printBalance(sim.activePlayer);
            System.out.println("Can not afford any home options.");
            return;
        }
        boolean purchased = false;
        House selectedHouse = null;
        while (!purchased) {
            System.out.println("");
            printBalance(sim.activePlayer);
            System.out.println("Please enter your selection: ");
            for (int i = 0; i < houseOptions.size(); i++) {
                System.out.println((i+1) + ": " + houseOptions.get(i).name + " for $" + houseOptions.get(i).purchasePrice);
            }
            selectedHouse = houseOptions.get((input.getListSelection(1, 3)-1));
            purchased = purchase(selectedHouse.purchasePrice);
            if(!purchased){
                System.out.println("Unable to afford selection. Please enter new selection.");
            }
            System.out.println(sim.activePlayer.name+" successfully purchased " + selectedHouse.name + " for $" + selectedHouse.purchasePrice);
            printBalance(sim.activePlayer);
        }
        sim.activePlayer.house=selectedHouse;
        if(selectedHouse.starterHouse){
            sim.board.availableStarterHouses.remove(selectedHouse);
        }
        else{
            sim.board.availableHouses.remove(selectedHouse);
        }
    }

    private boolean canBuyHouse(){
        if(sim.activePlayer.house != null){
            System.out.println("");
            System.out.println("Would "+sim.activePlayer.name+" like to sell your house?");
            System.out.println("Current House: " + sim.activePlayer.house.name + " is worth $" + sim.activePlayer.house.sellPrice);
            boolean selection = input.receiveBooleanSelection();
            if(selection){
                sell(sim.activePlayer.house.sellPrice);
                if(sim.activePlayer.house.starterHouse){
                    sim.board.availableStarterHouses.add(sim.activePlayer.house);
                }
                else{
                    sim.board.availableHouses.add(sim.activePlayer.house);
                }
                System.out.println("");
                System.out.println(sim.activePlayer.name+" successfully sold " + sim.activePlayer.house.name + " for $" + sim.activePlayer.house.sellPrice);
                printBalance(sim.activePlayer);
                sim.activePlayer.house=null;
                return true;
            }
            System.out.println("");
            System.out.println(sim.activePlayer.name+" declined to sell " + sim.activePlayer.house.name + " for $" + sim.activePlayer.house.sellPrice);
            return false;
        }
        return true;
    }


    public void buyStarterHome(){
        ArrayList<House> houses = getHouseOptions(true);
        boolean canAfford = checkIfAffordAnyHouses(houses);
        selectHouse(houses, canAfford);
    }
    public void buyHome(){
        boolean canBuyHouse = canBuyHouse();
        if(!canBuyHouse) return;
        ArrayList<House> houses = getHouseOptions(false);
        boolean canAfford = checkIfAffordAnyHouses(houses);
        selectHouse(houses, canAfford);
    }

     /******************************************************************************************/
    public void returnToSchool(){
        System.out.println(sim.activePlayer.name+" would you like to return to school? It costs $50,000.");
        System.out.println("Once you graduate, you will receive a degree if you do not already have one. This will make you eligible for college careers.");
        System.out.println("Additionally, you may choose between a new career or a $20,000 raise.");
        printBalance(sim.activePlayer);
        boolean selection = input.receiveBooleanSelection();
        if(!selection){
            System.out.println(sim.activePlayer.name+" declined to return to school.");
            return;
        }
        System.out.println(sim.activePlayer.name+" decided to return to school for $50,000.");
        sim.activePlayer.pay(50000);
        printBalance(sim.activePlayer);
    }
    public void graduateNightSchool(){
        System.out.println(sim.activePlayer.name+" graduated school and received a degree!");
        sim.activePlayer.hasDegree=true;
        System.out.println(sim.activePlayer.name+" would you like a new college career or a $20,000 raise?");
        System.out.println("1: New college career");
        System.out.println("2: $20,000 raise");
        int selection = input.getListSelection(1,2);
        if(selection==1){
            System.out.println(sim.activePlayer+" chose a new college career!");
            loseJob();
        }
        else{
            System.out.println(sim.activePlayer+" chose a $20,000 raise!");
            sim.activePlayer.career.numOfRaises+=2;
        }
    }
    public void getMarried(){
        System.out.println(sim.activePlayer.name+" got married!");
        collectLifeTile();
        sim.activePlayer.hasSpouse = true;
        int spin = Utility.rndFromRange(1, 10);
        System.out.println(sim.activePlayer.name+" spun a "+spin);
        if(spin >= 8){
            System.out.println(sim.activePlayer.name+" collect $10,000 from each player!");
            collectFromEachPlayer(10000);
        }
        else if (spin <= 7 && spin >= 5){
            System.out.println(sim.activePlayer.name+" collect $5,000 from each player!");
            collectFromEachPlayer(5000);
        }
        else{
            System.out.println(sim.activePlayer.name+" got nothing from each player!");
        }
    }
    public void suePlayer(){
        System.out.println(sim.activePlayer.name+" can sue another player for $100,000");
        System.out.println(sim.activePlayer.name+" choose who you want to sue");
        ArrayList<Integer> active = new ArrayList<>();
        for(int i = 0; i < sim.players.size(); i++){
            if(sim.players.get(i).name == sim.activePlayer.name){
                active.add(i+1);
            }
            else System.out.println((i+1) +": " +sim.players.get(i).name);
        }
        int selection = input.getListSelectionRestricted(1, sim.players.size(), active)-1;

        boolean exempt = STWexemption(sim.activePlayer);
        if(exempt) return;

        System.out.println(sim.activePlayer.name+" sued "+sim.players.get(selection).name+ " for $100,000.");
        sim.players.get(selection).pay(100000);
        printBalance(sim.activePlayer);
        printBalance(sim.players.get(selection));
    }
    public void takeLoan(){
        sim.activePlayer.numOfLoans++;
        sim.activePlayer.money += 200;
        System.out.println(sim.activePlayer.name+" had to take a loan.");
        printBalance(sim.activePlayer);
        System.out.println(sim.activePlayer.name+" now has " +sim.activePlayer.numOfLoans + "loans");
    }
    public void collectSTWcard(){
        if(sim.board.availableDeckCards.size() == 0){
            System.out.println(sim.activePlayer.name+" could not collect a Share The Wealth card as no more are available.");
            return;
        }
        System.out.println(sim.activePlayer.name+" collected a Share The Wealth Card.");
        sim.activePlayer.deckCards.add(sim.board.availableDeckCards.get(0));
        sim.activePlayer.deckCards.remove(0);
    }

    private void removeBoostCard(int boost){
        for(int i = 0; i < sim.cardUsePlayer.deckCards.size(); i++){
            if(sim.cardUsePlayer.deckCards.get(i).name.equals("Spin to Win (" +boost+ ")")){
                System.out.println(sim.cardUsePlayer.name+" used a " +boost+ " token boost card.");
                sim.board.availableDeckCards.add(sim.cardUsePlayer.deckCards.get(i));
                sim.cardUsePlayer.deckCards.remove(i);
                return;
            }
        }
    }
    private int STWboost(){
        boolean hasTwoCard = false;
        boolean hasFourCard = false;
        for(int i = 0; i < sim.cardUsePlayer.deckCards.size(); i++){
            if(sim.cardUsePlayer.deckCards.get(i).name == "Spin to Win (2)"){
                hasTwoCard = true;
            }
            if(sim.cardUsePlayer.deckCards.get(i).name == "Spin to Win (4)"){
                hasFourCard = true;
            }
        }

        if(!hasTwoCard && !hasFourCard){
            return 0;
        }
        System.out.println("Would " +sim.cardUsePlayer.name+ " like to use a Spin To Win boost card?");
        boolean boost = input.receiveBooleanSelection();
        if(!boost){
            System.out.println(sim.cardUsePlayer.name+" declined to use a boost card.");
            return 0;
        }
        if (hasTwoCard && !hasFourCard){
            removeBoostCard(2);
            return 2;
        }
        if (!hasTwoCard && hasFourCard){
            removeBoostCard(4);
            return 4;
        }
        else{
            System.out.println("Please enter which boost card to use");
            System.out.println("1: 2 tokens");
            System.out.println("2: 4 tokens");
            int select = input.getListSelection(1,2);
            if(select == 1){
                removeBoostCard(2);
                return 2;
            }
            else{
                removeBoostCard(4);
                return 4;
            }
        }
    }

    public void spinToWin(){
        ArrayList<Integer> bets = new ArrayList<>();
        for(int i = 0; i < sim.players.size(); i++){
            sim.cardUsePlayer = sim.players.get(i);
            sim.cardUsePlayer.currentSTWtokens.clear();

            System.out.println("How much would "+sim.cardUsePlayer.name+ " like to wager? ($0-$50000)");
            int bet = input.getListSelection(0, 50000);
            bets.add(bet);
            sim.cardUsePlayer.money -= bet;
            System.out.println(sim.cardUsePlayer.name+ " wagered $" + bet);
            printBalance(sim.cardUsePlayer);

            int boost = STWboost();
            int tokens = 1;
            if(boost != 0){
                tokens = tokens * boost;
            }
            for(int j = 0; j < tokens; j++){
                System.out.println("Please select the number to place a token on");
                for(int k = 1; k <= 10; k++){
                    boolean inPicks = false;
                    for(int p = 0; p < sim.cardUsePlayer.currentSTWtokens.size(); p++){
                        if(k == sim.cardUsePlayer.currentSTWtokens.get(p)) inPicks = true;
                    }
                    if(!inPicks) System.out.print(k+" ");
                }
                System.out.println("");
                int selection = input.getListSelectionRestricted(1, 10, sim.cardUsePlayer.currentSTWtokens);
                System.out.println(sim.cardUsePlayer.name+ " placed a token on space " + selection);
                sim.cardUsePlayer.currentSTWtokens.add(selection);
            }
        }

        int spin = sim.board.spinWheel();
        System.out.println("The number spun was "+spin);
        for(int i = 0; i < sim.players.size(); i++){
            Player player = sim.players.get(i);
            boolean won = false;
            for(int j = 0; j < player.currentSTWtokens.size(); j++){
                if(spin == player.currentSTWtokens.get(j)) won = true;
            }
            if(!won){
                System.out.println(player.name+" did not win");
                printBalance(player);
            }
            else{
                System.out.println(player.name+" won! They receive $"+(10*bets.get(i)));
                player.money += (10*bets.get(i));
                printBalance(player);
            }
        }
    }

    private boolean checkSTWeligibleValue(){
        if(sim.activePlayer.currentSpace.value <= 5000) return false;
        return true;
    }
    private int[] divideValue(){
        int[] values = new int[2];
        int value = sim.activePlayer.currentSpace.value;
        values[0]=(value/2); //less if odd for card player
        values[1]=(value/2 + value%2); //more if odd
        return values;
    }

    private void STWcollect(){
        boolean check = checkSTWeligibleValue();
        if(!check){
            System.out.println(sim.cardUsePlayer.name+" can not use a Collect card for this space. The value must be greater than $5,000");
            return;
        }
        System.out.println(sim.cardUsePlayer.name+" decided to use a collect card.");

        boolean exempt = STWexemption(sim.activePlayer);
        if(exempt) return;

        int[] values = divideValue();
        sim.cardUsePlayer.money += values[0];
        sim.activePlayer.money -= values[1];
        System.out.println(sim.cardUsePlayer.name+" used a Collect card on "+sim.activePlayer.name+" and collected $"+values[0]
        +" from the space while "+sim.activePlayer.name+" only collected $"+values[1]);
        printBalance(sim.cardUsePlayer);
        printBalance(sim.activePlayer);

        for(int i = 0; i < sim.cardUsePlayer.deckCards.size(); i++){
            if(sim.cardUsePlayer.deckCards.get(i).name == "Collect Card"){
                sim.board.availableDeckCards.add(sim.cardUsePlayer.deckCards.get(i));
                sim.cardUsePlayer.deckCards.remove(i);
                break;
            }
        }
    }
    private void STWpay(){
        boolean check = checkSTWeligibleValue();
        if(!check){
            System.out.println(sim.activePlayer.name+" can not use a Pay card for this space. The value must be greater than $5,000");
            return;
        }
        ArrayList<Integer> restricted = new ArrayList<>();
        System.out.println(sim.activePlayer.name+" decided to use a pay card.");
        System.out.println("Please select the player "+sim.activePlayer.name+" wish to use your Pay card on.");
        System.out.println("Available options are: ");
        for(int i = 0; i < sim.players.size(); i++){
            if(sim.activePlayer != sim.players.get(i)) {
                System.out.println((i+1)+":"+sim.players.get(i).name);
            }
            else{
                restricted.add(i+1);
            }
        }
        int selection = input.getListSelectionRestricted(1, sim.players.size(), restricted);
        Player selectedPlayer = sim.players.get(selection-1);

        boolean exempt = STWexemption(selectedPlayer);
        if(exempt) return;

        int[] values = divideValue();
        sim.activePlayer.money += values[0];
        selectedPlayer.money -= values[1];
        System.out.println(sim.activePlayer.name+" used a Pay card on "+ selectedPlayer.name+" and paid $"+values[0]
                +" to the bank while "+selectedPlayer.name+" paid $"+values[1]);
        printBalance(sim.activePlayer);
        printBalance(selectedPlayer);

        for(int i = 0; i < sim.activePlayer.deckCards.size(); i++){
            if(sim.activePlayer.deckCards.get(i).name == "Pay Card"){
                sim.board.availableDeckCards.add(sim.activePlayer.deckCards.get(i));
                sim.activePlayer.deckCards.remove(i);
                break;
            }
        }
    }

    private boolean STWexemption(Player targetedPlayer){
        boolean hasExemption = false;
        int index = -1;
        for(int i = 0; i < targetedPlayer.deckCards.size(); i++){
            if(targetedPlayer.deckCards.get(i).name == "Exemption Card" && index == -1){
                hasExemption = true;
                index = i;
                break;
            }
        }
        if(!hasExemption) return false;
        System.out.println(targetedPlayer.name+" would you like to use your exemption card?");
        boolean selection = input.receiveBooleanSelection();
        if(!selection){
            System.out.println(targetedPlayer.name+" declined to use their exemption card.");
            return false;
        }
        System.out.println(targetedPlayer.name+" used their exemption card.");
        sim.board.availableDeckCards.add(targetedPlayer.deckCards.get(index));
        targetedPlayer.deckCards.remove(index);
        return true;
    }
    public void retire(){
        System.out.println(sim.activePlayer.name+" retired!");
        retiredSellHouse();
        if(sim.activePlayer.children>0){
            System.out.println(sim.activePlayer.name+" received a $10,000 retirement gift from each of their children worth $"+(10000*sim.activePlayer.children));
            sim.activePlayer.money+=10000;
            printBalance(sim.activePlayer);
        }
        System.out.println(sim.activePlayer.name+" would you like to retire at Millionaire Estates or Countryside Acres?");
        System.out.println("1: Millionaire Estates");
        System.out.println("2: Countryside Acres");
        int selection = input.getListSelection(1,2);
        if(selection == 1){
            System.out.println(sim.activePlayer.name+" chose to retire at Millionaire Estates");
            System.out.println(sim.activePlayer.name+" will receive a LIFE tile, but their LIFE tiles may still be stolen.");
            collectLifeTile();
            sim.activePlayer.retiredAtEstates=true;
        }
        else {
            System.out.println(sim.activePlayer.name+" chose to retire at Countryside Acres");
            System.out.println(sim.activePlayer.name+" will not receive a LIFE tile, but their LIFE tiles may not be stolen.");
            sim.activePlayer.retiredAtEstates=false;
        }
        sim.retiredPlayers.add(sim.activePlayer);
        sim.players.remove(sim.activePlayer);
        if(sim.players.isEmpty()) calculateFinalScores();

    }

    private void retiredSellHouse(){
        if(sim.activePlayer.house == null) return;
        System.out.println(sim.activePlayer.name+" sold their "+sim.activePlayer.house.name+" for $"+sim.activePlayer.house.sellPrice);
        sim.activePlayer.money+=sim.activePlayer.house.sellPrice;
        printBalance(sim.activePlayer);
        if(sim.activePlayer.house.starterHouse){
            sim.board.availableStarterHouses.add(sim.activePlayer.house);
        }
        else{
            sim.board.availableHouses.add(sim.activePlayer.house);
        }
        sim.activePlayer.house=null;
    }

    private void cashLifeTiles(Player player){
        int value = 0;
        int count = 0;
        for(int i = 0; i < player.lifeTiles.size(); i++){
            value += player.lifeTiles.get(i).value;
            count ++;
        }
        System.out.println(player.name+" is cashing in "+count+" LIFE tiles worth a total of $"+value);
        player.money+=value;
    }

    private void calculateFinalScores(){
        ArrayList<Player> rankings = new ArrayList<>();
        System.out.println("");
        for(int i = 0; i < sim.retiredPlayers.size(); i++){
            Player player = sim.retiredPlayers.get(i);
            player.repayLoans();
            cashLifeTiles(player);
            System.out.println("");
            if(i==0)rankings.add(player);
            else{
                boolean added=false;
                for(int j = 0; j < rankings.size(); j++){
                    if(player.money > rankings.get(j).money){
                        rankings.add(j, player);
                    }
                }
                if(!added) rankings.add(rankings.size(), player);
            }
        }
        System.out.println("The final rankings are:");
        for(int i = 0; i < rankings.size(); i++){
            System.out.println((i+1)+": "+rankings.get(i).name+" with a net worth of $"+rankings.get(i).money);
        }
    }

    private int printAvailableLTI(){
        System.out.println("Please enter the number "+sim.activePlayer.name+" would like to purchase for $10,000.");
        System.out.print("Available options are: ");
        for(int i = 0; i < sim.board.availableLTI.size(); i++){
            if(i+1 < sim.board.availableLTI.size()) {System.out.print(sim.board.availableLTI.get(i) + ", ");}
            else{System.out.print(sim.board.availableLTI.get(i));}
        }
        System.out.println("");
        return input.getListSelection(1, 10);
    }

    public void buyLTI(){
        if(sim.activePlayer.money < 10000){
            System.out.println(sim.activePlayer.name+" can not buy a Long Term Investment. You must have at least $10,000.");
            printBalance(sim.activePlayer);
            return;
        }
        int selection = printAvailableLTI();
        int index = -1;
        int test;
        while(index == -1){
            for(int i = 0; i < sim.board.availableLTI.size(); i++){
                test = sim.board.availableLTI.get(i);
                if(test == selection) index = i;
            }
            if(index == -1){
                System.out.println("That number is no longer available. Please select a new option.");
                selection = printAvailableLTI();
            }
        }
        sim.activePlayer.LTI = selection;
        sim.board.availableLTI.remove(index);
        sim.activePlayer.money -= 10000;
        System.out.println(sim.activePlayer.name+" has successfully purchased Long Term Investment " +selection + " for $10,000.");
        printBalance(sim.activePlayer);

    }

    private void collectFromEachPlayer(int money){
        for(int i = 0; i < sim.players.size(); i++){
            Player player = sim.players.get(i);
            if(player != sim.activePlayer){
                System.out.println(sim.activePlayer.name + " collected $" + money + " from " + player.name);
                player.pay(money);
            }
        }
    }

    private void printBalance (Player player){
        System.out.println(player.name + " balance is $"+player.money);
    }

    private Player getNextPlayer(int distFromActive){
        int currentIndex = sim.players.indexOf(sim.activePlayer);
        int numOfPlayers = sim.players.size();
        if(currentIndex + distFromActive + 1 < numOfPlayers) return sim.players.get(currentIndex + distFromActive + 1);
        return sim.players.get(currentIndex + distFromActive + 1 - numOfPlayers);
    }

    public void collectMoney(){
        System.out.println(sim.activePlayer.name+" landed on a space worth $"+sim.activePlayer.currentSpace.value);
        sim.activePlayer.money+=sim.activePlayer.currentSpace.value;
        printBalance(sim.activePlayer);
        for(int i = 0; i < sim.players.size()-1; i++){
            Player checkCardPlayer = getNextPlayer(i);
            boolean hasCard = false;
            for(int j = 0; j < checkCardPlayer.deckCards.size(); j++){
                if(checkCardPlayer.deckCards.get(j).name == "Collect Card") hasCard=true;
            }
            if(hasCard){
                System.out.println(checkCardPlayer.name+" would you like to use a Collect card?");
                System.out.println("The space is worth $"+sim.activePlayer.currentSpace.value);
                boolean selection = input.receiveBooleanSelection();

                if(selection){
                    sim.cardUsePlayer=checkCardPlayer;
                    STWcollect();
                }
            }
        }
    }

    public void payMoney(){
        System.out.println(sim.activePlayer.name+" landed on a space worth a payment of $"+sim.activePlayer.currentSpace.value);
        sim.activePlayer.money-=sim.activePlayer.currentSpace.value;
        printBalance(sim.activePlayer);
        boolean hasPayCard = false;
        for(int i = 0; i < sim.activePlayer.deckCards.size(); i++){
            if(sim.activePlayer.deckCards.get(i).name == "Pay Card") hasPayCard = true;
        }
        if(hasPayCard){
            System.out.println("Would "+sim.activePlayer.name+" like to use a Pay Card?");
            boolean selection = input.receiveBooleanSelection();
            if(selection) STWpay();
            else System.out.println(sim.activePlayer.name+" declined to use a Pay Card.");
        }
    }

    private void printCards(){
        for(int i = 0; i < sim.players.size(); i++){
            Player player = sim.players.get(i);
            System.out.println("");
            System.out.println(player.name);
            for(int j = 0; j < player.deckCards.size(); j++){
                System.out.print(player.deckCards.get(j).name+" ");
            }
            System.out.println("");
        }
    }

}
