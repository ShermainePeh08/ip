package shonks.ui;

import shonks.command.ExitStatus;

/**
 * UI implementation that captures output into a {@link StringBuilder}.
 * Intended for GUI integration via {@code getResponse}.
 */
public class StringUi extends Ui {

    /**
     * Special marker prefix that the JavaFX GUI will detect and convert to a real chart node.
     */
    public static final String PIE_MARKER_PREFIX = "[[PIE|";

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
    public void showPieChart(String title, int todo, int deadline, int event) {
        // Format: [[PIE|<title>|<todo>|<deadline>|<event>]]
        // Title is kept, but you can ignore it in GUI if you want.
        renderLine(PIE_MARKER_PREFIX + safe(title) + "|" + todo + "|" + deadline + "|" + event + "]]");
    }

    private static String safe(String s) {
        return (s == null) ? "" : s.replace("|", "/").replace("]]", ")");
    }

    @Override
    protected void renderLine(String line) {
        output.append(line).append("\n");
    }
}
