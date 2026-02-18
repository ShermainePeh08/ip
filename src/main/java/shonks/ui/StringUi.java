package shonks.ui;

import shonks.command.ExitStatus;

/**
 * UI implementation that captures output into a {@link StringBuilder}.
 * Intended for GUI integration via {@code getResponse}.
 */
public class StringUi extends Ui {

    private final StringBuilder output;
    private final ExitStatus exitStatus;

    /**
     * Constructs a StringUi that writes UI outputs into a buffer.
     *
     * @param output The output buffer.
     * @param exitStatus Exit status tracker.
     */
    public StringUi(StringBuilder output, ExitStatus exitStatus) {
        this.output = output;
        this.exitStatus = exitStatus;
    }

    @Override
    public void showBye() {
        super.showBye();
        exitStatus.requestExit();
    }

    @Override
    protected void renderLine(String line) {
        output.append(line).append("\n");
    }
}
