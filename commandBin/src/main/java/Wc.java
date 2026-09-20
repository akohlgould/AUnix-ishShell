import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * {@code wc [<file>...]}: prints newline, word, and byte counts for each file,
 * and a total line
 * if more than one file is specified. A word is a non-zero-length sequence of
 * characters
 * delimited by white space.
 */
public class Wc extends ShellCommand {
    /**
     * Format specifier for printing output counts. Do not change.
     */
    public static final String formatSpecifier = "%8d %8d %8d %s%n";

    public Wc(String[] args) {
        super(args);
    }

    public static void main(String[] args) {
        ShellCommand.start(Wc.class, args);
    }

    @Override
    protected void runCommand() throws IOException {
        // TODO: implement Wc.runCommand
        String line;
        long wordCount = 0;
        long lineCount = 0;
        long charCount = 0;
        long totalWordCount = 0;
        long totalLineCount = 0;
        long totalCharCount = 0;
        if (cmdArgs.length == 0) {
            try {
                while ((line = readLineRaw(System.in)) != null) {
                    lineCount++;
                    charCount += line.length();
                    if (!line.trim().isEmpty()) {
                        wordCount += line.trim().split("\\s+").length;
                    }

                }
                System.out.printf("%8d %8d %8d %s%n", lineCount, wordCount, charCount, "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (cmdArgs.length == 1) {
            String arg = cmdArgs[0];
            try (InputStream input = getFileInput(Path.of(arg))) {
                while ((line = readLineRaw(input)) != null) {
                    lineCount++;
                    charCount += line.length();
                    if (!line.trim().isEmpty()) {
                        wordCount += line.trim().split("\\s+").length;
                    }
                }
                System.out.printf("%8d %8d %8d %s%n", lineCount, wordCount, charCount, arg);
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
                if (e.getMessage().endsWith("Is a directory")) {
                    System.out.printf("%8d %8d %8d %s%n", 0, 0, 0, arg);
                }
            }

        } else if (cmdArgs.length > 1) {
            for (int i = 0; i < cmdArgs.length; i++) {
                wordCount = 0;
                lineCount = 0;
                charCount = 0;
                String arg = cmdArgs[i];
                try (InputStream input = getFileInput(Path.of(arg))) {
                    while ((line = readLineRaw(input)) != null) {
                        lineCount++;
                        charCount += line.length();
                        if (!line.trim().isEmpty()) {
                            wordCount += line.trim().split("\\s+").length;
                        }
                    }
                    System.out.printf("%8d %8d %8d %s%n", lineCount, wordCount, charCount, arg);
                    totalLineCount += lineCount;
                    totalWordCount += wordCount;
                    totalCharCount += charCount;
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                    if (e.getMessage().endsWith("Is a directory")) {
                        System.out.printf("%8d %8d %8d %s%n", 0, 0, 0, arg);
                    }
                }

            }
            System.out.printf("%8d %8d %8d %s%n", totalLineCount, totalWordCount, totalCharCount, "total");
        }

    }
}