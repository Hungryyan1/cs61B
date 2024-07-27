package gitlet;

import java.io.File;

/**
 * Starting at the current head commit,
 * display information about each commit backwards
 * along the commit tree until the initial commit,
 * following the first parent commit links,
 * ignoring any second parents found in merge commits.
 */

public class Log {
    public static void log() {
        String commitID = Commit.getHead();
        Commit commit = Commit.findCommit(commitID);
        while (true) {
            printCommitMSG(commit);
            String parentID = commit.getParent();
            if (parentID != null) {
                commit = Commit.findCommit(parentID);
            } else {
                break;
            }
        }
    }

    public static void globalLog() {
        File commitFolder = Utils.join(Repository.OBJECT_FOLDER, "Commits");
        for (String fileName : Utils.plainFilenamesIn(commitFolder)) {
            Commit commit = Commit.findCommit(fileName);
            printCommitMSG(commit);
        }
    }

    /** print the extra log info when the commit have two parents*/
    public static void printMergeMSG(Commit commit) {
        String parentID = commit.getParent();
        String secondParentID = commit.getSecondParent();
        System.out.println("Merge: " + parentID.substring(0, 7)
                + " " + secondParentID.substring(0, 7));
    }

    /** print the regular log info of a commit */
    public static void printCommitMSG(Commit commit) {
        String commitID = commit.getCommitId();
        System.out.println("===");
        System.out.println("commit "+ commitID);
        if (commit.getSecondParent() != null) {
            printMergeMSG(commit);
        }
        System.out.println("Date: " + commit.getTimestamp());
        System.out.println(commit.getMessage());
        System.out.println();
    }
}
