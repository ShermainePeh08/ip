package shonks;

import java.net.URL;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The main JavaFX entry point for the Shonks application.
 * Sets up the main window layout and wires user input to the chatbot response flow.
 */
public class Main extends Application {

    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;

    private final Shonks shonks = new Shonks();

    /**
     * Starts the JavaFX application.
     *
     * @param stage The primary stage provided by JavaFX.
     */
    @Override
    public void start(Stage stage) {
        BorderPane root = createRootLayout();
        Scene scene = createScene(root);

        stage.setScene(scene);
        stage.setTitle("Shonks");
        stage.setMinWidth(380);
        stage.setMinHeight(560);
        stage.show();
    }

    /**
     * Creates the root layout for the main window.
     *
     * @return The root {@code BorderPane}.
     */
    private BorderPane createRootLayout() {
        BorderPane root = new BorderPane();
        root.setTop(createHeader());
        root.setCenter(createChatArea());
        root.setBottom(createInputBar());
        root.getStyleClass().add("root");
        return root;
    }

    /**
     * Creates the scene for the application window and attaches stylesheets when available.
     *
     * @param root The root node of the scene graph.
     * @return The created {@code Scene}.
     */
    private Scene createScene(BorderPane root) {
        Scene scene = new Scene(root, 420, 640);
        addStylesheetIfPresent(scene, "/css/main.css");
        return scene;
    }

    /**
     * Adds a stylesheet to the scene if it exists on the classpath.
     *
     * @param scene The scene to add the stylesheet to.
     * @param resourcePath The classpath resource path of the stylesheet.
     */
    private void addStylesheetIfPresent(Scene scene, String resourcePath) {
        URL url = Main.class.getResource(resourcePath);
        if (url != null) {
            scene.getStylesheets().add(url.toExternalForm());
        }
    }

    /**
     * Creates the fixed header component.
     *
     * @return The header node.
     */
    private Node createHeader() {
        Text title = new Text("Shonks");
        title.getStyleClass().add("header-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(10, title, spacer);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(12, 14, 12, 14));
        header.getStyleClass().add("header");
        return header;
    }

    /**
     * Creates the chat area component containing the scrollable dialog container.
     *
     * @return The chat area node.
     */
    private Node createChatArea() {
        dialogContainer = new VBox();
        dialogContainer.setPadding(new Insets(12));
        dialogContainer.setSpacing(10);
        dialogContainer.getStyleClass().add("dialog-container");

        scrollPane = new ScrollPane(dialogContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("chat-scroll");

        dialogContainer.heightProperty().addListener((obs, oldV, newV) -> scrollPane.setVvalue(1.0));

        return scrollPane;
    }

    /**
     * Creates the input bar component containing the user input field and send button.
     *
     * @return The input bar node.
     */
    private Node createInputBar() {
        userInput = new TextField();
        userInput.getStyleClass().add("input-field");

        sendButton = new Button("Send");
        sendButton.getStyleClass().add("send-button");

        HBox.setHgrow(userInput, Priority.ALWAYS);

        HBox inputBar = new HBox(10, userInput, sendButton);
        inputBar.setAlignment(Pos.CENTER);
        inputBar.setPadding(new Insets(10, 12, 12, 12));
        inputBar.getStyleClass().add("input-bar");

        sendButton.setOnAction(e -> handleUserInput());
        userInput.setOnAction(e -> handleUserInput());

        return inputBar;
    }

    /**
     * Handles user input: displays the user message, obtains the chatbot response, and displays it.
     */
    private void handleUserInput() {
        String input = userInput.getText();
        if (input == null || input.isBlank()) {
            return;
        }

        dialogContainer.getChildren().add(DialogBox.getUserDialog(input, MainWindowImages.USER));

        String response = shonks.getResponse(input);
        dialogContainer.getChildren().add(DialogBox.getShonksDialog(response, MainWindowImages.SHONKS));

        userInput.clear();
    }
}
