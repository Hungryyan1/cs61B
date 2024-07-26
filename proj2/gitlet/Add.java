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
     *  If returns true, must have exact same contents.*/
    public static boolean isStaged(File file) {
        List<String> fileNames = Utils.plainFilenamesIn(Repository.STAGING_FOLDER);
        if (fileNames == null) {
            return false;
        }
        for (String fileName : fileNames) {
            File f = Utils.join(Repository.STAGING_FOLDER, fileName);
            if (f.equals(file)) {
                return true;
            }
        }
        return false;
    }

    public static void copyFile(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }

    /** Copy the file to the staging area if it is not staged.
     *  If*/
    public static void copyFileToStage(String fileName) throws IOException {
        File fileToStage = Utils.join(Repository.CWD, fileName);
        if (!isStaged(fileToStage)) {
            List<String> fileNames = Utils.plainFilenamesIn(Repository.STAGING_FOLDER);
            if (fileNames != null){
                if (fileNames.contains(fileName)) {
                    removeFromStage(fileName);
                }
            }
            File fileStaged = Utils.join(Repository.STAGING_FOLDER, fileName);
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
            System.out.println("Commit is null");
            return false;
        }
        TreeMap<String, String> map = commit.getBlobs();
        if (map == null) {
            System.out.println("Blobs is null");
            return false;
        }
        return map.containsValue(Utils.sha1((Object) Utils.readContents(fileToStage)));
    }

    /** Remove a file from staging area */
    public static void removeFromStage(String fileName)  {
        File fileStaged = Utils.join(Repository.STAGING_FOLDER, fileName);
        fileStaged.delete();
    }

    /** Clear the staging area (after a commit) */
    public static void clearStagingArea() {
        List<String> fileNames = Utils.plainFilenamesIn(Repository.STAGING_FOLDER);
        if (fileNames == null) {
            return;
        }
        for (String fileName : fileNames) {
            removeFromStage(fileName);
        }
    }

    public static boolean isStageEmpty() {
        if (Utils.plainFilenamesIn(Repository.STAGING_FOLDER) == null) {
            return true;
        }
        return Utils.plainFilenamesIn(Repository.STAGING_FOLDER).isEmpty();
    }
}
