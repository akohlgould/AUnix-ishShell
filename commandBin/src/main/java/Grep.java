import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * {@code grep <pattern> [<file>...]}: prints every line, from the given files
 * or from standard
 * input, that matches the {@link java.util.regex.Pattern regular expression}
 * {@code pattern}.
 *
 * <p>
 * With more than one file, each matching line is prefixed with
 * {@code "<filename>:"}.
 */
public class Grep extends ShellCommand {

    public Grep(String[] args) {
        super(args);
    }

    public static void main(String[] args) {
        ShellCommand.start(Grep.class, args);
    }

    @Override
    protected void runCommand() throws Exception {
        // TODO: implement Grep.runCommand
        if (cmdArgs.length == 0) {
            // error message
            System.err.println("Usage: grep <pattern> [<file>...]");
            return;

        }
        // save pattern from first arg
        Pattern pattern = Pattern.compile(cmdArgs[0]);
        Matcher matcher;

        // reading from standard input
        if (cmdArgs.length == 1) {
            String line;
            try {
                while ((line = readLineRaw(System.in)) != null) {
                    matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        System.out.print(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (cmdArgs.length > 2) {
            for (int i = 1; i < cmdArgs.length; i++) {
                String arg = cmdArgs[i];
                try (InputStream input = getFileInput(Path.of(arg))) {
                    String line;
                    while ((line = readLineRaw(input)) != null) {
                        matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            System.out.print(arg + ":" + line);
                        }
                    }

                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }
        } else if (cmdArgs.length > 1) {
            for (int i = 1; i < cmdArgs.length; i++) {
                String arg = cmdArgs[i];
                try (InputStream input = getFileInput(Path.of(arg))) {
                    String line;
                    while ((line = readLineRaw(input)) != null) {
                        matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            System.out.print(line);
                        }
                    }

                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }
}
