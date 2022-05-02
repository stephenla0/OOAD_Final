package com.Final;

import java.util.Scanner;
import java.util.ArrayList;

public class Receiver implements LoggerWriter{
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

    public void startFirstTurn(){
        loggerOutln("", sim.logger);
        loggerOutln(sim.activePlayer.name+", would you like to attend college or start your career?", sim.logger);
        loggerOutln("Starting your career immediately will leave you with no debt; however, you will not have immediate access to " +
                "higher paying careers.", sim.logger);
        loggerOutln("Attending college will provide you with immediate access to higher paying careers; however, " +
                "you will need to take out $100,000 in loans.", sim.logger);
        loggerOutln("Which path would you like to take?", sim.logger);
        loggerOutln("1: Start career", sim.logger);
        loggerOutln("2: Attend college", sim.logger);
        int selection = input.getListSelection(1,2);
        if(selection == 1){
            loggerOutln(sim.activePlayer.name+" decided to start their career!", sim.logger);
            sim.activePlayer.currentSpace=sim.activePlayer.currentSpace.right;
        }
        else{
            loggerOutln(sim.activePlayer.name+" decided to attend college!", sim.logger);
            sim.activePlayer.currentSpace=sim.activePlayer.currentSpace.left;
        }
        sim.activePlayer.currentSpace.card.executeCommand();
    }

    private void sell(int money){
        sim.activePlayer.money += money;
    }

    public void collectLifeTile(){
        if(!sim.board.availableLifeTiles.isEmpty()){
            LifeTile tile = sim.board.availableLifeTiles.get(Utility.rndFromRange(0, sim.board.availableLifeTiles.size()-1));
            sim.activePlayer.lifeTiles.add(tile);
            sim.board.availableLifeTiles.remove(tile);
            loggerOutln("", sim.logger);
            loggerOutln(sim.activePlayer.name+" collected a LIFE Tile worth $" + tile.value, sim.logger);
            return;
        }
        else if(!sim.retiredPlayers.isEmpty()){
            ArrayList<Integer> indexes = new ArrayList<>();
            for(int i = 0; i < sim.retiredPlayers.size(); i++){
                if(sim.retiredPlayers.get(i).retiredAtEstates && !sim.retiredPlayers.get(i).lifeTiles.isEmpty()) indexes.add(i);
            }
            if(indexes.isEmpty()){
                loggerOutln("There are no more LIFE Tiles left and no people to steal from.", sim.logger);
                return;
            }
            loggerOutln("There are no more LIFE Tiles left but there are people to steal from.", sim.logger);
            loggerOutln("Please enter your selection to steal from.", sim.logger);
            for(int i = 0; i<indexes.size(); i++){
                loggerOutln((i+1)+": "+sim.retiredPlayers.get(indexes.get(i)).name, sim.logger);
            }
            int selection = input.getListSelection(1, indexes.size());
            Player selectedPlayer = sim.retiredPlayers.get(indexes.get(selection-1));
            loggerOutln(sim.activePlayer.name+" chose to steal a LIFE tile from "+selectedPlayer.name, sim.logger);
            int random = Utility.rndFromRange(0, selectedPlayer.lifeTiles.size()-1);
            sim.activePlayer.lifeTiles.add(selectedPlayer.lifeTiles.get(random));
            selectedPlayer.lifeTiles.remove(random);
        }
    }

    public void payTaxes(){
        printBalance(sim.activePlayer);
        int tax = sim.activePlayer.career.taxes;
        sim.activePlayer.money -= tax;
        loggerOutln(sim.activePlayer.name+" had to pay $" + tax + " in taxes.", sim.logger);
        printBalance(sim.activePlayer);
    }

    public void collectTaxRefund(){
        printBalance(sim.activePlayer);
        int tax = sim.activePlayer.career.taxes;
        sim.activePlayer.money += tax;
        loggerOutln(sim.activePlayer.name+" got $" + tax + " in a tax refund.", sim.logger);
        printBalance(sim.activePlayer);
    }

    public void loseJob(){
        loggerOutln(sim.activePlayer.name+" lost their job "+sim.activePlayer.career.name, sim.logger);
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
            loggerOutln(sim.activePlayer.name+" can not have a baby since you do not have a spouse", sim.logger);
            return;
        }
        for(int i=0; i<sim.activePlayer.currentSpace.value; i++) {
            sim.activePlayer.children++;
            loggerOutln(sim.activePlayer.name + " and your spouse had a baby! " + sim.activePlayer.name + " now have " + sim.activePlayer.children + " children.", sim.logger);
        }
        collectLifeTile();
    }

    private void getPaid(){
        printBalance(sim.activePlayer);
        int pay = (sim.activePlayer.career.salary + (sim.activePlayer.career.numOfRaises * 100));
        sim.activePlayer.money += pay;
        loggerOutln(sim.activePlayer.name+" got paid $" + pay, sim.logger);
        printBalance(sim.activePlayer);
    }

    public void payDay(){
        getPaid();
    }
    public void payDayRaise(){
        if(sim.activePlayer.career.salary * sim.activePlayer.career.numOfRaises < sim.activePlayer.career.maxSalary &&
                sim.activePlayer.career.maxSalary != -1)  sim.activePlayer.career.numOfRaises++;
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
        loggerOutln("Please enter your career selection: ", sim.logger);
        for (int i = 0; i < careers.size(); i++) {
            loggerOutln((i+1) + ": " + careers.get(i).name + " with a salary of $" + careers.get(i).salary
            + " and taxes of $" + careers.get(i).taxes, sim.logger);
        }
        Career selectedCareer = careers.get((input.getListSelection(1, 3)-1));
        loggerOutln(sim.activePlayer.name+" chose " + selectedCareer.name, sim.logger);
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
            loggerOutln("Can not afford any home options.", sim.logger);
            return;
        }
        boolean purchased = false;
        House selectedHouse = null;
        while (!purchased) {
            loggerOutln("", sim.logger);
            printBalance(sim.activePlayer);
            loggerOutln("Please enter your selection: ", sim.logger);
            for (int i = 0; i < houseOptions.size(); i++) {
                loggerOutln((i+1) + ": " + houseOptions.get(i).name + " for $" + houseOptions.get(i).purchasePrice, sim.logger);
            }
            selectedHouse = houseOptions.get((input.getListSelection(1, 3)-1));
            purchased = purchase(selectedHouse.purchasePrice);
            if(!purchased){
                loggerOutln("Unable to afford selection. Please enter new selection.", sim.logger);
            }
            loggerOutln(sim.activePlayer.name+" successfully purchased " + selectedHouse.name + " for $" + selectedHouse.purchasePrice, sim.logger);
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
            loggerOutln("", sim.logger);
            loggerOutln("Would "+sim.activePlayer.name+" like to sell your house?", sim.logger);
            loggerOutln("Current House: " + sim.activePlayer.house.name + " is worth $" + sim.activePlayer.house.sellPrice, sim.logger);
            boolean selection = input.receiveBooleanSelection();
            if(selection){
                sell(sim.activePlayer.house.sellPrice);
                if(sim.activePlayer.house.starterHouse){
                    sim.board.availableStarterHouses.add(sim.activePlayer.house);
                }
                else{
                    sim.board.availableHouses.add(sim.activePlayer.house);
                }
                loggerOutln("", sim.logger);
                loggerOutln(sim.activePlayer.name+" successfully sold " + sim.activePlayer.house.name + " for $" + sim.activePlayer.house.sellPrice, sim.logger);
                printBalance(sim.activePlayer);
                sim.activePlayer.house=null;
                return true;
            }
            loggerOutln("", sim.logger);
            loggerOutln(sim.activePlayer.name+" declined to sell " + sim.activePlayer.house.name + " for $" + sim.activePlayer.house.sellPrice, sim.logger);
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
    public void startCollege(){
        sim.activePlayer.hasDegree = true;
        for(int i = 0; i < 5; i++){
            sim.activePlayer.takeLoan();
        }
    }
    public void graduateCollege(){
        loggerOutln(sim.activePlayer.name+" has graduated college!", sim.logger);
        chooseCareer();
    }
    public void returnToSchool(){
        loggerOutln(sim.activePlayer.name+" would you like to return to school? It costs $50,000.", sim.logger);
        loggerOutln("Once you graduate, you will receive a degree if you do not already have one. This will make you eligible for college careers.", sim.logger);
        loggerOutln("Additionally, you may choose between a new career or a $20,000 raise.", sim.logger);
        printBalance(sim.activePlayer);
        boolean selection = input.receiveBooleanSelection();
        if(!selection){
            loggerOutln(sim.activePlayer.name+" declined to return to school.", sim.logger);
            sim.activePlayer.currentSpace=sim.activePlayer.currentSpace.right;
            return;
        }
        loggerOutln(sim.activePlayer.name+" decided to return to school for $50,000.", sim.logger);
        sim.activePlayer.pay(50000);
        printBalance(sim.activePlayer);
        sim.activePlayer.currentSpace=sim.activePlayer.currentSpace.left;
    }
    public void graduateNightSchool(){
        loggerOutln(sim.activePlayer.name+" graduated school and received a degree!", sim.logger);
        sim.activePlayer.hasDegree=true;
        loggerOutln(sim.activePlayer.name+" would you like a new college career or a $20,000 raise?", sim.logger);
        loggerOutln("1: New college career", sim.logger);
        loggerOutln("2: $20,000 raise", sim.logger);
        int selection = input.getListSelection(1,2);
        if(selection==1){
            loggerOutln(sim.activePlayer+" chose a new college career!", sim.logger);
            loseJob();
        }
        else{
            loggerOutln(sim.activePlayer+" chose a $20,000 raise!", sim.logger);
            sim.activePlayer.career.numOfRaises+=2;
        }
    }
    public void getMarried(){

        loggerOutln(sim.activePlayer.name+" got married!", sim.logger);
        collectLifeTile();
        sim.activePlayer.hasSpouse = true;
        int spin = Utility.rndFromRange(1, 10);
        loggerOutln(sim.activePlayer.name+" spun a "+spin, sim.logger);
        if(spin >= 8){
            loggerOutln(sim.activePlayer.name+" collect $10,000 from each player!", sim.logger);
            collectFromEachPlayer(10000);
        }
        else if (spin <= 7 && spin >= 5){
            loggerOutln(sim.activePlayer.name+" collect $5,000 from each player!", sim.logger);
            collectFromEachPlayer(5000);
        }
        else{
            loggerOutln(sim.activePlayer.name+" got nothing from each player!", sim.logger);
        }
    }
    public void suePlayer(){
        if(sim.players.size()<2){
            loggerOutln(sim.activePlayer.name+" can not sue as there are not enough players.", sim.logger);
            return;
        }
        loggerOutln(sim.activePlayer.name+" can sue another player for $100,000", sim.logger);
        loggerOutln(sim.activePlayer.name+" choose who you want to sue", sim.logger);
        ArrayList<Integer> active = new ArrayList<>();
        for(int i = 0; i < sim.players.size(); i++){
            if(sim.players.get(i).name == sim.activePlayer.name){
                active.add(i+1);
            }
            else loggerOutln((i+1) +": " +sim.players.get(i).name, sim.logger);
        }
        int selection = input.getListSelectionRestricted(1, sim.players.size(), active)-1;

        boolean exempt = STWexemption(sim.activePlayer);
        if(exempt) return;

        loggerOutln(sim.activePlayer.name+" sued "+sim.players.get(selection).name+ " for $100,000.", sim.logger);
        sim.players.get(selection).pay(100000);
        printBalance(sim.activePlayer);
        printBalance(sim.players.get(selection));
    }

    public void collectSTWcard(){
        if(sim.board.availableDeckCards.size() == 0){
            loggerOutln(sim.activePlayer.name+" could not collect a Share The Wealth card as no more are available.", sim.logger);
            return;
        }
        loggerOutln(sim.activePlayer.name+" collected a Share The Wealth Card.", sim.logger);
        sim.activePlayer.deckCards.add(sim.board.availableDeckCards.get(0));
        sim.board.availableDeckCards.remove(0);
    }

    private void removeBoostCard(int boost){
        for(int i = 0; i < sim.cardUsePlayer.deckCards.size(); i++){
            if(sim.cardUsePlayer.deckCards.get(i).name.equals("Spin to Win (" +boost+ ")")){
                loggerOutln(sim.cardUsePlayer.name+" used a " +boost+ " token boost card.", sim.logger);
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
        loggerOutln("Would " +sim.cardUsePlayer.name+ " like to use a Spin To Win boost card?", sim.logger);
        boolean boost = input.receiveBooleanSelection();
        if(!boost){
            loggerOutln(sim.cardUsePlayer.name+" declined to use a boost card.", sim.logger);
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
            loggerOutln("Please enter which boost card to use", sim.logger);
            loggerOutln("1: 2 tokens", sim.logger);
            loggerOutln("2: 4 tokens", sim.logger);
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

            loggerOutln("How much would "+sim.cardUsePlayer.name+ " like to wager? ($0-$50000)", sim.logger);
            int bet = input.getListSelection(0, 50000);
            bets.add(bet);
            sim.cardUsePlayer.money -= bet;
            loggerOutln(sim.cardUsePlayer.name+ " wagered $" + bet, sim.logger);
            printBalance(sim.cardUsePlayer);

            int boost = STWboost();
            int tokens = 1;
            if(boost != 0){
                tokens = tokens * boost;
            }
            for(int j = 0; j < tokens; j++){
                loggerOutln("Please select the number to place a token on", sim.logger);
                for(int k = 1; k <= 10; k++){
                    boolean inPicks = false;
                    for(int p = 0; p < sim.cardUsePlayer.currentSTWtokens.size(); p++){
                        if(k == sim.cardUsePlayer.currentSTWtokens.get(p)) inPicks = true;
                    }
                    if(!inPicks) loggerOut(k+" ", sim.logger);
                }
                loggerOutln("", sim.logger);
                int selection = input.getListSelectionRestricted(1, 10, sim.cardUsePlayer.currentSTWtokens);
                loggerOutln(sim.cardUsePlayer.name+ " placed a token on space " + selection, sim.logger);
                sim.cardUsePlayer.currentSTWtokens.add(selection);
            }
        }

        int spin = sim.board.spinWheel();
        loggerOutln("The number spun was "+spin, sim.logger);
        for(int i = 0; i < sim.players.size(); i++){
            Player player = sim.players.get(i);
            boolean won = false;
            for(int j = 0; j < player.currentSTWtokens.size(); j++){
                if(spin == player.currentSTWtokens.get(j)) won = true;
            }
            if(!won){
                loggerOutln(player.name+" did not win", sim.logger);
                printBalance(player);
            }
            else{
                loggerOutln(player.name+" won! They receive $"+(10*bets.get(i)), sim.logger);
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
            loggerOutln(sim.cardUsePlayer.name+" can not use a Collect card for this space. The value must be greater than $5,000", sim.logger);
            return;
        }
        loggerOutln(sim.cardUsePlayer.name+" decided to use a collect card.", sim.logger);

        boolean exempt = STWexemption(sim.activePlayer);
        if(exempt) return;

        int[] values = divideValue();
        sim.cardUsePlayer.money += values[0];
        sim.activePlayer.money -= values[1];
        loggerOutln(sim.cardUsePlayer.name+" used a Collect card on "+sim.activePlayer.name+" and collected $"+values[0]
        +" from the space while "+sim.activePlayer.name+" only collected $"+values[1], sim.logger);
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
            loggerOutln(sim.activePlayer.name+" can not use a Pay card for this space. The value must be greater than $5,000", sim.logger);
            return;
        }
        ArrayList<Integer> restricted = new ArrayList<>();
        loggerOutln(sim.activePlayer.name+" decided to use a pay card.", sim.logger);
        loggerOutln("Please select the player "+sim.activePlayer.name+" wish to use your Pay card on.", sim.logger);
        loggerOutln("Available options are: ", sim.logger);
        for(int i = 0; i < sim.players.size(); i++){
            if(sim.activePlayer != sim.players.get(i)) {
                loggerOutln((i+1)+":"+sim.players.get(i).name, sim.logger);
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
        loggerOutln(sim.activePlayer.name+" used a Pay card on "+ selectedPlayer.name+" and paid $"+values[0]
                +" to the bank while "+selectedPlayer.name+" paid $"+values[1], sim.logger);
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
        loggerOutln(targetedPlayer.name+" would you like to use your exemption card?", sim.logger);
        boolean selection = input.receiveBooleanSelection();
        if(!selection){
            loggerOutln(targetedPlayer.name+" declined to use their exemption card.", sim.logger);
            return false;
        }
        loggerOutln(targetedPlayer.name+" used their exemption card.", sim.logger);
        sim.board.availableDeckCards.add(targetedPlayer.deckCards.get(index));
        targetedPlayer.deckCards.remove(index);
        return true;
    }
    public void retire(){
        loggerOutln(sim.activePlayer.name+" retired!", sim.logger);
        retiredSellHouse();
        if(sim.activePlayer.children>0){
            loggerOutln(sim.activePlayer.name+" received a $10,000 retirement gift from each of their children worth $"+(10000*sim.activePlayer.children), sim.logger);
            sim.activePlayer.money+=10000;
            printBalance(sim.activePlayer);
        }
        loggerOutln(sim.activePlayer.name+" would you like to retire at Millionaire Estates or Countryside Acres?", sim.logger);
        loggerOutln("1: Millionaire Estates", sim.logger);
        loggerOutln("2: Countryside Acres", sim.logger);
        int selection = input.getListSelection(1,2);
        if(selection == 1){
            loggerOutln(sim.activePlayer.name+" chose to retire at Millionaire Estates", sim.logger);
            loggerOutln(sim.activePlayer.name+" will receive a LIFE tile, but their LIFE tiles may still be stolen.", sim.logger);
            collectLifeTile();
            sim.activePlayer.retiredAtEstates=true;
        }
        else {
            loggerOutln(sim.activePlayer.name+" chose to retire at Countryside Acres", sim.logger);
            loggerOutln(sim.activePlayer.name+" will not receive a LIFE tile, but their LIFE tiles may not be stolen.", sim.logger);
            sim.activePlayer.retiredAtEstates=false;
        }
        sim.retiredPlayers.add(sim.activePlayer);
        sim.players.remove(sim.activePlayer);
        if(sim.players.isEmpty()) {
            calculateFinalScores();
        }

    }

    private void retiredSellHouse(){
        if(sim.activePlayer.house == null) return;
        loggerOutln(sim.activePlayer.name+" sold their "+sim.activePlayer.house.name+" for $"+sim.activePlayer.house.sellPrice, sim.logger);
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
        loggerOutln(player.name+" is cashing in "+count+" LIFE tiles worth a total of $"+value, sim.logger);
        player.money+=value;
    }

    private void calculateFinalScores(){
        ArrayList<Player> rankings = new ArrayList<>();
        loggerOutln("", sim.logger);
        for(int i = 0; i < sim.retiredPlayers.size(); i++){
            Player player = sim.retiredPlayers.get(i);
            player.repayLoans();
            cashLifeTiles(player);
            loggerOutln("", sim.logger);
            if(i==0)rankings.add(player);
            else{
                boolean added=false;
                for(int j = 0; j < rankings.size(); j++){
                    if(player.money > rankings.get(j).money){
                        rankings.add(j, player);
                        added = true;
                        break;
                    }
                }
                if(!added) rankings.add(rankings.size(), player);
            }
        }
        loggerOutln("The final rankings are:", sim.logger);
        for(int i = 0; i < rankings.size(); i++){
            loggerOutln((i+1)+": "+rankings.get(i).name+" with a net worth of $"+rankings.get(i).money, sim.logger);
        }
    }

    private int printAvailableLTI(){
        loggerOutln("Please enter the number "+sim.activePlayer.name+" would like to purchase for $10,000.", sim.logger);
        loggerOut("Available options are: ", sim.logger);
        for(int i = 0; i < sim.board.availableLTI.size(); i++){
            if(i+1 < sim.board.availableLTI.size()) {loggerOut(sim.board.availableLTI.get(i) + ", ", sim.logger);}
            else{loggerOut(sim.board.availableLTI.get(i).toString(), sim.logger);}
        }
        loggerOutln("", sim.logger);
        return input.getListSelection(1, 10);
    }

    public void buyLTI(){
        if(sim.activePlayer.money < 10000){
            loggerOutln(sim.activePlayer.name+" can not buy a Long Term Investment. You must have at least $10,000.", sim.logger);
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
                loggerOutln("That number is no longer available. Please select a new option.", sim.logger);
                selection = printAvailableLTI();
            }
        }
        sim.activePlayer.LTI = selection;
        sim.board.availableLTI.remove(index);
        sim.activePlayer.money -= 10000;
        loggerOutln(sim.activePlayer.name+" has successfully purchased Long Term Investment " +selection + " for $10,000.", sim.logger);
        printBalance(sim.activePlayer);

    }

    private void collectFromEachPlayer(int money){
        for(int i = 0; i < sim.players.size(); i++){
            Player player = sim.players.get(i);
            if(player != sim.activePlayer){
                loggerOutln(sim.activePlayer.name + " collected $" + money + " from " + player.name, sim.logger);
                player.pay(money);
            }
        }
    }

    private void printBalance (Player player){
        loggerOutln(player.name + " balance is $"+player.money, sim.logger);
    }

    private Player getNextPlayer(int distFromActive){
        int currentIndex = sim.players.indexOf(sim.activePlayer);
        int numOfPlayers = sim.players.size();
        if(currentIndex + distFromActive + 1 < numOfPlayers) return sim.players.get(currentIndex + distFromActive + 1);
        return sim.players.get(currentIndex + distFromActive + 1 - numOfPlayers);
    }

    public void collectMoney(){
        loggerOutln(sim.activePlayer.name+" landed on a space worth $"+sim.activePlayer.currentSpace.value, sim.logger);
        sim.activePlayer.money+=sim.activePlayer.currentSpace.value;
        printBalance(sim.activePlayer);
        for(int i = 0; i < sim.players.size()-1; i++){
            Player checkCardPlayer = getNextPlayer(i);
            boolean hasCard = false;
            for(int j = 0; j < checkCardPlayer.deckCards.size(); j++){
                if(checkCardPlayer.deckCards.get(j).name == "Collect Card") hasCard=true;
            }
            if(hasCard){
                loggerOutln(checkCardPlayer.name+" would you like to use a Collect card?", sim.logger);
                loggerOutln("The space is worth $"+sim.activePlayer.currentSpace.value, sim.logger);
                boolean selection = input.receiveBooleanSelection();

                if(selection){
                    sim.cardUsePlayer=checkCardPlayer;
                    STWcollect();
                }
            }
        }
    }

    public void payMoney(){
        loggerOutln(sim.activePlayer.name+" landed on a space worth a payment of $"+sim.activePlayer.currentSpace.value, sim.logger);
        sim.activePlayer.money-=sim.activePlayer.currentSpace.value;
        printBalance(sim.activePlayer);
        boolean hasPayCard = false;
        for(int i = 0; i < sim.activePlayer.deckCards.size(); i++){
            if(sim.activePlayer.deckCards.get(i).name == "Pay Card") hasPayCard = true;
        }
        if(hasPayCard){
            loggerOutln("Would "+sim.activePlayer.name+" like to use a Pay Card?", sim.logger);
            boolean selection = input.receiveBooleanSelection();
            if(selection) STWpay();
            else loggerOutln(sim.activePlayer.name+" declined to use a Pay Card.", sim.logger);
        }
    }

    private void printCards(){
        for(int i = 0; i < sim.players.size(); i++){
            Player player = sim.players.get(i);
            loggerOutln("", sim.logger);
            loggerOutln(player.name, sim.logger);
            for(int j = 0; j < player.deckCards.size(); j++){
                loggerOut(player.deckCards.get(j).name+" ", sim.logger);
            }
            loggerOutln("", sim.logger);
        }
    }

    public void familyOrNormal(){
        loggerOutln(sim.activePlayer.name+" would you like to take the family path or normal path?", sim.logger);
        loggerOutln("1: Family", sim.logger);
        loggerOutln("2: Normal", sim.logger);
        int selection = input.getListSelection(1,2);
        if(selection==1){
            loggerOutln(sim.activePlayer.name+" chose the family path", sim.logger);
            sim.activePlayer.currentSpace=sim.activePlayer.currentSpace.left;
        }
        else{
            loggerOutln(sim.activePlayer.name+" chose the normal path", sim.logger);
            sim.activePlayer.currentSpace=sim.activePlayer.currentSpace.right;
        }
    }

    public void safeOrRisky(){
        loggerOutln(sim.activePlayer.name+" would you like to take the safe path or risky path?", sim.logger);
        loggerOutln("1: Safe", sim.logger);
        loggerOutln("2: Risky", sim.logger);
        int selection = input.getListSelection(1,2);
        if(selection==1){
            loggerOutln(sim.activePlayer.name+" chose the safe path", sim.logger);
            sim.activePlayer.currentSpace=sim.activePlayer.currentSpace.right;
        }
        else{
            loggerOutln(sim.activePlayer.name+" chose the risky path", sim.logger);
            sim.activePlayer.currentSpace=sim.activePlayer.currentSpace.left;
        }
    }

}
