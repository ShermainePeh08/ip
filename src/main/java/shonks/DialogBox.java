package shonks;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * Represents a dialog box consisting of an image and a text label.
 * <p>
 * A {@code DialogBox} is used in the GUI to display messages from either
 * the user or the Shonks chatbot. User dialog boxes are aligned to the right,
 * while Shonks dialog boxes are aligned to the left.
 */
public class DialogBox extends HBox {
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
        return new DialogBox(s, img);
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
        return db;
    }
}
