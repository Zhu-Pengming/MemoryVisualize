package com.memory;

package MemoryManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class GanttChart {

    private BarChart<Number, String> chart;
    private Map<String, XYChart.Series<Number, String>> seriesMap;
    private CategoryAxis yAxis;
    private Set<String> categories; // Maintain a set of unique categories
    private List<String> processOrder;

    public GanttChart() {
        NumberAxis xAxis = new NumberAxis();
        yAxis = new CategoryAxis();  
        xAxis.setAutoRanging(false);
        xAxis.setTickUnit(1);
        chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Gantt Chart");

        seriesMap = new HashMap<>();
        categories = new LinkedHashSet<>();
        processOrder = new ArrayList<>();

        
    }


    public BarChart<Number, String> getChart() {
        return chart;
    }

    public void updateChart(String processName, int startTime, int size) {
        int endTime = startTime + size;
    
        // Add the category only if it's not in the set
        if (!categories.contains(processName)) {
            categories.add(processName);
            processOrder.add(processName); // Add the process to the order list
            yAxis.setCategories(FXCollections.observableArrayList(processOrder));
        }
    
        XYChart.Series<Number, String> series = seriesMap.computeIfAbsent(processName, k -> {
            XYChart.Series<Number, String> newSeries = new XYChart.Series<>();
            chart.getData().add(newSeries);
            return newSeries;
        });
    
        // Use the end time as the x-coordinate, and set the correct duration
        series.getData().add(new XYChart.Data<>(endTime, processName, new CustomData(size)));
    
        System.out.println("Updating Chart: Process " + processName + " - Start: " + startTime + ", End: " + endTime);
    }

    
    
    
    
    public static class CustomData {
        private double duration;

        public CustomData(double duration) {
            this.duration = duration;
        }

        public double getDuration() {
            return duration;
        }
    }
}
