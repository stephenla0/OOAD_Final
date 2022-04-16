package com.Final;

public class Career {
    String name;
    int salary;
    int taxes;
    int numOfRaises;
    boolean needsCollege;

    Career(String name, int salary, int taxes, boolean needsCollege){
        this.name = name;
        this.salary = salary;
        this.taxes = taxes;
        numOfRaises = 0;
        this.needsCollege = needsCollege;
    }
}
