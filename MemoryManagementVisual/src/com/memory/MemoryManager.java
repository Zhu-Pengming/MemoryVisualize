package com.memory;


import java.util.ArrayList;
import java.util.List;

public class MemoryManager {

    static Partition[] partitions;
    static PCB[] pcbs;
    static int MIN;
    private static List<MemoryAllocationListener> listeners = new ArrayList<>();

    public static void display() {
        System.out.println("\nMemory Manager Status:");
        // Display partition information

        // Display PCB information
        displayPCB();
    }


    public static void displayPCB() {
        System.out.println("\nPCB Status:");
        for (PCB pcb : pcbs) {
            pcb.display();
        }
    }

    public static void addMemoryAllocationListener(MemoryAllocationListener listener) {
        listeners.add(listener);
    }

    public static void notifyMemoryAllocated() {
        for (MemoryAllocationListener listener : listeners) {
            listener.memoryAllocated();
        }
    }

    public static void firstFit() {
        Partition partition = new Partition(0, 0);
        partition.firstFit(MemoryManagementApp.pcbs, MemoryManagementApp.partitions);
        notifyMemoryAllocated();
        display();
    }

    public static void nextFit() {
        Partition partition = new Partition(0, 0);
        partition.nextFit(MemoryManagementApp.pcbs, MemoryManagementApp.partitions);
        notifyMemoryAllocated();
        display();
    }

    public static void bestFit() {
        Partition partition = new Partition(0, 0);
        partition.bestFit(MemoryManagementApp.pcbs, MemoryManagementApp.partitions);
        notifyMemoryAllocated();
        display();
    }

    public static void worstFit() {
        Partition partition = new Partition(0, 0);
        partition.worstFit(MemoryManagementApp.pcbs, MemoryManagementApp.partitions);
        notifyMemoryAllocated();
        display();
    }
}
