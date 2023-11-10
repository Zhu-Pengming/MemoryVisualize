package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MemoryManagement {

    static Partition[] partitions;
    static PCB[] pcbs;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose data source:");
        System.out.println("1. File");
        System.out.println("2. User input");

        int choice = scanner.nextInt();

        if (choice == 1) {
            readDataFromFile("data.txt"); // Read data from file
        } else if (choice == 2) {
            readData(); // Read data from user input
        } else {
            System.out.println("Invalid choice. Exiting...");
            return;
        }

        display(); // Display initial memory status


        // Perform memory allocation based on user choice or application logic
        System.out.println("Choose memory allocation method:");
        System.out.println("1. First Fit");
        System.out.println("2. Next Fit");
        System.out.println("3. Best Fit");
        System.out.println("4. Worst Fit");

        int allocationChoice = scanner.nextInt();

        switch (allocationChoice) {
            case 1:
                MemoryManager.firstFit();
                break;
            case 2:
                MemoryManager.nextFit();
                break;
            case 3:
                MemoryManager.bestFit();
                break;
            case 4:
                MemoryManager.worstFit();
                break;
            default:
                System.out.println("Invalid memory allocation choice. Exiting...");
                break;
        }

        display(); // Display memory status after allocation
    }

    public static void readDataFromFile(String filename) {
        try {
            Scanner readData = new Scanner(new File(filename));

            Partition.partitionNum = readData.nextInt();
            partitions = new Partition[Partition.partitionNum];

            for (int i = 0; i < Partition.partitionNum; i++) {
                partitions[i] = new Partition(readData.nextInt(), 0);
                partitions[i].blockId = partitions[i].partitionId;
            }

            for (int i = 0; i < Partition.partitionNum; i++) {
                partitions[i].partitionSize = readData.nextInt();
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    public static void readData() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of partitions: ");
        int partitionNum = scanner.nextInt();
        partitions = new Partition[partitionNum];

        for (int i = 0; i < partitionNum; i++) {
            System.out.print("Enter partition " + i + " size: ");
            int partitionSize = scanner.nextInt();
            partitions[i] = new Partition(i, partitionSize);
        }

        System.out.print("Enter the number of processes: ");
        int pcbNum = scanner.nextInt();
        pcbs = new PCB[pcbNum];

        for (int i = 0; i < pcbNum; i++) {
            System.out.print("Enter process " + i + " name: ");
            String pidName = scanner.next();
            System.out.print("Enter process " + i + " size: ");
            int pidSize = scanner.nextInt();
            pcbs[i] = new PCB(pidName, pidSize);
        }
    }

    public static void display() {
        System.out.println("\nMemory Status:");
        System.out.println("Partition ID   Partition Size   Block ID");
        for (Partition partition : partitions) {
            System.out.println(partition.partitionId + "\t\t\t" + partition.partitionSize + "\t\t\t" + partition.blockId);
        }
    }

    public static void allocateMemory(String processName, int processSize) {
        for (Partition partition : partitions) {
            if (processSize <= partition.partitionSize) {
                partition.partitionSize -= processSize;
                partition.blockId += partition.partitionSize;
                System.out.println("Process " + processName + " allocated to Partition " + partition.partitionId);
                return;
            }
        }
        System.out.println("Process " + processName + " allocation failed!");
    }
}
