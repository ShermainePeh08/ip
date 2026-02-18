package shonks;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Represents a dialog box consisting of an image and a text label.
 * <p>
 * A {@code DialogBox} is used in the GUI to display messages from either
 * the user or the Shonks chatbot. User dialog boxes are aligned to the right,
 * while Shonks dialog boxes are aligned to the left.
 */
public class DialogBox extends HBox {
    private static final String USER_BUBBLE_STYLE =
            "-fx-background-color: rgba(0, 126, 73, 0.4);"
            + "-fx-text-fill: white;"
            + "-fx-padding: 10 12 10 12;"
            + "-fx-background-radius: 14;"
            + "-fx-border-radius: 14;";

    private static final String BOT_BUBBLE_STYLE =
            "-fx-background-color: rgba(89, 143, 96, 0.4);"
            + "-fx-text-fill: #222;"
            + "-fx-padding: 10 12 10 12;"
            + "-fx-background-radius: 14;"
            + "-fx-border-radius: 14;";

    private static final String BOT_CHART_STYLE =
            "-fx-background-color: rgba(89, 143, 96, 0.4);"
            + "-fx-padding: 10 12 10 12;"
            + "-fx-background-radius: 14;"
            + "-fx-border-radius: 14;";

    private final Label text;
    private final ImageView displayPicture;

    /**
     * Constructs a dialog box with the given text and image.
     * <p>
     * The dialog box is right-aligned by default, which is suitable for
     * displaying user messages.
     *
     * @param s   The message text to be displayed.
     * @param img The image associated with the dialog (e.g. user or bot avatar).
     */
    private DialogBox(String s, Image img) {
        text = new Label(s);
        displayPicture = new ImageView(img);

        text.setWrapText(true);
        text.setMaxWidth(250);

        displayPicture.setFitWidth(50);
        displayPicture.setFitHeight(50);
        displayPicture.setPreserveRatio(true);

        this.setAlignment(Pos.TOP_RIGHT);
        this.setSpacing(10);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        this.getChildren().addAll(spacer, text, displayPicture);
    }

    /**
     * Applies the bubble styling for user messages.
     */
    private void styleAsUser() {
        text.setStyle(USER_BUBBLE_STYLE);
    }

    /**
     * Applies the bubble styling for Shonks messages.
     */
    private void styleAsBot() {
        text.setStyle(BOT_BUBBLE_STYLE);
    }

    /**
     * Flips the dialog box such that the image appears on the left
     * and the text appears on the right.
     * <p>
     * This method is used to format dialog boxes for Shonks messages,
     * distinguishing them visually from user messages.
     */
    private void flip() {
        this.setAlignment(Pos.TOP_LEFT);
        this.getChildren().clear();

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        this.getChildren().addAll(displayPicture, text, spacer);
    }

    /**
     * Creates a dialog box representing a user message.
     *
     * @param s   The message text entered by the user.
     * @param img The image representing the user.
     * @return A {@code DialogBox} aligned to the right containing the user's message.
     */
    public static DialogBox getUserDialog(String s, Image img) {
        DialogBox db = new DialogBox(s, img);
        db.styleAsUser();
        return db;
    }

    /**
     * Creates a dialog box representing a message from Shonks.
     * <p>
     * The dialog box is flipped so that it is left-aligned.
     *
     * @param s   The message text returned by Shonks.
     * @param img The image representing Shonks.
     * @return A {@code DialogBox} aligned to the left containing Shonks' message.
     */
    public static DialogBox getShonksDialog(String s, Image img) {
        DialogBox db = new DialogBox(s, img);
        db.flip();
        db.styleAsBot();
        return db;
    }

    /**
     * Creates a "Shonks" dialog bubble that contains a JavaFX node (e.g., a PieChart).
     *
     * @param node content node to show (chart, etc.)
     * @param img  Shonks avatar image
     * @return an HBox styled like a bot bubble
     */
    public static HBox getShonksNodeDialog(Node node, Image img) {
        ImageView displayPicture = new ImageView(img);
        displayPicture.setFitWidth(50);
        displayPicture.setFitHeight(50);
        displayPicture.setPreserveRatio(true);

        VBox bubble = new VBox(node);
        bubble.setPadding(new Insets(0));
        bubble.setStyle(BOT_CHART_STYLE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox box = new HBox(10, displayPicture, bubble, spacer);
        box.setAlignment(Pos.TOP_LEFT);
        return box;
    }
}
