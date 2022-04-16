package com.Final;

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

    public void collectMoney(){
    }

    public void collectLifeTile(){
        if(!sim.board.availableLifeTiles.isEmpty()){
            LifeTile tile = sim.board.availableLifeTiles.get(Utility.rndFromRange(0, sim.board.availableLifeTiles.size()-1));
            sim.activePlayer.lifeTiles.add(tile);
            System.out.println("");
            System.out.println("You Collected a Life Tile worth $ " + tile.value);
            return;
        }
        System.out.println("");
        System.out.println("No more Life Tiles left");
    }

    public void payTaxes(){
        System.out.println("Your balance is $" + sim.activePlayer.money);
        int tax = sim.activePlayer.career.taxes;
        sim.activePlayer.money -= tax;
        System.out.println("You got had to pay $" + tax + " in taxes bringing your balance to $" + sim.activePlayer.money);
    }

    public void collectTaxRefund(){
        System.out.println("Your balance is $" + sim.activePlayer.money);
        int tax = sim.activePlayer.career.taxes;
        sim.activePlayer.money += tax;
        System.out.println("You got $" + tax + " in a tax refund bringing your balance to $" + sim.activePlayer.money);
    }

    public void loseJob(){

    }

    public void getBaby(){
        if(!sim.activePlayer.hasSpouse){
            System.out.println("You can not have a baby since you do not have a spouse");
            return;
        }
        sim.activePlayer.children++;
        System.out.println("You and your spouse had a baby! You now have " + sim.activePlayer.children + " children.");
    }

    public void spinToWin(){

    }

    private void getPaid(){
        System.out.println("Your balance is $" + sim.activePlayer.money);
        int pay = (sim.activePlayer.career.salary + (sim.activePlayer.career.numOfRaises * 100));
        sim.activePlayer.money += pay;
        System.out.println("You got paid $" + pay + " bringing your balance to $" + sim.activePlayer.money);
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
        System.out.println("You chose " + selectedCareer.name);
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
            System.out.println("Your current money is $" + sim.activePlayer.money);
            System.out.println("Can not afford any home options.");
            return;
        }
        boolean purchased = false;
        House selectedHouse = null;
        while (!purchased) {
            System.out.println("");
            System.out.println("Your current money is $" + sim.activePlayer.money);
            System.out.println("Please enter your selection: ");
            for (int i = 0; i < houseOptions.size(); i++) {
                System.out.println((i+1) + ": " + houseOptions.get(i).name + " for $" + houseOptions.get(i).purchasePrice);
            }
            selectedHouse = houseOptions.get((input.getListSelection(1, 3)-1));
            purchased = purchase(selectedHouse.purchasePrice);
            if(!purchased){
                System.out.println("Unable to afford selection. Please enter new selection.");
            }
            System.out.println("Successfully purchased " + selectedHouse.name + " for $" + selectedHouse.purchasePrice);
            System.out.println("Your new balance is $" +sim.activePlayer.money);
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
            System.out.println("Would you like to sell your house?");
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
                System.out.println("Successfully sold " + sim.activePlayer.house.name + " for $" + sim.activePlayer.house.sellPrice);
                System.out.println("Your new balance is $" +sim.activePlayer.money);
                sim.activePlayer.house=null;
                return true;
            }
            System.out.println("");
            System.out.println("You declined to sell " + sim.activePlayer.house.name + " for $" + sim.activePlayer.house.sellPrice);
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
    public void returnToSchool(){}
    public void graduateNightSchool(){}
    public void getMarried(){
        System.out.println("You got married!");
        sim.activePlayer.hasSpouse = true;
    }
    public void suePlayer(){}
    public void takeLoan(){
        System.out.println("Your balance is $" + sim.activePlayer.money);
        sim.activePlayer.numOfLoans++;
        sim.activePlayer.money += 200;
        System.out.println("You had to take a loan bringing your balance to $" + sim.activePlayer.money +
                " and increasing your loan count to " +sim.activePlayer.numOfLoans);
    }
    public void collectSTWcard(){}
    public void STWboost(){}
    public void STWcollect(){}
    public void STWpay(){}
    public void STWexemption(){}
    public void retire(){}
    public void calculateFinalScores(){}

}
