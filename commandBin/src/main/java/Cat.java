import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * {@code cat}: prints the contents of one or more files to standard out, in
 * order.
 *
 * <p>
 * On a missing file or directory, print the error and keep going with the rest
 * —
 * {@link #getFileInput} already throws a ready-to-print
 * {@link IllegalArgumentException}.
 */
public class Cat extends ShellCommand {

    public Cat(String[] args) {
        super(args);
    }

    public static void main(String[] args) throws Exception {
        ShellCommand.start(Cat.class, args);
    }

    @Override
    protected void runCommand() throws IOException {
        // TODO: implement Cat.runCommand
        // throw new UnsupportedOperationException("TODO: implement Cat.runCommand");
        // prints the contents of all files passed as arguments, in the order they were
        // passed.
        if (cmdArgs.length != 0) {
            for (String arg : cmdArgs) {
                try (InputStream input = getFileInput(Path.of(arg))) {
                    String line;
                    while ((line = readLineRaw(input)) != null) {
                        System.out.print(line);
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
        // If no arguments were passed, it should read from standard input (System.in)
        // and output the same.
        else {
            String line;
            // readLine() returns null when the end of the file is reached
            while ((line = readLineRaw(System.in)) != null) {
                System.out.print(line);
            }
        }

    }
}