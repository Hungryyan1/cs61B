package gitlet;


import java.util.List;

/**
 *  Unstage the file if it is currently staged for addition.
 *  If the file is tracked in the current commit,
 *  stage it for removal and remove the file from
 *  the working directory if the user has not already done so
 *  (do not remove it unless it is tracked in the current commit).
 *
 *  If the file is neither staged nor tracked by the head commit,
 *  print the error message "No reason to remove the file."
 */

public class Remove {
    public static void remove(String fileName) {
        if (!isStaged(fileName) && !isTracked(fileName)){
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        if (isStaged(fileName)) {
            Add.removeAddFromStage(fileName);
        }
        if (isTracked(fileName)) {
            Utils.restrictedDelete(fileName);
        }
    }

    public static boolean isTracked(String fileName) {
        return false;
    }

    public static boolean isStaged(String fileName) {
        List<String> fileNames = Utils.plainFilenamesIn(Repository.STAGING_FOLDER);
        if (fileNames == null) {
            return false;
        }
        return fileNames.contains(fileName);
    }
}
