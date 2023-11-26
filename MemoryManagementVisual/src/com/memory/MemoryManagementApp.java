package com.memory;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MemoryManagementApp extends Application implements MemoryAllocationListener{

    private Stage primaryStage;
    private int partitionCount;
    private List<Integer> partitionSizes = new ArrayList<>();
    private int processCount;
    private String[] processNames;
    private int[] processSizes;
    private String allocationMethod;

    static Partition[] partitions;
    static PCB[] pcbs;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        // Initialize MemoryManager listeners
        MemoryManager.addMemoryAllocationListener(this);
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
        Label partitionCountLabel = new Label("输入分区数：");
        TextField partitionCountTextField = new TextField();
        Button nextButton = new Button("下一步");

        nextButton.setOnAction(e -> {
            try {
                partitionCount = Integer.parseInt(partitionCountTextField.getText());
                partitions = new Partition[partitionCount];
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
                        partitions[i] = new Partition(i, size);
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
            showProcessCountScreen();
        });

        inputPartitionSizeLayout.getChildren().add(nextButton);
        inputPartitionSizeLayout.setAlignment(Pos.CENTER);
        inputPartitionSizeLayout.setPadding(new Insets(20));

        Scene inputPartitionSizeScene = new Scene(inputPartitionSizeLayout, 400, 300);
        primaryStage.setScene(inputPartitionSizeScene);
    }

    private void showProcessCountScreen() {
        Label processCountLabel = new Label("输入进程数：");
        TextField processCountTextField = new TextField();
        Button nextButton = new Button("下一步");

        nextButton.setOnAction(e -> {
            try {
                processCount = Integer.parseInt(processCountTextField.getText());
                pcbs = new PCB[partitionCount];
                showInputProcessInfoScreen();
            } catch (NumberFormatException ex) {
                showAlert("请输入有效的数字");
            }
        });

        VBox inputProcessCountLayout = new VBox(10);
        inputProcessCountLayout.getChildren().addAll(processCountLabel, processCountTextField, nextButton);
        inputProcessCountLayout.setAlignment(Pos.CENTER);
        inputProcessCountLayout.setPadding(new Insets(20));

        Scene inputProcessCountScene = new Scene(inputProcessCountLayout, 400, 300);
        primaryStage.setScene(inputProcessCountScene);
    }

    private void showInputProcessInfoScreen() {
        VBox inputProcessInfoLayout = new VBox(10);

        for (int i = 0; i < processCount; i++) {
            Label processNameLabel = new Label("输入进程 " + (i + 1) + " 的名称：");
            TextField processNameTextField = new TextField();
            Label processSizeLabel = new Label("输入进程 " + (i + 1) + " 的大小：");
            TextField processSizeTextField = new TextField();

            inputProcessInfoLayout.getChildren().addAll(processNameLabel, processNameTextField, processSizeLabel, processSizeTextField);
        }

        Button nextButton = new Button("下一步");
        nextButton.setOnAction(e -> {

            pcbs = new PCB[processCount];

            for (int i = 0; i < processCount; i++) {
                TextField nameField = (TextField) inputProcessInfoLayout.getChildren().get(i * 4 + 1);
                TextField sizeField = (TextField) inputProcessInfoLayout.getChildren().get(i * 4 + 3);

                try {
                    String name = nameField.getText();
                    int size = Integer.parseInt(sizeField.getText());
                    pcbs[i] = new PCB(name, size);
                    // You can also perform validation or additional processing here
                } catch (NumberFormatException ex) {
                    showAlert("请输入有效的数字");
                    return;  // Stop processing if an error occurs
                }
            }


            // Move to the next step after all process information is entered
            showMemoryAllocationMethodScreen();
        });

        inputProcessInfoLayout.getChildren().add(nextButton);
        inputProcessInfoLayout.setAlignment(Pos.CENTER);
        inputProcessInfoLayout.setPadding(new Insets(20));

        Scene inputProcessInfoScene = new Scene(inputProcessInfoLayout, 400, 300);
        primaryStage.setScene(inputProcessInfoScene);
    }

    private void showMemoryAllocationMethodScreen() {
        Label methodLabel = new Label("选择内存分配方法：");

        ChoiceBox<String> methodChoiceBox = new ChoiceBox<>();
        methodChoiceBox.getItems().addAll("First Fit", "Next Fit","Best Fit", "Worst Fit");
        methodChoiceBox.setValue("First Fit"); // Default selection

        Button allocateButton = new Button("分配内存");
        allocateButton.setOnAction(e -> performMemoryAllocation(methodChoiceBox.getValue()));

        VBox methodLayout = new VBox(10);
        methodLayout.getChildren().addAll(methodLabel, methodChoiceBox, allocateButton);
        methodLayout.setAlignment(Pos.CENTER);
        methodLayout.setPadding(new Insets(20));

        Scene methodScene = new Scene(methodLayout, 400, 300);
        primaryStage.setScene(methodScene);
    }

    private void performMemoryAllocation(String selectedMethod) {
        switch (selectedMethod) {
            case "First Fit":
                // 调用您的 First Fit 内存分配算法
                MemoryManager.firstFit();
                break;
            case "Next Fit":
                // 调用您的 Next Fit 内存分配算法
                MemoryManager.nextFit();
                break;
            case "Best Fit":
                // 调用您的 Best Fit 内存分配算法
                MemoryManager.bestFit();
                break;
            case "Worst Fit":
                // 调用您的 Worst Fit 内存分配算法
                MemoryManager.worstFit();
                break;
            default:
                showAlert("未知的内存分配方法");
                return; // Stop processing if an error occurs
        }

        // 分配完成后更新UI
        updateMemoryVisualization();
    }



    private void updateMemoryVisualization() {
        int x = 100;
        int y = 100;

        Text text = new Text(x, y, "JavaFX 2.0");


    }





    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void memoryAllocated() {
        // This method is called when memory is allocated
        // Update the UI here
        updateMemoryVisualization();
        // You may want to add additional logic here based on the memory allocation event
        // For example, showing a success message or handling other UI updates
    }

}