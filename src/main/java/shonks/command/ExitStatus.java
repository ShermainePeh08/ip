package shonks.command;

/**
 * Tracks whether the application has requested to exit.
 */
public class ExitStatus {

    private boolean exitRequested;

    /**
     * Returns whether exit has been requested.
     *
     * @return True if exit has been requested, false otherwise.
     */
    public boolean isExitRequested() {
        return exitRequested;
    }

    /**
     * Requests that the application exit.
     */
    public void requestExit() {
        this.exitRequested = true;
    }
}
