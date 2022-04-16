package com.Final;
import java.text.NumberFormat;
import java.util.Random;

public interface Utility {
    static int rndFromRange(int min, int max){
        return (int) ((Math.random() * ((max+1) - min)) + min);
    }
}
