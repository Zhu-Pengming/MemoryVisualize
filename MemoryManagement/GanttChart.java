package MemoryManagement;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class GanttChart {

    private BarChart<Number, String> chart;
    private int colorIndex = 0;

    public GanttChart() {
        NumberAxis xAxis = new NumberAxis();
        CategoryAxis yAxis = new CategoryAxis();
        chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Gantt Chart");
    }

    public BarChart<Number, String> getChart() {
        return chart;
    }

    public void updateChart(String processName, int startTime, int size) {
        XYChart.Series<Number, String> series = new XYChart.Series<>();
    
        String[] colors = {"#FF0000", "#00FF00", "#0000FF", "#FFFF00"}; // Add more colors if needed
        String color = colors[colorIndex];
    
        int endTime = startTime + size; // Calculate endTime
    
        series.getData().add(new XYChart.Data<>(startTime, processName, new CustomData(size, color)));
        chart.getData().add(series);
    
        colorIndex = (colorIndex + 1) % colors.length; // Cycle through colors
    }
    

    // Add this static inner class to represent custom data for each data point
    public static class CustomData {
        private double duration;
        private String color;

        public CustomData(double duration, String color) {
            this.duration = duration;
            this.color = color;
        }

        public double getDuration() {
            return duration;
        }

        public String getColor() {
            return color;
        }
    }
}
