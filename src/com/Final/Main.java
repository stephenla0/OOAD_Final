package com.Final;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
	Simulation sim = new Simulation(scan);
    sim.startTestSim();
    }
}
