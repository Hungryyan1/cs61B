package gitlet;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.TreeMap;

/** Try to do some functionality related to add here.
 *  Adds a copy of the file as it currently exists to the staging area.
 *  If the current working version of the file is identical
 *  to the version in the current commit, do not stage it to
 *  be added, and remove it from the staging area if it is already there  */

public class Add  {
    /** To see if the file has already been staged.
     *  If returns true, must have exact same contents.
     *  Use hash to identify if the two file equals*/
    public static boolean isStaged(File file) {
        List<String> fileNames = Utils.plainFilenamesIn(Repository.STAGING_ADDITION_FOLDER);
        if (fileNames == null) {
            return false;
        }
        for (String fileName : fileNames) {
            File f = Utils.join(Repository.STAGING_ADDITION_FOLDER, fileName);
            String hash = Utils.sha1(Utils.readContentsAsString(f) + fileName);
            if (hash.equals(Utils.readContentsAsString(file))) {
                return true;
            }
        }
        return false;
    }

    public static void copyFile(File source, File dest) {
        try {
            Files.copy(source.toPath(), dest.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Copy the file to the staging area for addition if it is not staged.
     *  If*/
    public static void copyFileToStage(String fileName) {
        File fileToStage = Utils.join(Repository.CWD, fileName);
        if (!isStaged(fileToStage)) {
            List<String> fileNames = Utils.plainFilenamesIn(Repository.STAGING_ADDITION_FOLDER);
            if (fileNames != null){
                if (fileNames.contains(fileName)) {
                    removeAddFromStage(fileName);
                }
            }
            File fileStaged = Utils.join(Repository.STAGING_ADDITION_FOLDER, fileName);
            copyFile(fileToStage, fileStaged);
        }
    }

    /** Return true if the current working version of the file is identical
     *  to the version in the current commit */
    public static boolean isSameAsCurrentCommit(String fileName)  {
        File fileToStage = Utils.join(Repository.CWD, fileName);
        String head = Commit.getHead();
        Commit commit = Commit.findCommit(head);
        if (commit == null) {
            //System.out.println("Commit is null");
            return false;
        }
        TreeMap<String, String> map = commit.getBlobs();
        if (map == null) {
            //System.out.println("Blobs is null");
            return false;
        }
        return map.containsValue(Utils.sha1(Utils.readContentsAsString(fileToStage) + fileName));
    }

    /** Remove a file from staging for addition area */
    public static void removeAddFromStage(String fileName)  {
        File fileStaged = Utils.join(Repository.STAGING_ADDITION_FOLDER, fileName);
        fileStaged.delete();
    }

    /** Remove a file from staging for removal area */
    public static void removeRemovalFromStage(String fileName)  {
        File fileStaged = Utils.join(Repository.STAGING_REMOVAL_FOLDER, fileName);
        fileStaged.delete();
    }

    /** Clear the staging area (after a commit) */
    public static void clearStagingArea() {
        List<String> fileAddNames = Utils.plainFilenamesIn(Repository.STAGING_ADDITION_FOLDER);
        List<String> fileRemoveNames = Utils.plainFilenamesIn(Repository.STAGING_REMOVAL_FOLDER);
        if (fileAddNames == null && fileRemoveNames == null) {
            return;
        }
        if (fileAddNames != null) {
            for (String fileName : fileAddNames) {
                removeAddFromStage(fileName);
            }
        }
        if (fileRemoveNames != null) {
            for (String fileName : fileRemoveNames) {
                removeRemovalFromStage(fileName);
            }
        }

    }

    /** If stage for addition is empty*/
    public static boolean isStageEmpty() {
        List<String> addFiles = Utils.plainFilenamesIn(Repository.STAGING_ADDITION_FOLDER);
        List<String> rmFiles = Utils.plainFilenamesIn(Repository.STAGING_REMOVAL_FOLDER);
        if (addFiles == null && rmFiles == null) {
            return true;
        }
        if (addFiles == null) {
            return rmFiles.isEmpty();
        }
        if (rmFiles == null) {
            return addFiles.isEmpty();
        }
        return rmFiles.isEmpty() && addFiles.isEmpty();
    }
}
