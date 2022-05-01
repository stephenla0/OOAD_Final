package com.OOAD;

import java.util.ArrayList;

// Decorator code, partially used from professors example code

// we can make our own custom decorator that takes the original item being sold
// and lets us add to it
// in decorator fashion, this should not impact the code for selling undecorated items
// note that this has no impact on the existing SellAnItem in Clerk
// or the way items work
// We will include an internal ArrayList of the items being purchased

public abstract class DecoratingItem {
    ArrayList<Item> cart;
    ArrayList<Item> inventory;

    DecoratingItem(ArrayList<Item> inventory, ArrayList<Item> cart) {
        this.cart = cart;
        this.inventory = inventory;
    }

    public String toString(){
        if (cart==null) return "no items";
        else {
            String s = "Items: ";
            for (Item item:cart) s = s + item.itemType.toString().toLowerCase() + " ";
            return s;
        }
    }
    public double totalCost(){
        if (cart==null) return 0;
        else {
            double d = 0;
            for (Item item:cart) d += item.listPrice;
            return d;
        }
    }
    public void AddItemTypeToCart(ItemType type) {
        Item item = Item.FindItemToAdd(inventory, type);
        if (item != null) {
            cart.add(item);
            inventory.remove(item);
        }
    }

}

// you could add special rules here for deciding whether you want to add an
// Item and how many, and make sure there's enough, etc.
// for simplicity if there is one, we add it to the sale and take it off inventory

class AddGigBag extends DecoratingItem {
    AddGigBag(ArrayList<Item> inventory, ArrayList<Item> cart, Item initialItem) {
        super(inventory, cart);
        double modifier = 0;
        if(!((Stringed) initialItem).isElectric){
            modifier = 0.1;
        }
        if(0.2 + modifier > Utility.rnd()){
            AddItemTypeToCart(ItemType.GIGBAG);
        }
    }
}

class AddCables extends DecoratingItem {
    AddCables(ArrayList<Item> inventory, ArrayList<Item> cart, Item initialItem) {
        super(inventory, cart);
        double modifier = 0;
        if(!((Stringed) initialItem).isElectric){
            modifier = 0.1;
        }
        if(0.3 + modifier > Utility.rnd()){
            int num = Utility.rndFromRange(1, 2);
            for(int i = 0; i<num; i++){
                AddItemTypeToCart(ItemType.CABLES);
            }
        }
    }
}

class AddStrings extends DecoratingItem {
    AddStrings(ArrayList<Item> inventory, ArrayList<Item> cart, Item initialItem) {
        super(inventory, cart);
        double modifier = 0;
        if(!((Stringed) initialItem).isElectric){
            modifier = 0.1;
        }
        if(0.4 + modifier > Utility.rnd()){
            int num = Utility.rndFromRange(1, 3);
            for(int i = 0; i<num; i++){
                AddItemTypeToCart(ItemType.STRINGS);
            }
        }
    }
}

class AddPracticeAmps extends DecoratingItem {
    AddPracticeAmps(ArrayList<Item> inventory, ArrayList<Item> cart, Item initialItem) {
        super(inventory, cart);
        double modifier = 0;
        if(!((Stringed) initialItem).isElectric){
            modifier = 0.1;
        }
        if(0.25 + modifier > Utility.rnd()){
            AddItemTypeToCart(ItemType.PRACTICEAMPS);
        }
    }
}


