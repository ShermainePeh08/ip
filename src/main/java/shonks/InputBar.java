package shonks;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

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

        dialogContainer.getChildren().add(
            DialogBox.getShonksDialog(response, MainWindowImages.SHONKS)
        );

        userInput.clear();
    }
}
