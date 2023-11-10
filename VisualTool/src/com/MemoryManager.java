package com;

import java.util.ArrayList;

public class MemoryManager {

    static Partition[] partitions;
    static PCB[] pcbs;
    static int MIN;

    public static void display() {
        System.out.println("\nMemory Manager Status:");
        // Display partition information
        Partition.display();
        // Display PCB information
        displayPCB();
    }


    public static void displayPCB() {
        System.out.println("\nPCB Status:");
        for (PCB pcb : pcbs) {
            pcb.display();
        }
    }

    public static void firstFit() {
        Partition partition = new Partition(0, 0);
        partition.firstFit(new ArrayList<>(), partitions);
        display();
    }

    public static void nextFit() {
        Partition partition = new Partition(0, 0);
        partition.nextFit(new ArrayList<>(), partitions);
        display();
    }

    public static void bestFit() {
        Partition partition = new Partition(0, 0);
        partition.bestFit(new ArrayList<>(), partitions);
        display();
    }

    public static void worstFit() {
        Partition partition = new Partition(0, 0);
        partition.worstFit(new ArrayList<>(), partitions);
        display();
    }
}
