import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * {@code head [-n <count>] [<file>...]}: prints the first {@code count} lines
 * (default {@code 10})
 * of each file.
 *
 * <p>
 * With more than one file, each file's output is preceded by an
 * {@code "==> <file> <=="}
 * header line, with a blank line separating consecutive files' output (no
 * header for a single
 * file or standard input).
 */
public class Head extends ShellCommand {
    private static final int DEFAULT_COUNT = 10;

    public Head(String[] args) {
        super(args);
    }

    public static void main(String[] args) {
        ShellCommand.start(Head.class, args);
    }

    @Override
    protected void runCommand() throws IOException {
        // TODO: implement Head.runCommand
        // throw new UnsupportedOperationException("TODO: implement Head.runCommand");
        // parse through the args
        int count = DEFAULT_COUNT;
        ArrayList<String> files = new ArrayList<>();
        for (int i = 0; i < cmdArgs.length; i++) {
            String arg = cmdArgs[i];
            if (arg.equals("-n")) {
                if (i + 1 >= cmdArgs.length) {
                    // error
                    System.err.println("Usage: head [-n <count>] [<file>...]");
                    return;
                }
                try {
                    count = Integer.parseInt(cmdArgs[++i]);
                    if (count < 0) {
                        // error
                        System.err.println("Usage: head [-n <count>] [<file>...]");
                        return;
                    }
                } catch (NumberFormatException e) {
                    // if the numbers not a number
                    System.err.println("Usage: head [-n <count>] [<file>...]");
                    return;
                }

            } else {
                files.add(arg);
            }
        }

        // Standard input
        if (files.isEmpty())

        {
            int linesRead = 0;
            String line;
            while (linesRead < count && (line = readLineRaw(System.in)) != null) {
                System.out.print(line);
                linesRead++;
            }
            return;
        }
        // FILES (plural)
        // only print headers when multiple files
        boolean showHeaders = files.size() > 1;
        // blank line separation
        boolean printedAnyFile = false;

        for (String file : files) {
            try (InputStream input = getFileInput(Path.of(file))) {
                if (showHeaders) {
                    if (printedAnyFile) {
                        // not first
                        System.out.printf("%n==> %s <==%n", file);
                    } else {
                        // first file (no leading newline)
                        System.out.printf("==> %s <==%n", file);
                        printedAnyFile = true;
                    }
                }

                int linesRead = 0;
                String line;
                while (linesRead < count && (line = readLineRaw(input)) != null) {
                    System.out.print(line);
                    linesRead++;
                }
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }

        }
    }
}