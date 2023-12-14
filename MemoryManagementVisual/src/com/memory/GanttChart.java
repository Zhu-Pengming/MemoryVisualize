package com.memory;

import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.Arrays;

public class GanttChart {

    private BarChart<Number, String> chart;
    private int colorIndex = 0;

    public GanttChart(String[] processNames) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setAutoRanging(false);
        xAxis.setTickUnit(1); // 设置刻度间隔为1
        xAxis.setMinorTickCount(0); // 设置次要刻度为0
        CategoryAxis yAxis = new CategoryAxis();
        yAxis.setCategories(FXCollections.observableArrayList(Arrays.asList(processNames)));
        chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Gantt Chart");
        chart.setCategoryGap(1);
    }



    public BarChart<Number, String> getChart() {
        return chart;
    }

    public void updateGanttChart(PCB allocatedPCB, int startTime, int size) {
        String[] vibrantColors = {
                "#FF6347", "#FFD700", "#FF69B4", "#00FF7F",
                "#1E90FF", "#FFA07A", "#FF4500", "#FF8C00",
                "#8A2BE2", "#32CD32", "#FF1493", "#00FFFF"
        };

        String color = vibrantColors[colorIndex];

        int endTime = startTime + size; // Calculate endTime

        XYChart.Series<Number, String> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(startTime, allocatedPCB.getPidName(), new CustomData(size, color, endTime)));
        chart.getData().add(series);

        colorIndex = (colorIndex + 1) % vibrantColors.length; // Cycle through colors
    }




    public static class CustomData {
        private double duration;
        private String color;
        private double endTime;

        public CustomData(double duration, String color, double endTime) {
            this.duration = duration;
            this.color = color;
            this.endTime = endTime;
        }

        public double getDuration() {
            return duration;
        }

        public String getColor() {
            return color;
        }

        public double getEndTime() {
            return endTime;
        }
    }

}
