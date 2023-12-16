package com.memory;

import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class GanttChart {

    private BarChart<Number, String> chart;
    private Map<String, XYChart.Series<Number, String>> seriesMap;
    private CategoryAxis yAxis;
    private Set<String> categories; // Maintain a set of unique categories
    private List<String> processOrder;

    public GanttChart(String[] processNames) {
        NumberAxis xAxis = new NumberAxis();
        yAxis = new CategoryAxis();
        xAxis.setAutoRanging(false);
        xAxis.setTickUnit(1);
        chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Gantt Chart");
        seriesMap = new HashMap<>();
        categories = FXCollections.observableSet(processNames);
        processOrder = new ArrayList<>(categories);
        yAxis.setCategories(FXCollections.observableArrayList(processOrder));
    }

    public void updateChart(String processName, int startTime, int size) {
        int endTime = startTime + size;

        XYChart.Series<Number, String> series = seriesMap.computeIfAbsent(processName, k -> {
            XYChart.Series<Number, String> newSeries = new XYChart.Series<>();
            chart.getData().add(newSeries);
            return newSeries;
        });
        series.getData().add(new XYChart.Data<>(endTime, processName, size));
        System.out.println("Updating Chart: Process " + processName +
                " - Start: " + startTime +
                ", End: " + endTime);
    }

    public BarChart<Number, String> getChart() {
        return chart;
    }
}
