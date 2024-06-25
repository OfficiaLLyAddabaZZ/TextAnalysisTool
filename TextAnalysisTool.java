package com.example.textanalysistool;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TextAnalysisTool extends Application {

    private TextArea inputTextArea;
    private Label charCountLabel;
    private Label wordCountLabel;
    private Label commonCharLabel;
    private TextField charFrequencyField;
    private Label charFrequencyLabel;
    private TextField wordFrequencyField;
    private Label wordFrequencyLabel;
    private Label uniqueWordsLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Text Analysis Tool");

        inputTextArea = new TextArea();
        inputTextArea.setWrapText(true); // Enable word wrapping
        inputTextArea.setPromptText("Enter your text here...");

        Button analyzeButton = new Button("Analyze Text");
        analyzeButton.setOnAction(event -> analyzeText());

        charCountLabel = new Label("Character Count: ");
        wordCountLabel = new Label("Word Count: ");
        commonCharLabel = new Label("Most Common Character: ");
        charFrequencyField = new TextField();
        charFrequencyField.setPromptText("Enter character");
        Button charFrequencyButton = new Button("Check Character Frequency");
        charFrequencyButton.setOnAction(event -> checkCharFrequency());
        charFrequencyLabel = new Label("Character Frequency: ");
        wordFrequencyField = new TextField();
        wordFrequencyField.setPromptText("Enter word");
        Button wordFrequencyButton = new Button("Check Word Frequency");
        wordFrequencyButton.setOnAction(event -> checkWordFrequency());
        wordFrequencyLabel = new Label("Word Frequency: ");
        uniqueWordsLabel = new Label("Unique Words Count: ");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(inputTextArea, analyzeButton, charCountLabel, wordCountLabel, commonCharLabel,
                new HBox(10, charFrequencyField, charFrequencyButton), charFrequencyLabel,
                new HBox(10, wordFrequencyField, wordFrequencyButton), wordFrequencyLabel, uniqueWordsLabel);

        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void analyzeText() {
        String text = inputTextArea.getText().trim();
        if (text.isEmpty()) {
            showAlert("Input Error", "Please enter some text to analyze.");
            return;
        }

        charCountLabel.setText("Character Count: " + text.length());
        String[] words = text.split("\\s+");
        wordCountLabel.setText("Word Count: " + words.length);
        commonCharLabel.setText("Most Common Character: " + getMostCommonCharacter(text));
        uniqueWordsLabel.setText("Unique Words Count: " + getUniqueWordCount(words));
    }

    private char getMostCommonCharacter(String text) {
        Map<Character, Integer> charFrequency = new HashMap<>();
        for (char c : text.toCharArray()) {
            c = Character.toLowerCase(c);
            if (Character.isLetterOrDigit(c)) {
                charFrequency.put(c, charFrequency.getOrDefault(c, 0) + 1);
            }
        }
        Optional<Map.Entry<Character, Integer>> mostCommonChar = charFrequency.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());
        return mostCommonChar.map(Map.Entry::getKey).orElse('?');
    }

    private int getUniqueWordCount(String[] words) {
        return (int) java.util.Arrays.stream(words)
                .map(word -> word.replaceAll("[^a-zA-Z0-9]", "").toLowerCase())
                .filter(word -> !word.isEmpty())
                .collect(Collectors.toSet())
                .size();
    }

    private void checkCharFrequency() {
        String text = inputTextArea.getText().toLowerCase();
        String character = charFrequencyField.getText().toLowerCase();
        if (character.length() != 1) {
            charFrequencyLabel.setText("Please enter a single character.");
            return;
        }
        long count = text.chars().filter(ch -> ch == character.charAt(0)).count();
        charFrequencyLabel.setText("Character Frequency: " + count);
    }

    private void checkWordFrequency() {
        String text = inputTextArea.getText().toLowerCase();
        String word = wordFrequencyField.getText().toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
        if (word.isEmpty()) {
            wordFrequencyLabel.setText("Please enter a valid word.");
            return;
        }
        long count = java.util.Arrays.stream(text.split("\\s+"))
                .map(w -> w.replaceAll("[^a-zA-Z0-9]", ""))
                .filter(w -> w.equals(word))
                .count();
        wordFrequencyLabel.setText("Word Frequency: " + count);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
