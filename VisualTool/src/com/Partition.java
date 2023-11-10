package com;

import java.util.*;

import static com.MemoryManager.MIN;


public class Partition {
    public static int partitionNum;
    int partitionId;
    int partitionSize;
    int blockId;

    // Constructor
    public Partition(int id, int size) {
        partitionId = id;
        partitionSize = size;
        blockId = partitionId;
    }

    public static void readData() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of partitions: ");
        partitionNum = scanner.nextInt();
        MemoryManagement.partitions = new Partition[partitionNum];

        for (int i = 0; i < partitionNum; i++) {
            System.out.print("Enter partition " + i + " size: ");
            int partitionSize = scanner.nextInt();
            MemoryManagement.partitions[i] = new Partition(i, partitionSize);
        }

        System.out.print("Enter the number of processes: ");
        int pcbNum = scanner.nextInt();
        MemoryManagement.pcbs = new PCB[pcbNum];

        for (int i = 0; i < pcbNum; i++) {
            System.out.print("Enter process " + i + " name: ");
            String pidName = scanner.next();
            System.out.print("Enter process " + i + " size: ");
            int pidSize = scanner.nextInt();
            MemoryManagement.pcbs[i] = new PCB(pidName, pidSize);
        }
    }

    public static void display1() {
        System.out.println("Displaying 1");
        // Implement display1 logic
    }

    public static void displayPartition() {
        System.out.println("Displaying Partition");
        // Implement displayPartition logic
    }

    // Display information for all partitions
    public static void display() {
        System.out.println("\nPartition Status:");
        System.out.println("Partition ID   Partition Size   Block ID");

        for (Partition partition : MemoryManagement.partitions) {
            System.out.println(partition.partitionId + "\t\t\t" + partition.partitionSize + "\t\t\t" + partition.blockId);
        }
    }

    public void allocateMemory(PCB newProcess) {
        for (int j = 0; j < MemoryManagement.partitions.length; j++) {
            if (newProcess.pidSize <= MemoryManagement.partitions[j].partitionSize) {
                MemoryManagement.partitions[j].partitionSize -= newProcess.pidSize;
                MemoryManagement.partitions[j].blockId += MemoryManagement.partitions[j].partitionSize;
                if (MemoryManagement.partitions[j].partitionSize <= MIN) {
                    MemoryManagement.partitions[j].partitionSize = 0;
                }
                System.out.println("Process " + newProcess.pidName + " allocated to Partition " + MemoryManagement.partitions[j].partitionId);
                return;
            }
        }
        System.out.println("Process " + newProcess.pidName + " allocation failed!");
    }



    public void firstFit(ArrayList<PCB> pcbs, Partition[] partitions) {
        boolean flag;
        int i, j;
        String choose;
        displayPartition();

        do {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter process name: ");
            String pidName = scanner.next();
            System.out.print("Enter process size: ");
            int pidSize = scanner.nextInt();

            PCB newProcess = new PCB(pidName, pidSize);
            pcbs.add(newProcess);

            i = pcbs.size() - 1;

            flag = false;
            for (j = 0; j < partitions.length; j++) {
                if (pcbs.get(i).pidSize <= partitions[j].partitionSize) {
                    partitions[j].blockId += partitions[j].partitionSize; // 先更新 blockId
                    partitions[j].partitionSize -= pcbs.get(i).pidSize;
                    if (partitions[j].partitionSize <= MIN) {
                        partitions[j].partitionSize = 0;
                    }
                    flag = true;
                    break;
                }
            }

            if(flag){
                flag = false;
                System.out.println("Process " + pcbs.get(i).pidName + " allocated to Partition " + partitions[j].partitionId);
            } else {
                System.out.println("Process " + pcbs.get(i).pidName + " allocation failed!");
            }

            display1();

            System.out.print("Continue allocation? (Y/N): ");
            choose = scanner.next();

        } while (choose.equals("Y"));
    }


    // Next Fit memory allocation algorithm
    public void nextFit(ArrayList<PCB> pcbs, Partition[] partitions) {
        int pos = 0;
        boolean flag = false;
        int i, j;
        String choose;
        displayPartition();

        do {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter process name: ");
            String pidName = scanner.next();
            System.out.print("Enter process size: ");
            int pidSize = scanner.nextInt();

            PCB newProcess = new PCB(pidName, pidSize);
            pcbs.add(newProcess);

            i = pcbs.size() - 1;

            for (j = pos; ; j++) {
                if (pos >= partitions.length) {
                    pos = 0;
                }
                if (pcbs.get(i).pidSize <= partitions[j].partitionSize) {
                    partitions[j].blockId += partitions[j].partitionSize;
                    partitions[j].partitionSize -= pcbs.get(i).pidSize;
                    if (partitions[j].partitionSize <= MIN) {
                        partitions[j].partitionSize = 0;
                    }
                    flag = true;
                    pos = j + 1;
                    if (pos == partitions.length) {
                        pos = 0;
                    }
                    break;
                }
            }

            if (flag) {
                flag = false;
                System.out.println("Process " + pcbs.get(i).pidName + " allocated to Partition " + partitions[j].partitionId);
            } else {
                System.out.println("Process " + pcbs.get(i).pidName + " allocation failed!");
            }

            display1();

            System.out.print("Continue allocation? (Y/N): ");
            choose = scanner.next();

        } while (choose.equals("Y"));
    }

    // Best Fit memory allocation algorithm
    public void bestFit(ArrayList<PCB> pcbs, Partition[] partitions) {
        int pos = 0;
        boolean flag = false;
        int i, j;
        String choose;
        displayPartition();

        do {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter process name: ");
            String pidName = scanner.next();
            System.out.print("Enter process size: ");
            int pidSize = scanner.nextInt();

            PCB newProcess = new PCB(pidName, pidSize);
            pcbs.add(newProcess);

            i = pcbs.size() - 1;

            TreeMap<Integer, Partition> sortedPartitions = new TreeMap<>();
            for (j = 0; j < partitions.length; j++) {
                sortedPartitions.put(partitions[j].partitionSize, partitions[j]);
            }

            boolean allocated = false;
            for (Map.Entry<Integer, Partition> entry : sortedPartitions.entrySet()) {
                if (pcbs.get(i).pidSize <= entry.getKey()) {
                    Partition selectedPartition = entry.getValue();
                    selectedPartition.blockId += selectedPartition.partitionSize;
                    selectedPartition.partitionSize -= pcbs.get(i).pidSize;

                    if (selectedPartition.partitionSize <= MIN) {
                        selectedPartition.partitionSize = 0;
                    }

                    flag = true;
                    allocated = true;
                    break;
                }
            }

            if (allocated) {
                flag = false;
                System.out.println("Process " + pcbs.get(i).pidName + " allocated to Partition " + partitions[j].partitionId);
            } else {
                System.out.println("Process " + pcbs.get(i).pidName + " allocation failed!");
            }

            display1();

            System.out.print("Continue allocation? (Y/N): ");
            choose = scanner.next();

        } while (choose.equals("Y"));
    }

    public static void worstFit(ArrayList<PCB> pcbs, Partition[] partitions) {
        int pos = 0;
        boolean flag = false;
        int i, j;
        TreeMap<Integer, Partition> m = new TreeMap<>(Collections.reverseOrder());
        String choose;

        readData();

        do {
            displayPartition();

            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter process name: ");
            pcbs.get(PCB.pcbNum - 1).pidName = scanner.next();
            System.out.print("Enter process size: ");
            pcbs.get(PCB.pcbNum - 1).pidSize = scanner.nextInt();
            i = PCB.pcbNum - 1;

            m.clear();
            for (j = 0; j < partitions.length; j++) {
                m.put(partitions[j].partitionSize, partitions[j]);
            }

            boolean allocated = false;
            for (Map.Entry<Integer, Partition> entry : m.entrySet()) {
                if (pcbs.get(i).pidSize <= entry.getKey()) {
                    Partition selectedPartition = entry.getValue();
                    selectedPartition.partitionSize -= pcbs.get(i).pidSize;
                    selectedPartition.blockId += selectedPartition.partitionSize;

                    if (selectedPartition.partitionSize <= MIN) {
                        selectedPartition.partitionSize = 0;
                    }

                    flag = true;
                    allocated = true;
                    break;
                }
            }

            if (allocated) {
                flag = false;
                System.out.println("Process " + pcbs.get(i).pidName + " allocated to Partition " + partitions[j].partitionId);
            } else {
                System.out.println("Process " + pcbs.get(i).pidName + " allocation failed!");
            }

            display1();

            System.out.print("Continue allocation? (Y/N): ");
            choose = scanner.next();

        } while (choose.equals("Y"));
    }

}

