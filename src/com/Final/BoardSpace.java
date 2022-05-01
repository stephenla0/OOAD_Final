package com.Final;

public class BoardSpace {
    BoardSpace forward;
    BoardSpace backward;
    BoardSpace left;
    BoardSpace right;
    BoardSpaceCard card;
    String color;
    boolean interruptMovement;
    int value;
    String message;

    BoardSpace(BoardSpaceCard card, String color, boolean interruptMovement){
        forward = null;
        backward = null;
        left = null;
        right = null;
        this.card = card;
        this.color = color;
        this.interruptMovement = interruptMovement;
        value = 0;
    }

    public void setForward(BoardSpace forward) {
        this.forward = forward;
    }

    public void setBackward(BoardSpace backward) {
        this.backward = backward;
    }

    public void setLeft(BoardSpace left) {
        this.left = left;
    }

    public void setRight(BoardSpace right) {
        this.right = right;
    }

    public void setValue(int value){this.value = value;}
}
