package com.memory;


public class PCB {
    static int pcbNum;
    String pidName;
    int pidSize;

    // Constructor
    public PCB(String name, int size) {
        pidName = name;
        pidSize = size;
    }

    public void display() {
        System.out.println("Process ID: " + pidName + "\tSize: " + pidSize);
    }

    public int getPidSize() {
        return pidSize;
    }

    public String getPidName() {
        return pidName;
    }
}
