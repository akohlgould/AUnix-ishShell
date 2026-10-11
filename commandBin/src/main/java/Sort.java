import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

/**
 * {@code sort [-r] [<file>...]}: reads every line from all given files (or
 * standard input, if
 * none given), concatenated in argument order, and prints them sorted
 * lexicographically —
 * ascending by default, descending with {@code -r}.
 *
 * <p>
 * Unlike {@code head}/{@code tail}, files are merged into one sorted stream
 * rather than
 * processed (and headed) independently — that's how real {@code sort} behaves
 * too.
 */
public class Sort extends ShellCommand {

    public Sort(String[] args) {
        super(args);
    }

    public static void main(String[] args) {
        ShellCommand.start(Sort.class, args);
    }

    @Override
    protected void runCommand() throws IOException {

        // TODO: implement Sort.runCommand
        // throw new UnsupportedOperationException("TODO: implement Sort.runCommand");
        boolean reverse = false;
        ArrayList<String> files = new ArrayList<>();
        for (String arg : cmdArgs) {
            if (arg.equals("-r")) {
                reverse = true;
            } else {
                files.add(arg);
            }
        }
        ArrayList<String> lines = new ArrayList<>();
        if (files.isEmpty()) {
            // Standard input
            String line;
            while ((line = readLineRaw(System.in)) != null) {
                lines.add(line);
            }
        } else {
            // Files (plural)
            for (String file : files) {
                try (InputStream input = getFileInput(Path.of(file))) {
                    String line;
                    while ((line = readLineRaw(input)) != null) {
                        lines.add(line);
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }
        }

        if (reverse) {
            Collections.sort(lines, Collections.reverseOrder());

        } else {
            Collections.sort(lines);
        }
        for (String line : lines) {
            System.out.print(line); // readLineRaw already kept the line breaks!
        }

    }
}
