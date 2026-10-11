import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code tail [-n <count>] [<file>...]}: prints the last {@code count} lines
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
public class Tail extends ShellCommand {
    private static final int DEFAULT_COUNT = 10;

    public Tail(String[] args) {
        super(args);
    }

    public static void main(String[] args) {
        ShellCommand.start(Tail.class, args);
    }

    @Override
    protected void runCommand() throws IOException {
        int count = DEFAULT_COUNT;
        ArrayList<String> files = new ArrayList<>();
        for (int i = 0; i < cmdArgs.length; i++) {
            String arg = cmdArgs[i];
            if (arg.equals("-n")) {
                if (i + 1 >= cmdArgs.length) {
                    // error
                    System.err.println("Usage: tail [-n <count>] [<file>...]");
                    return;
                }
                try {
                    count = Integer.parseInt(cmdArgs[++i]);
                    if (count < 0) {
                        // error
                        System.err.println("Usage: tail [-n <count>] [<file>...]");
                        return;
                    }
                } catch (NumberFormatException e) {
                    // if the numbers not a number
                    System.err.println("Usage: tail [-n <count>] [<file>...]");
                    return;
                }

            } else {
                files.add(arg);
            }
        }

        // Standard input
        if (files.isEmpty())

        {
            ArrayList<String> allLines = new ArrayList<>();
            String line;
            while ((line = readLineRaw(System.in)) != null) {
                allLines.add(line);
            }
            int startIndex = Math.max(0, allLines.size() - count);
            for (int i = startIndex; i < allLines.size(); i++) {
                System.out.print(allLines.get(i));
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

                ArrayList<String> allLines = new ArrayList<>();
                String line;
                while ((line = readLineRaw(input)) != null) {
                    allLines.add(line);
                }
                int startIndex = Math.max(0, allLines.size() - count);
                for (int i = startIndex; i < allLines.size(); i++) {
                    System.out.print(allLines.get(i));
                }
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }

        }

    }
}
