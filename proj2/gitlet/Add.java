package gitlet;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.List;
import java.util.TreeMap;

/** Try to do some functionality related to add here.
 *  Adds a copy of the file as it currently exists to the staging area.
 *  If the current working version of the file is identical
 *  to the version in the current commit, do not stage it to
 *  be added, and remove it from the staging area if it is already there  */

public class Add  {
    /** To see if the file has already been staged. */
    public static boolean isStaged(String fileName) {
        List<String> fileNames = Utils.plainFilenamesIn(Repository.STAGING_FOLDER);
        if (fileNames == null) {
            return false;
        }
        if (fileNames.contains(fileName)) { // has the same name
            File fileToStage = Utils.join(Repository.CWD, fileName);
            File fileStaged = Utils.join(Repository.STAGING_FOLDER, fileName);
            if (fileToStage.equals(fileStaged)) { // they have the same content
                return true;
            } else { // delete the staged but modified file
                fileStaged.delete();
                return false;
            }
        }
        return false;
    }

    private static void copyFile(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }

    /** Copy the file to the staging area if it is not staged.
     * else */
    public static void copyFileToStage(String fileName) throws IOException {
        if (!isStaged(fileName)) {
            File fileToStage = Utils.join(Repository.CWD, fileName);
            File fileStaged = Utils.join(Repository.STAGING_FOLDER, fileName);
            copyFile(fileToStage, fileStaged);
        }
    }

    /** Return true if the current working version of the file is identical
     *  to the version in the current commit */
    public static boolean isSameAsCommitVersion(String fileName)  {
        File fileToStage = Utils.join(Repository.CWD, fileName);
        String head = Commit.getHead();
        Commit commit = Commit.findCommit(head);
        if (commit == null) {
            return false;
        }
        TreeMap<String, String> map = commit.getBlobs();
        if (map == null) {
            return false;
        }
        return map.containsKey(Utils.sha1(fileToStage));
    }

    /** Remove a file from staging area */
    public static void removeFromStage(String fileName)  {
        if (isStaged(fileName)) {
            File fileStaged = Utils.join(Repository.STAGING_FOLDER, fileName);
            fileStaged.delete();
        } else {
            System.out.println(fileName + " is not a staging file");
        }
    }

}
