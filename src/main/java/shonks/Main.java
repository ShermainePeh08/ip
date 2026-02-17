package shonks;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The main JavaFX entry point for the Shonks application.
 */
public class Main extends Application {

    private ScrollPane scrollPane;
    private VBox dialogContainer;

    private final Shonks shonks = new Shonks();

    @Override
    public void start(Stage stage) {
        BorderPane root = createRootLayout();

        Scene scene = new Scene(root, 420, 640);

        stage.setScene(scene);
        stage.setTitle("Shonks");
        stage.setMinWidth(380);
        stage.setMinHeight(560);
        stage.show();
    }

    private BorderPane createRootLayout() {
        BorderPane root = new BorderPane();
        root.setTop(createHeader());
        root.setCenter(createChatAreaWithBackground());
        root.setBottom(new InputBar(dialogContainer, shonks));
        return root;
    }

    private Node createHeader() {
        ImageView logo = new ImageView(MainWindowImages.LOGO);
        logo.setFitWidth(22);
        logo.setFitHeight(22);
        logo.setPreserveRatio(true);

        Text title = new Text("shonks");
        title.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-fill: #0f172a;"
        );

        HBox header = new HBox(8, logo, title);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: white;");

        return header;
    }

    private Node createChatAreaWithBackground() {

        dialogContainer = new VBox();
        dialogContainer.setPadding(new Insets(15));
        dialogContainer.setSpacing(12);
        dialogContainer.setStyle("-fx-background-color: transparent;");

        scrollPane = new ScrollPane(dialogContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-insets: 0;" +
                "-fx-padding: 0;"
        );

        scrollPane.viewportBoundsProperty().addListener((obs, oldV, newV) -> {
                Node viewport = scrollPane.lookup(".viewport");
                if (viewport != null) {
                viewport.setStyle("-fx-background-color: transparent;");
                }
        });

        dialogContainer.heightProperty().addListener(
                (obs, oldV, newV) -> scrollPane.setVvalue(1.0)
        );

        addInitialBotMessage();

        StackPane wrapper = new StackPane(scrollPane);
        wrapper.setStyle(
                "-fx-background-image: url('/images/bg.jpg');" +
                "-fx-background-size: cover;" +
                "-fx-background-position: center center;" 
        );

        return wrapper;
        }

    private void addInitialBotMessage() {
        String greeting =
            "Hello! I'm Shonks 👋\nHow can I help you today?";

        dialogContainer.getChildren().add(
            DialogBox.getShonksDialog(
                greeting,
                MainWindowImages.SHONKS
            )
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}
