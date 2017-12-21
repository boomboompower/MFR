package me.boomboompower.fnr;

import java.io.File;

/**
 * A Java Application designed to mass replace
 * class names in all subdirectories
 *
 * **NOTE**
 * This will not replace the contents of files!
 *
 * @author boomboompower
 * @created 21/12/2017
 * @version 1.0
 */
public class Main {

    private static File currentDir; // The programs running directory

    private static final String TO_REPLACE = "Skin"; // Put the part of the filename you wish to replace
    private static final String REPLACE_TO = "Cape"; // Replace "To Replace" with this.

    private static final String DIR_SCANNING = "src"; // Which folder should we scan in the running directory?
    private static final String FILE_EXTENSION = ".java"; // File extension we are scanning

    private static final Boolean EXIT_ON_ERROR = true; // Should we exit the application if an error occurs?

    private static Integer successes; // Amount of successful refactors
    private static Integer failures; // Amount of unsuccessful refactors

    @SuppressWarnings("ConstantConditions") // We already look out for this error
    public static void main(String[] args) {
        File current = currentDir = new File("."); // Grab the programs running directory
        Boolean stopped = false; // Set to true if there is an issue with the current directory

        successes = 0; // Set the success count to 0
        failures = 0;  // Set the failure count to 0

        try {
            if (current.isFile() || (current.isDirectory() && (current.listFiles() == null || current.listFiles().length == 0))) {
                // Exit the application if one of the following conditions are met:
                //  - The current running dir is a file (This should NEVER, EVER, EVER, EVER happen)
                //  - The current running dir is a directory and either
                //     - There are no files in this directory
                //     - This directories files' are null

                stopped = true; // Set force stop variable to true, and stop it running.
            }

            // If the program hasn't been force stopped, begin looping
            if (!stopped) {

                // Begin looping through all files in the current directory
                for (File allInCurrentDir : current.listFiles()) {

                    // If the file we are looping into is a directory and its name matches the one
                    // we want to scan, begin looping through all the files in that directory
                    if (allInCurrentDir.isDirectory() && allInCurrentDir.getName().equalsIgnoreCase(DIR_SCANNING)) {
                        beginLoop(allInCurrentDir); // LOOOOOOOOP
                    }
                }
            }
        } catch (Exception ex) {
            // An issue has occured, we'll log the stacktrace
            // and if exiting on error is enabled, we'll exit
            ex.printStackTrace();
            System.err.println("An error occured whilst refactoring!");

            // Quit the application if we "exit on error" flag is true
            if (EXIT_ON_ERROR) {

                // Because we are quitting, we'll display the appropriate exiting message
                System.out.println("");
                System.out.println(String.format("Program exited with %s success(es), and %s failure(s)!", successes, failures));
                System.out.println("");

                System.gc(); // Begin running the system garbage collector
                System.exit(-1); // Exit the application with an irregular status
                return;
            }
        }

        // Because we are quitting, we'll display the appropriate exiting message
        System.out.println("");
        System.out.println(String.format("Program exited with %s success(es), and %s failure(s)!", successes, failures));
        System.out.println("");

        System.gc(); // Begin running the system garbage collector
        System.exit(0); // Exit the application with a normal exit status
    }

    /**
     * Loop through every directory and
     *      rename every file with the replacement
     *
     * @param inputFile input directory we want to loop through
     */
    @SuppressWarnings("ConstantConditions") // We already catch it!
    private static void beginLoop(File inputFile) {
        try {
            if (inputFile.isDirectory() && (inputFile.listFiles() == null || inputFile.listFiles().length == 0)) {
                // Skip looping if there is nothing in this directory or if the files may be null
                return;
            }

            // Begin looping through all the files in the input directory
            for (File file : inputFile.listFiles()) {
                // If the file we are looping into is a directory,
                // We'll begin scanning it for the file names
                if (file.isDirectory()) {
                    for (File file2 : file.listFiles()) {
                        // Begin looping through the found directory
                        beginLoop(file);
                    }
                } else {
                    // The target file is not a directory, check if the extension is what we are looking
                    // for, and that it cotains what we want to replace. If it meets these conditions
                    // continue on and begin refactoring the file name!
                    if (file.getName().endsWith(FILE_EXTENSION) && file.getName().contains(TO_REPLACE)) {

                        String oldDir = file.getAbsolutePath(); // The old directory of the file (Before moving it)
                        File newFile = new File(file.getParentFile(), file.getName().replace(TO_REPLACE, REPLACE_TO)); // The new location for the file

                        // This will return true if the file moving was a success,
                        // If the file rename failed, we'll log it as an error!
                        if (file.renameTo(newFile)) {
                            System.out.println("[INFO] Renamed: " + oldDir.replace(currentDir.getAbsolutePath(), "") + " > " + newFile.getAbsolutePath().replace(currentDir.getAbsolutePath(), ""));
                            successes += 1; // Increment the success count
                        } else {
                            System.err.println("[ERROR] Renamed: " + oldDir.replace(currentDir.getAbsolutePath(), "") + " > " + newFile.getAbsolutePath().replace(currentDir.getAbsolutePath(), ""));
                            failures += 1; // Increment the failure count
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("[ERROR] An error occured whilst looping file: \"" + inputFile.getAbsolutePath().replace(currentDir.getAbsolutePath(), "") + "\"");
            failures += 1; // Increment the failure count because an issue occured.
        }
    }
}
