package MemoryManagementApp;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;
import static MemoryManagementApp.MemoryManager.MIN;


public class Partition {
    private boolean allocated; 
    public static int partitionNum;
    int partitionId;
    int partitionSize;
    int blockId;





    // For JavaFX visualization
    private Rectangle visualizationRect;

    // Constructor
    public Partition(int id, double size) {
        partitionId = id;
        partitionSize = (int) size;
        blockId = partitionId;

        
    }

    // JavaFX visualization
    public Rectangle getVisualizationRect() {
        return visualizationRect;
    }

    // Update the visualization color based on allocation status
    public void updateVisualization() {
        if (partitionSize == 0) {
            visualizationRect.setFill(Color.GREEN); // Allocated
        } else {
            visualizationRect.setFill(Color.RED); // Not allocated
        }
    }

    // Allocate memory and update the visualization
    public void allocateMemory(PCB newProcess) {
        for (int j = 0; j < MemoryManagementApp.partitions.length; j++) {
            if (newProcess.getPidSize() <= MemoryManagementApp.partitions[j].partitionSize) {
                MemoryManagementApp.partitions[j].partitionSize -= newProcess.getPidSize();
                MemoryManagementApp.partitions[j].blockId += MemoryManagementApp.partitions[j].partitionSize;
                if (MemoryManagementApp.partitions[j].partitionSize <= MemoryManager.MIN) {
                    MemoryManagementApp.partitions[j].partitionSize = 0;
                }
                System.out.println("Process " + newProcess.getPidName() + " allocated to Partition " + MemoryManagementApp.partitions[j].partitionId);

                // Update the visualization after allocation
                MemoryManagementApp.partitions[j].updateVisualization();
                return;
            }
        }
        System.out.println("Process " + newProcess.getPidName() + " allocation failed!");
    }


    // Additional method to retrieve partition information
    public String getPartitionInfo() {
        return "Partition " + partitionId + " - Size: " + partitionSize + " - BlockId: " + blockId;
    }


    public void setAllocated(boolean allocated) {
        this.allocated = allocated;
    }

    
    public Partition[] firstFit(PCB[] pcbs, Partition[] partitions, int currentPCBIndex) {
        for (int j = 0; j < partitions.length; j++) {
            if (!partitions[j].isAllocated() && pcbs[currentPCBIndex].getPidSize() <= partitions[j].partitionSize) {
                partitions[j].blockId += partitions[j].partitionSize;
                partitions[j].partitionSize -= pcbs[currentPCBIndex].getPidSize();
                if (partitions[j].partitionSize <= MemoryManager.MIN) {
                    partitions[j].partitionSize = 0;
                }
                partitions[j].setAllocated(true);  // Mark the partition as allocated
            }
        }
        
        return partitions;  // Return the modified array
    }
    


    // Next Fit memory allocation algorithm
    public void nextFit(PCB[] pcbs, Partition[] partitions) {
        int pos = 0;
        boolean flag = false;
        int i, j;
        String choose;

        Scanner scanner = new Scanner(System.in);

        do {


            i = pcbs.length - 1;

            for (j = pos; ; j++) {
                if (pos >= partitions.length) {
                    pos = 0;
                }
                if (pcbs[i].getPidSize() <= partitions[j].partitionSize) {
                    partitions[j].blockId += partitions[j].partitionSize;
                    partitions[j].partitionSize -= pcbs[i].getPidSize();
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
                System.out.println("Process " + pcbs[i].getPidName()+ " allocated to Partition " + partitions[j].partitionId);
            } else {
                System.out.println("Process " + pcbs[i].getPidName()+ " allocation failed!");
            }


            System.out.print("Continue allocation? (Y/N): ");
            choose = scanner.next();

        } while (choose.equals("Y"));
    }

    // Best Fit memory allocation algorithm
    public void bestFit(PCB[] pcbs, Partition[] partitions) {
        int pos = 0;
        boolean flag = false;
        int i, j;
        String choose;

        Scanner scanner = new Scanner(System.in);

        do {

            i = pcbs.length - 1;

            TreeMap<Integer, Partition> sortedPartitions = new TreeMap<>();
            for (j = 0; j < partitions.length; j++) {
                sortedPartitions.put(partitions[j].partitionSize, partitions[j]);
            }

            boolean allocated = false;
            for (Map.Entry<Integer, Partition> entry : sortedPartitions.entrySet()) {
                if (pcbs[i].getPidSize() <= entry.getKey()) {
                    Partition selectedPartition = entry.getValue();
                    selectedPartition.blockId += selectedPartition.partitionSize;
                    selectedPartition.partitionSize -= pcbs[i].getPidSize();

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
                System.out.println("Process " + pcbs[i].getPidName() + " allocated to Partition " + partitions[j].partitionId);
            } else {
                System.out.println("Process " + pcbs[i].getPidName() + " allocation failed!");
            }


            System.out.print("Continue allocation? (Y/N): ");
            choose = scanner.next();

        } while (choose.equals("Y"));
    }

    public static void worstFit(PCB[] pcbs, Partition[] partitions) {
        int pos = 0;
        boolean flag = false;
        int i, j;
        TreeMap<Integer, Partition> m = new TreeMap<>(Collections.reverseOrder());
        String choose;
        Scanner scanner = new Scanner(System.in);


        do {

            i = PCB.pcbNum - 1;

            m.clear();
            for (j = 0; j < partitions.length; j++) {
                m.put(partitions[j].partitionSize, partitions[j]);
            }

            boolean allocated = false;
            for (Map.Entry<Integer, Partition> entry : m.entrySet()) {
                if (pcbs[i].getPidSize() <= entry.getKey()) {
                    Partition selectedPartition = entry.getValue();
                    selectedPartition.partitionSize -= pcbs[i].getPidSize();
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
                System.out.println("Process " + pcbs[i].getPidName() + " allocated to Partition " + partitions[j].partitionId);
            } else {
                System.out.println("Process " + pcbs[i].getPidName() + " allocation failed!");
            }


            System.out.print("Continue allocation? (Y/N): ");

            choose = scanner.next();

        } while (choose.equals("Y"));
    }

    public double getSize() {
        return partitionSize;
    }

    public boolean isAllocated() {
        return partitionSize == 0;
    }
}







