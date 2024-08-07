package com.example.compiler;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.io.File;
import java.io.FileNotFoundException;

public class Main extends Application {

      CompilerParser compilerParser;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("LL1 Parser");

        // Create a FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");


// Set the initial directory to the "compiler files" folder on the desktop
        String userDesktopPath = System.getProperty("user.home") + "/Desktop/codfmpiler files";
        File compilerFilesDir = new File(userDesktopPath);
        if (compilerFilesDir.exists() && compilerFilesDir.isDirectory()) {
            fileChooser.setInitialDirectory(compilerFilesDir);
        }

        Label label = new Label("You can choose any file\n" +
                "            from here ");
        label.setFont(Font.font("BOLD", FontWeight.BOLD, 16));  // Increase the font size and make it bold
        label.setTextFill(Color.WHITE);  // Set the text color to white

        Button btn = new Button("Choose the file");
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Text selectedFileText = new Text();
        selectedFileText.setFont(Font.font("Arial", 15));
        selectedFileText.setFill(Color.WHITE);

        // Set button action
        btn.setOnAction(e -> {

            try {
                File selectedFile = fileChooser.showOpenDialog(primaryStage);
                if (selectedFile != null) {
                    selectedFileText.setText("Selected file: " + selectedFile.getAbsolutePath());
                    try {
                        compilerParser = new CompilerParser(selectedFile);
                        compilerParser.parse();
                    } catch (Exception q) {
                        try {
                            throw new Exception(q.getMessage());
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                } else {
                    selectedFileText.setText("File selection cancelled!!!");
                }
            } catch (Exception w){
//                System.out.println(w.getMessage());
                throw new RuntimeException(w.getMessage());
            }
        });

        VBox vbox = new VBox(20, label, btn, selectedFileText);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-padding: 20; -fx-background-color: linear-gradient(to bottom, #3c3b6e, #000);");

        Scene s = new Scene(vbox, 600, 500);

        primaryStage.setScene(s);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
