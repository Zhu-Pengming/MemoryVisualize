package MemoryManagement;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import static MemoryManagement.MemoryManager.MIN;

import java.util.concurrent.BlockingDeque;


public class Partition {
    private boolean allocated;
    public static int partitionNum;
    int partitionId;
    int partitionSize;
    int blockId;

    private Rectangle visualizationRect;

    // Constructor
    public Partition(int id, double size) {
        partitionId = id;
        partitionSize = (int) size;
        blockId = partitionId;


        // Initialize visualizationRect (assumes Rectangle as the visualization)
        visualizationRect = new Rectangle(100, 20); // Set width and height as per your visualization needs
        visualizationRect.setStroke(Color.BLACK); // Set stroke color
        visualizationRect.setStrokeWidth(1); // Set stroke width
        visualizationRect.setFill(Color.RED); // Initial color (not allocated)

        // Additional initialization or styling if needed
    }


    public void setAllocated(boolean allocated) {
        this.allocated = allocated;
    }


    public static Partition[] firstFit(PCB[] pcbs, Partition[] partitions, int currentPCBIndex) {
        for (int j = 0; j < partitions.length; j++) {
            if (!partitions[j].isAllocated() && pcbs[currentPCBIndex].getPidSize() <= partitions[j].partitionSize) {
                partitions[j].blockId += partitions[j].partitionSize;
                partitions[j].partitionSize -= pcbs[currentPCBIndex].getPidSize();
                if (partitions[j].partitionSize <= MIN) {
                    partitions[j].partitionSize = 0;
                }
                partitions[j].setAllocated(true);  // Mark the partition as allocated
            }
        }

        return partitions;  // Return the modified array
    }

    public static Partition[] worstFit(PCB[] pcbs, Partition[] partitions, int currentPCBIndex) {
        int worstFitIndex = -1;
        int worstFitSize = -1;

        for (int j = 0; j < partitions.length; j++) {
            if (!partitions[j].isAllocated() && pcbs[currentPCBIndex].getPidSize() <= partitions[j].partitionSize) {
                int remainingSize = partitions[j].partitionSize - pcbs[currentPCBIndex].getPidSize();
                if (worstFitIndex == -1 || remainingSize > worstFitSize) {
                    worstFitIndex = j;
                    worstFitSize = remainingSize;
                }
            }
        }

        if (worstFitIndex != -1) {
            partitions[worstFitIndex].blockId += partitions[worstFitIndex].partitionSize;
            partitions[worstFitIndex].partitionSize -= pcbs[currentPCBIndex].getPidSize();
            if (partitions[worstFitIndex].partitionSize <= MIN) {
                partitions[worstFitIndex].partitionSize = 0;
            }
            partitions[worstFitIndex].setAllocated(true);
        }

        return partitions;
    }

    public static Partition[] nextFit(PCB[] pcbs, Partition[] partitions, int currentPCBIndex, int lastAllocatedIndex) {
        // Start searching from the last allocated index
        int start = lastAllocatedIndex;

        for (int i = 0; i < partitions.length; i++) {
            int j = (start + i) % partitions.length;
            if (!partitions[j].isAllocated() && pcbs[currentPCBIndex].getPidSize() <= partitions[j].partitionSize) {
                partitions[j].blockId += partitions[j].partitionSize;
                partitions[j].partitionSize -= pcbs[currentPCBIndex].getPidSize();
                if (partitions[j].partitionSize <= MIN) {
                    partitions[j].partitionSize = 0;
                }
                partitions[j].setAllocated(true);
                return partitions;
            }
        }

        // If no suitable partition is found, return the original array
        return partitions;
    }

    public static Partition[] bestFit(PCB[] pcbs, Partition[] partitions, int currentPCBIndex) {
        int bestFitIndex = -1;
        int bestFitSize = Integer.MAX_VALUE;

        for (int j = 0; j < partitions.length; j++) {
            if (!partitions[j].isAllocated() && pcbs[currentPCBIndex].getPidSize() <= partitions[j].partitionSize) {
                int remainingSize = partitions[j].partitionSize - pcbs[currentPCBIndex].getPidSize();
                if (remainingSize < bestFitSize) {
                    bestFitIndex = j;
                    bestFitSize = remainingSize;
                }
            }
        }

        if (bestFitIndex != -1) {
            partitions[bestFitIndex].blockId += partitions[bestFitIndex].partitionSize;
            partitions[bestFitIndex].partitionSize -= pcbs[currentPCBIndex].getPidSize();
            if (partitions[bestFitIndex].partitionSize <= MIN) {
                partitions[bestFitIndex].partitionSize = 0;
            }
            partitions[bestFitIndex].setAllocated(true);
        }

        return partitions;
    }

    public int getSize() {
        return partitionSize;
    }

    public boolean isAllocated() {
        return partitionSize == 0;
    }


    public void updateVisualization() {
        if (partitionSize == 0) {
            visualizationRect.setFill(Color.GREEN); // Allocated
        } else {
            visualizationRect.setFill(Color.RED); // Not allocated
        }
    }

    public String getPartitionInfo() {
        return "Partition " + partitionId + " - Size: " + partitionSize + " - BlockId: " + blockId;
    }


    public int getBlockId() {
        return blockId;
    }

}