import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * {@code uniq [-c] [<file>]}: collapses runs of <b>adjacent</b> duplicate lines
 * into a single
 * line — non-adjacent duplicates are left alone, same as real {@code uniq}
 * (pair with
 * {@code sort} first to dedupe an entire file). With {@code -c}, each output
 * line is prefixed
 * with the number of times it occurred in its run.
 *
 * <p>
 * Unlike the other commands, {@code uniq} takes at most <b>one</b> file
 * argument — that's a
 * real constraint of the actual Unix command, not a simplification for this
 * project.
 */
public class Uniq extends ShellCommand {

    public Uniq(String[] args) {
        super(args);
    }

    public static void main(String[] args) {
        ShellCommand.start(Uniq.class, args);
    }

    @Override
    protected void runCommand() throws IOException {
        boolean countFlag = false;
        ArrayList<String> files = new ArrayList<>();
        for (String arg : cmdArgs) {
            if (arg.equals("-c")) {
                countFlag = true;
            } else if (arg.startsWith("-")) {
                System.err.println("Usage: uniq [-c] [<file>]");
                return;
            } else {
                files.add(arg);
            }
        }
        if (files.size() > 1) {
            System.err.println("Usage: uniq [-c] [<file>]");
            return;
        }
        if (files.isEmpty()) {
            processStream(System.in, countFlag);
        } else {
            try (InputStream input = getFileInput(Path.of(files.get(0)))) {
                processStream(input, countFlag);
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }

    }

    private void processStream(InputStream stream, boolean countFlag) throws IOException {
        String prevLine = null;
        int count = 0;
        String line;

        while ((line = readLineRaw(stream)) != null) {
            if (prevLine == null) {
                // First line seen
                prevLine = line;
                count = 1;
            } else if (line.equals(prevLine)) {
                // Same as previous line: increment run count
                count++;
            } else {
                // Run ended: print the previous line
                if (countFlag) {
                    System.out.print(count + " " + prevLine);
                } else {
                    System.out.print(prevLine);
                }
                // Start tracking the new run
                prevLine = line;
                count = 1;
            }
        }

        // Flush the final run after EOF (if input was not empty)
        if (prevLine != null) {
            if (countFlag) {
                System.out.print(count + " " + prevLine);
            } else {
                System.out.print(prevLine);
            }
        }
    }

}
