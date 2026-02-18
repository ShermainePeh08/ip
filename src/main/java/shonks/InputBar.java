package shonks;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import shonks.ui.StringUi;

/**
 * Input bar component for user message entry.
 */
public class InputBar extends HBox {

    private final TextField userInput;
    private final Button sendButton;

    /**
     * Constructs an input bar linked to the dialog container and Shonks backend.
     *
     * @param dialogContainer The container displaying chat messages.
     * @param shonks The backend instance.
     */
    public InputBar(VBox dialogContainer, Shonks shonks) {

        this.userInput = new TextField();
        this.sendButton = new Button("Send");

        userInput.setPromptText("Type a message...");
        userInput.setStyle(
            "-fx-background-radius: 14;" +
            "-fx-padding: 10;"
        );

        sendButton.setStyle(
            "-fx-background-radius: 14;" +
            "-fx-background-color: #2563eb;" +
            "-fx-text-fill: white;"
        );

        HBox.setHgrow(userInput, Priority.ALWAYS);

        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(12));
        this.setStyle(
            "-fx-background-color: rgba(255,255,255,0.85);"
        );

        this.getChildren().addAll(userInput, sendButton);

        sendButton.setOnAction(e -> handleInput(dialogContainer, shonks));
        userInput.setOnAction(e -> handleInput(dialogContainer, shonks));
    }

    private void handleInput(VBox dialogContainer, Shonks shonks) {

        String input = userInput.getText();

        if (input == null || input.isBlank()) {
            return;
        }

        dialogContainer.getChildren().add(
            DialogBox.getUserDialog(input, MainWindowImages.USER)
        );

        String response = shonks.getResponse(input);
        renderResponse(dialogContainer, response);

        userInput.clear();
    }

    private void renderResponse(VBox dialogContainer, String response) {
        if (response == null || response.isBlank()) {
            return;
        }

        String[] lines = response.split("\\R");
        StringBuilder textPart = new StringBuilder();

        for (String line : lines) {
            if (line.startsWith(StringUi.PIE_MARKER_PREFIX) && line.endsWith("]]")) {
                // flush any accumulated text before showing chart
                flushTextIfAny(dialogContainer, textPart);

                PieChart chart = parsePieChart(line);
                if (chart != null) {
                    dialogContainer.getChildren().add(
                        DialogBox.getShonksNodeDialog(chart, MainWindowImages.SHONKS)
                    );
                }
            } else {
                textPart.append(line).append("\n");
            }
        }

        flushTextIfAny(dialogContainer, textPart);
    }

    private void flushTextIfAny(VBox dialogContainer, StringBuilder textPart) {
        String text = textPart.toString().trim();
        if (!text.isBlank()) {
            dialogContainer.getChildren().add(
                DialogBox.getShonksDialog(text, MainWindowImages.SHONKS)
            );
        }
        textPart.setLength(0);
    }

    /**
     * Marker format: [[PIE|<title>|<todo>|<deadline>|<event>]]
     */
    private PieChart parsePieChart(String markerLine) {
        try {
            String inner = markerLine.substring(0, markerLine.length() - 2); // remove trailing ]]
            inner = inner.substring(StringUi.PIE_MARKER_PREFIX.length());     // remove prefix

            String[] parts = inner.split("\\|");
            if (parts.length != 4) {
                return null;
            }

            String title = parts[0];
            int todo = Integer.parseInt(parts[1]);
            int deadline = Integer.parseInt(parts[2]);
            int event = Integer.parseInt(parts[3]);

            PieChart chart = new PieChart(FXCollections.observableArrayList(
                    new PieChart.Data("Todo", todo),
                    new PieChart.Data("Deadline", deadline),
                    new PieChart.Data("Event", event)
            ));

            chart.setTitle(title);
            chart.setLabelsVisible(true);
            chart.setLegendVisible(true);

            // optional sizing so it fits nicely in your bubble
            chart.setPrefWidth(260);
            chart.setPrefHeight(220);

            return chart;
        } catch (Exception e) {
            return null;
        }
    }
}
