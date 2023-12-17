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

    private int currentStartTime;

    public GanttChart(String[] processNames, int startTime) {
        NumberAxis xAxis = new NumberAxis(startTime, startTime + 10, 1); // 适当调整范围
        yAxis = new CategoryAxis();
        chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Gantt Chart");
        seriesMap = new HashMap<>();
        categories = FXCollections.observableSet(processNames);
        processOrder = new ArrayList<>(categories);
        yAxis.setCategories(FXCollections.observableArrayList(processOrder));
    }

    public void updateChart(String processName, int actualStartTime, int size) {
        int endTime = actualStartTime + size;

        XYChart.Series<Number, String> series = seriesMap.computeIfAbsent(processName, k -> {
            XYChart.Series<Number, String> newSeries = new XYChart.Series<>();
            chart.getData().add(newSeries);
            return newSeries;
        });

        series.getData().add(new XYChart.Data<>(actualStartTime, processName, size));
        series.getData().add(new XYChart.Data<>(endTime, processName, 0));

        System.out.println("Updating Chart: Process " + processName +
                " - Start: " + actualStartTime +
                ", End: " + endTime);
    }





    public BarChart<Number, String> getChart() {
        return chart;
    }
}