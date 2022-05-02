package com.Final;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

//logger class documents current information in a new text file each day

public class Logger {
    BufferedWriter text;
    private static Logger logger = new Logger(); // eager instant singleton

    Logger() {

    }

    public static Logger getInstance(){
        return logger;
    }

    public void startLog(int round){
        // reference: https://www.w3schools.com/java/java_files_create.asp
        try {
            text = new BufferedWriter(new FileWriter( "txt\\GoL-Logger-Round-"+round+"-.txt"));
        } catch (IOException e) {
            System.out.println("An error creating logger file GoL-Logger-Round-"+round+" occurred.");
            e.printStackTrace();
        }
        write("*** Simulation of the Game Of Life Round "+ round+" ***");
    }

    public void writeln(String string){
        System.out.println(string);
        try{
            text.write(string);
            text.newLine();
        }
        catch (IOException e) {
            System.out.println("An error writing logger text occurred. " + string);
            e.printStackTrace();
        }
    }

    public void write(String string){
        System.out.print(string);
        try{
            text.write(string);
            text.newLine();
        }
        catch (IOException e) {
            System.out.println("An error writing logger text occurred. " + string);
            e.printStackTrace();
        }
    }

    public void close(){
        try{
            text.close();
        }
        catch (IOException e) {
            System.out.println("An error closing logger text occurred.");
            e.printStackTrace();
        }
    }
}
