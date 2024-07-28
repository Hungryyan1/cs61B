package gitlet;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

public class Checkout {
    /**
     * Takes the version of the file as it exists
     * in the head commit and puts it in the working directory,
     * overwriting the version of the file
     * that’s already there if there is one.
     * The new version of the file is not staged.
     * If the file does not exist in the previous commit,
     * abort, printing the error message
     * "File does not exist in that commit."
     * @param fileName
     */
    public static void checkoutFile(String fileName) {
        String head = Commit.getHead();
        checkoutFileInCommit(head, fileName);
    }

    /** Find the full commit ID according to the short*/
    public static String findFullCommitID(String shortID) {
        File commitsDir = Utils.join(Repository.OBJECT_FOLDER, "Commits");
        for (String fileName : Utils.plainFilenamesIn(commitsDir)) {
            if (fileName.contains(shortID)) {
                return fileName;
            }
        }
        return null;
    }

    /**
     * Checkout the file in the given commit.
     */
    public static void checkoutFileInCommit(String commitID, String fileName) {
        if (commitID.length() < 40 ) { // It is abbreviated
            commitID = findFullCommitID(commitID);
            if (commitID == null) {
                System.out.println("No commit with that id exists.");
                System.exit(1);
            }
        }
        Commit commit = Commit.findCommit(commitID);
        if (commit == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        TreeMap<String, String> blobs = commit.getBlobs();
        if (blobs == null) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        if (!blobs.containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        // get the Sha-1 hash to search the file in the Object directory.
        String fileID = blobs.get(fileName);
        File checkoutFile = Utils.join(Repository.OBJECT_FOLDER, fileID);
        File fileToBeOverwritten = Utils.join(Repository.CWD, fileName);
        if (fileToBeOverwritten.exists()) { // overwritten
            fileToBeOverwritten.delete();
        }
        Add.copyFile(checkoutFile, fileToBeOverwritten);
    }

    /**
     * Takes all files in the commit at the head of the given branch,
     * and puts them in the working directory,
     * overwriting the versions of the files
     * that are already there if they exist.
     * Also, at the end of this command, the given branch will now
     * be considered the current branch (HEAD).
     * Any files that are tracked in the current branch
     * but are not present in the checked-out branch are deleted.
     * The staging area is cleared, unless the checked-out branch
     * is the current branch.
     * @param branchName
     */
    public static void checkoutBranch(String branchName) {
        File branchHead = Utils.join(Repository.BRANCHES_FOLDER, branchName);
        if (!branchHead.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        String head = Commit.getHead();
        Commit headCommit = Commit.findCommit(head);
        if (branchName.equals(headCommit.getBranch())) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        // If a working file is untracked in the current
        // branch and would be overwritten by the checkout
        String branchCommitID = Utils.readContentsAsString(branchHead);
        checkoutByCommitID(branchCommitID);

    }

    public static void checkoutByCommitID(String commitID) {
        Commit commit = Commit.findCommit(commitID);
        TreeMap<String, String> blobs = commit.getBlobs();
        List<String> workingFiles = Utils.plainFilenamesIn(Repository.CWD);
        if (workingFiles != null) {
            for (String fileName : workingFiles) {
                if (blobs == null || blobs.containsKey(fileName)) {
                    System.out.println("There is an untracked file in the way; " +
                            "delete it, or add and commit it first.");
                    System.exit(0);
                }
            }
        }
        // To this point, every file in the CWD is tracked by the current commit.
        // Any files that are tracked in the current branch
        // but are not present in the checked-out branch are deleted.
        if (workingFiles != null) {
            for (String fileName : workingFiles) {
                if (!blobs.containsKey(fileName)) {
                    File fileToDelete = Utils.join(Repository.CWD, fileName);
                    fileToDelete.delete();
                }
            }
        }
        // deal with each file
        for (String fileName : blobs.keySet()) {
            checkoutFileInCommit(commitID, fileName);
        }

        Add.clearStagingArea();
    }
}
