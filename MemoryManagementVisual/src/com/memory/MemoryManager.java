package MemoryManagementApp;


import java.util.ArrayList;
import java.util.List;
import MemoryManagementApp.Partition;

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

    
    public static Partition firstFit(PCB[] pcbs, Partition[] partitions, int currentPCBIndex) {
        // Create an instance of Partition with default values (you may adjust the parameters accordingly)
        Partition partitionInstance = new Partition(0, 0.0);
        
        // Call the non-static method on the instance
        Partition partition = partitionInstance.firstFit(pcbs, partitions, currentPCBIndex);
        
        if (partition != null) {
            partition.blockId += partition.getSize();
            partition.partitionSize -= pcbs[currentPCBIndex].getPidSize();
            if (partition.partitionSize <= MemoryManager.MIN) {
                partition.partitionSize = 0;
            }
        }
        notifyMemoryAllocated();
        display();
        return partition;
    }

    public static Partition nextFit(PCB[] pcbs, Partition[] partitions, int currentPCBIndex) {
        Partition partition = Partition.nextFit(pcbs, partitions, currentPCBIndex);
        if (partition != null) {
            partition.blockId += partition.getSize();
            partition.partitionSize -= pcbs[currentPCBIndex].getPidSize();
            if (partition.partitionSize <= MemoryManager.MIN) {
                partition.partitionSize = 0;
            }
        }
        notifyMemoryAllocated();
        display();
        return partition;
    }

    public static Partition bestFit(PCB[] pcbs, Partition[] partitions, int currentPCBIndex) {
        Partition partition = Partition.bestFit(pcbs, partitions, currentPCBIndex);
        if (partition != null) {
            partition.blockId += partition.getSize();
            partition.partitionSize -= pcbs[currentPCBIndex].getPidSize();
            if (partition.partitionSize <= MemoryManager.MIN) {
                partition.partitionSize = 0;
            }
        }
        notifyMemoryAllocated();
        display();
        return partition;
    }

    public static Partition worstFit(PCB[] pcbs, Partition[] partitions, int currentPCBIndex) {
        Partition partition = Partition.worstFit(pcbs, partitions, currentPCBIndex);
        if (partition != null) {
            partition.blockId += partition.getSize();
            partition.partitionSize -= pcbs[currentPCBIndex].getPidSize();
            if (partition.partitionSize <= MemoryManager.MIN) {
                partition.partitionSize = 0;
            }
        }
        notifyMemoryAllocated();
        display();
        return partition;
    }

    
    
}
