import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * {@code ls [<path>...]}: lists files and directories. With no arguments, lists
 * the current
 * directory.
 *
 * <p>
 * Files print first (sorted by name), then directories (sorted by name),
 * blank-line separated.
 * A directory's contents get a {@code "
 * 
<dir>
 * :"} header only when more than one argument was given.
 */
public class Ls extends ShellCommand {

    public Ls(String[] args) {
        super(args);
    }

    public static void main(String[] args) {
        ShellCommand.start(Ls.class, args);
    }

    @Override
    protected void runCommand() {
        if (cmdArgs.length == 0) {
            // List & print "." (sorted)
            File currentDir = new File(".");
            String[] entries = currentDir.list();
            if (entries != null) {
                Arrays.sort(entries);
                for (String entry : entries) {
                    System.out.println(entry);
                }
            }
        } else {
            // 1. Report non-existent paths to System.err
            List<String> files = new ArrayList<>();
            List<String> dirs = new ArrayList<>();

            for (String arg : cmdArgs) {
                File f = new File(arg);
                if (!f.exists()) {
                    System.err.println("ls: " + arg + ": No such file or directory");
                }
                // 2. Collect valid into `files` and `dirs`
                else if (f.isFile()) {
                    files.add(arg);
                } else if (f.isDirectory()) {
                    dirs.add(arg);
                }
            }
            // 3. Sort both lists
            if (!files.isEmpty()) {
                Collections.sort(files);
                // 4. Print files
                for (String file : files) {
                    System.out.println(file);
                }
                // 5. Print blank line (if both files and dirs exist)
                if (!files.isEmpty() && !dirs.isEmpty()) {
                    System.out.println();
                }
            }
            // 6. Print dirs (with header if cmdArgs.length > 1), blank lines between dirs
            if (!dirs.isEmpty()) {
                Collections.sort(dirs);
                for (int i = 0; i < dirs.size(); i++) {
                    String dir = dirs.get(i);

                    // Header
                    if (cmdArgs.length > 1) {
                        System.out.println(dir + ":");
                    }

                    // Print directory contents (sorted):
                    File d = new File(dir);
                    String[] entries = d.list();
                    if (entries != null) {
                        Arrays.sort(entries);
                        for (String entry : entries) {
                            System.out.println(entry);
                        }
                    }

                    // Blank line between directories
                    if (i < dirs.size() - 1) {
                        System.out.println();
                    }
                }
            }

        }
    }

}
