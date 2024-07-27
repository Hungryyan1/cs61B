package gitlet;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

/**
 *  Unstage the file if it is currently staged for addition.
 *  If the file is tracked in the current commit,
 *  stage it for removal(create a file named by itself with no content)
 *  and remove the file from
 *  the working directory if the user has not already done so
 *  (do not remove it unless it is tracked in the current commit).
 *
 *  If the file is neither staged nor tracked by the head commit,
 *  print the error message "No reason to remove the file."
 */

public class Remove {
    public static void remove(String fileName) {
        if (!isStagedForAddition(fileName) && !isTracked(fileName)){
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        if (isStagedForAddition(fileName)) {
            Add.removeAddFromStage(fileName);
        }
        if (isTracked(fileName)) {
            if (!isStagedForRemoval(fileName)) {
                File file = Utils.join(Repository.STAGING_REMOVAL_FOLDER, fileName);
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            File fileToDelete = Utils.join(Repository.CWD, fileName);
            if (fileToDelete.exists()) {
                fileToDelete.delete();
            }
        }
    }

    /** Return true if the given file is tracked by the current commit. */
    public static boolean isTracked(String fileName) {
        String head = Commit.getHead();
        Commit commit = Commit.findCommit(head);
        TreeMap<String, String> blobs = commit.getBlobs();
        if (blobs == null) {
            return false;
        }
        return blobs.containsKey(fileName);
    }

    public static boolean isStagedForAddition(String fileName) {
        List<String> fileNames = Utils.plainFilenamesIn(Repository.STAGING_ADDITION_FOLDER);
        if (fileNames == null) {
            return false;
        }
        return fileNames.contains(fileName);
    }

    public static boolean isStagedForRemoval(String fileName) {
        List<String> fileNames = Utils.plainFilenamesIn(Repository.STAGING_REMOVAL_FOLDER);
        if (fileNames == null) {
            return false;
        }
        return fileNames.contains(fileName);
    }
}
