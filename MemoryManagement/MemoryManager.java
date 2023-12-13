package MemoryManagement;


import java.util.ArrayList;
import java.util.List;

public class MemoryManager {

    static Partition[] partitions;
    static PCB[] pcbs;
    static int MIN;
    private static List<MemoryAllocationListener> listeners = new ArrayList<>();



    public static void addMemoryAllocationListener(MemoryAllocationListener listener) {
        listeners.add(listener);
    }

    public static void notifyMemoryAllocated() {
        for (MemoryAllocationListener listener : listeners) {
            listener.memoryAllocated();
        }
    }


    public static Partition[] firstFit(PCB[] pcbs, Partition[] partitions, int currentPCBIndex) {
        Partition[] partition = Partition.firstFit(pcbs, partitions, currentPCBIndex);

        notifyMemoryAllocated();
        return partition;
    }


    public static Partition[] nextFit(PCB[] pcbs, Partition[] partitions, int currentPCBIndex, int lastAllocatedIndex) {
        Partition[] partition = Partition.nextFit(pcbs, partitions, currentPCBIndex,lastAllocatedIndex);
        notifyMemoryAllocated();
        return partition;
    }

    public static Partition[] bestFit(PCB[] pcbs, Partition[] partitions, int currentPCBIndex) {
        Partition[] partition = Partition.bestFit(pcbs, partitions, currentPCBIndex);
        notifyMemoryAllocated();
        return partition;
    }

    public static Partition[] worstFit(PCB[] pcbs, Partition[] partitions, int currentPCBIndex) {
        Partition[] partition = Partition.worstFit(pcbs, partitions, currentPCBIndex);
        notifyMemoryAllocated();
        return partition;
    }



}