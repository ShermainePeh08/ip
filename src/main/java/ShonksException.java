/**
 * Represents an exception specific to the Shonks chatbot.
 * <p>
 * Used to indicate invalid user commands or missing information.
 */
public class ShonksException extends Exception {
    /**
     * Creates a ShonksException with the specified error message.
     *
     * @param message Description of the error.
     */
    public ShonksException(String message) {
        super(message);
    }
}
