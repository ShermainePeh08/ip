package shonks;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The main JavaFX application for the Shonks chatbot.
 * <p>
 * This class is responsible for setting up the graphical user interface (GUI)
 * and handling user interactions such as sending input and displaying responses.
 * The chatbot logic itself is delegated to the {@link Shonks} class.
 */
public class Main extends Application {

    private ScrollPane scrollPane;

    private VBox dialogContainer;

    private TextField userInput;

    private Button sendButton;

    private AnchorPane mainLayout;

    private final Shonks shonks = new Shonks("./data/shonks.txt");

    /**
     * Initializes and displays the primary stage of the JavaFX application.
     * <p>
     * This method sets up the layout, configures UI components, and registers
     * event handlers for user input.
     *
     * @param stage The primary stage provided by the JavaFX runtime.
     */
    @Override
    public void start(Stage stage) {
        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        userInput = new TextField();
        sendButton = new Button("Send");
        mainLayout = new AnchorPane();

        scrollPane.setContent(dialogContainer);
        scrollPane.setFitToWidth(true);

        dialogContainer.setPadding(new Insets(10));
        dialogContainer.setSpacing(10);

        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 50.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);

        AnchorPane.setBottomAnchor(userInput, 0.0);
        AnchorPane.setLeftAnchor(userInput, 0.0);
        AnchorPane.setRightAnchor(sendButton, 0.0);

        AnchorPane.setBottomAnchor(sendButton, 0.0);

        userInput.setPrefHeight(50);
        sendButton.setPrefHeight(50);

        userInput.prefWidthProperty()
                .bind(mainLayout.widthProperty().subtract(sendButton.widthProperty()));

        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        dialogContainer.getChildren().add(
                DialogBox.getUserDialog("hello", MainWindowImages.USER));
        dialogContainer.getChildren().add(
                DialogBox.getDukeDialog("hi i'm shonks", MainWindowImages.DUKE));

        sendButton.setOnAction(e -> handleUserInput());
        userInput.setOnAction(e -> handleUserInput());

        dialogContainer.heightProperty().addListener(
                (obs, oldV, newV) -> scrollPane.setVvalue(1.0));

        Scene scene = new Scene(mainLayout);
        stage.setScene(scene);
        stage.setTitle("Shonks");
        stage.setMinWidth(400);
        stage.setMinHeight(600);
        stage.show();
    }

    /**
     * Handles user input from the text field.
     * <p>
     * This method retrieves the user's input, displays it in the dialog container,
     * obtains the chatbot's response, and displays the response accordingly.
     */
    private void handleUserInput() {
        String input = userInput.getText();
        if (input == null || input.isBlank()) {
            return;
        }

        dialogContainer.getChildren().add(
                DialogBox.getUserDialog(input, MainWindowImages.USER));

        String response = shonks.getResponse(input);
        dialogContainer.getChildren().add(
                DialogBox.getDukeDialog(response, MainWindowImages.DUKE));

        userInput.clear();
    }
}
