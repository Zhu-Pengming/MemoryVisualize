package MemoryManagementApp;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MemoryManagementApp extends Application {

    private Stage primaryStage;
    private int partitionCount;
    private List<Integer> partitionSizes = new ArrayList<>();
    private int processCount;
    private String[] processNames;
    private int[] processSizes;
    private String allocationMethod;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showWelcomeScreen();
    }

    private void showWelcomeScreen() {
        Label welcomeLabel = new Label("欢迎使用内存管理可视化工具");
        Button nextButton = new Button("下一步");

        nextButton.setOnAction(e -> showSelectDataSourceScreen());

        VBox welcomeLayout = new VBox(10);
        welcomeLayout.getChildren().addAll(welcomeLabel, nextButton);
        welcomeLayout.setAlignment(Pos.CENTER);
        welcomeLayout.setPadding(new Insets(20));

        Scene welcomeScene = new Scene(welcomeLayout, 400, 300);
        primaryStage.setScene(welcomeScene);
        primaryStage.show();
    }

    private void showSelectDataSourceScreen() {
        Label selectDataSourceLabel = new Label("选择数据源：");
        RadioButton fileRadioButton = new RadioButton("文件");
        RadioButton userInputRadioButton = new RadioButton("用户输入");
        ToggleGroup dataSourceToggleGroup = new ToggleGroup();
        fileRadioButton.setToggleGroup(dataSourceToggleGroup);
        userInputRadioButton.setToggleGroup(dataSourceToggleGroup);

        Button nextButton = new Button("下一步");
        nextButton.setOnAction(e -> {
            if (fileRadioButton.isSelected()) {
                // 处理文件数据源
                // ...
            } else if (userInputRadioButton.isSelected()) {
                showInputPartitionCountScreen();
            }
        });

        VBox selectDataSourceLayout = new VBox(10);
        selectDataSourceLayout.getChildren().addAll(selectDataSourceLabel, fileRadioButton, userInputRadioButton, nextButton);
        selectDataSourceLayout.setAlignment(Pos.CENTER);
        selectDataSourceLayout.setPadding(new Insets(20));

        Scene selectDataSourceScene = new Scene(selectDataSourceLayout, 400, 300);
        primaryStage.setScene(selectDataSourceScene);
    }

    private void showInputPartitionCountScreen() {
        Label partitionCountLabel = new Label("输入数字分区数：");
        TextField partitionCountTextField = new TextField();
        Button nextButton = new Button("下一步");

        nextButton.setOnAction(e -> {
            try {
                partitionCount = Integer.parseInt(partitionCountTextField.getText());
                showInputPartitionSizeScreen();
            } catch (NumberFormatException ex) {
                showAlert("请输入有效的数字");
            }
        });

        VBox inputPartitionCountLayout = new VBox(10);
        inputPartitionCountLayout.getChildren().addAll(partitionCountLabel, partitionCountTextField, nextButton);
        inputPartitionCountLayout.setAlignment(Pos.CENTER);
        inputPartitionCountLayout.setPadding(new Insets(20));

        Scene inputPartitionCountScene = new Scene(inputPartitionCountLayout, 400, 300);
        primaryStage.setScene(inputPartitionCountScene);
    }

    private void showInputPartitionSizeScreen() {
        // 输入分区大小
        VBox inputPartitionSizeLayout = new VBox(10); // Move this line up

        for (int i = 0; i < partitionCount; i++) {
            Label partitionSizeLabel = new Label("输入分区 " + (i + 1) + " 的大小：");
            TextField partitionSizeTextField = new TextField();
            inputPartitionSizeLayout.getChildren().addAll(partitionSizeLabel, partitionSizeTextField);
        }

        Button nextButton = new Button("下一步");
        nextButton.setOnAction(e -> {
            for (int i = 0; i < partitionCount; i++) {
                TextField textField = (TextField) inputPartitionSizeLayout.getChildren().get(i * 2 + 1);
                try {
                    int size = Integer.parseInt(textField.getText());
                    if (size > 0) {
                        partitionSizes.add(size);
                    } else {
                        showAlert("分区大小必须大于零");
                        return;  // Stop processing if an error occurs
                    }
                } catch (NumberFormatException ex) {
                    showAlert("请输入有效的数字");
                    return;  // Stop processing if an error occurs
                }
            }
            // Move to the next step after all partition sizes are entered
            showRealTimeMemoryUsageScreen();
        });

        inputPartitionSizeLayout.getChildren().add(nextButton);
        inputPartitionSizeLayout.setAlignment(Pos.CENTER);
        inputPartitionSizeLayout.setPadding(new Insets(20));

        Scene inputPartitionSizeScene = new Scene(inputPartitionSizeLayout, 400, 300);
        primaryStage.setScene(inputPartitionSizeScene);
    }
    

    // 实现其他界面的方法

    private void showRealTimeMemoryUsageScreen() {
        // 根据用户的选择实时显示内存使用情况
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
