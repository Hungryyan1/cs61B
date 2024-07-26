package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The Commits folder to store hashes of different commits. */
    public static final File OBJECT_FOLDER = Utils.join(GITLET_DIR, "OBJECT");
    /** The Heads folder to store the head pointer and branchNames and their heads. */
    public static final File HEADS_FOLDER = Utils.join(GITLET_DIR, "Heads");
    /** Index file that represents staging area */
    public static final File STAGING_FOLDER = Utils.join(GITLET_DIR, "index");
    /* TODO: fill in the rest of this class. */


    public static void createObjectFolder() {
        if (!OBJECT_FOLDER.exists()) {
            OBJECT_FOLDER.mkdir();
        }
    }

    public static void createHeadFolder() {
        if (!HEADS_FOLDER.exists()) {
            HEADS_FOLDER.mkdir();
        }
    }

    public static void createStagingFolder() {
        if (!STAGING_FOLDER.exists()) {
            STAGING_FOLDER.mkdir();
        }
    }

    /** Run the Init commit. */
    public static void init() {
        Init.setGitlet();
    }

    /** Run the Add commit
     * If the current working version of the file is identical
     * to the version in the current commit, do not stage it to
     * be added, and remove it from the staging area if it is already there  */
    public static void add(String fileName) throws IOException {
        File fileToStage = Utils.join(CWD, fileName);
        if (!fileToStage.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        if (Add.isSameAsCommitVersion(fileName)) {
            //the current working version of the file is
            // identical to the version in the current commit.
            if (Add.isStaged(fileName)) {
                Add.removeFromStage(fileName);
            }
            System.exit(0);
        }
        createStagingFolder();
        Add.copyFileToStage(fileName);
    }

    /** Make a commit */
    public static void commit(String message) {
        // Should somehow get blobs from the staging area,
        // see if staging area is empty

        createObjectFolder();
        createHeadFolder();
        // Find this commit's parent, should be the head ref in the directory.
        String parent = Commit.getHead();
        // copy the parent commit
        Commit parentCommit = Commit.findCommit(parent);
        Commit newCommit = new Commit(message, parentCommit.getBlobs(), parent, parentCommit.getBranch());

    }
}
