package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

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

    public static final File STAGING_ADDITION_FOLDER = Utils.join(STAGING_FOLDER, "addition");

    public static final File STAGING_REMOVAL_FOLDER = Utils.join(STAGING_FOLDER, "removal");

    public static final File BRANCHES_FOLDER = Utils.join(HEADS_FOLDER, "branches");
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
        if (!STAGING_ADDITION_FOLDER.exists()) {
            STAGING_ADDITION_FOLDER.mkdir();
        }
        if (!STAGING_REMOVAL_FOLDER.exists()) {
            STAGING_REMOVAL_FOLDER.mkdir();
        }
    }

    public static void createBranchFolder() {
        if (!BRANCHES_FOLDER.exists()) {
            BRANCHES_FOLDER.mkdir();
        }
    }
    /** Return false if the current directory doesn't contain .gitlet folder.*/
    public static boolean isGitlet() {
        return GITLET_DIR.exists();
    }

    /** Run the Init commit. */
    public static void init()  {
        try {
            Init.setGitlet();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Run the Add commit
     * If the current working version of the file is identical
     * to the version in the current commit, do not stage it to
     * be added, and remove it from the staging area if it is already there  */
    public static void add(String fileName) {
        File fileToStage = Utils.join(CWD, fileName);
        if (!fileToStage.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        if (Add.isSameAsCurrentCommit(fileName)) {
            //System.out.println(fileName + " is the same as the current commit");
            //the current working version of the file is
            // identical to the version in the current commit.
            List<String> fileNames = Utils.plainFilenamesIn(STAGING_ADDITION_FOLDER);
            if (!fileNames.isEmpty()) {
                if (fileNames.contains(fileName)) {
                    Add.removeAddFromStage(fileName);
                }
            }
            System.exit(0);
        }
        if (Add.isStaged(fileToStage)) { // is identical to the staged file
            System.exit(0);
        }
        createStagingFolder();
        Add.copyFileToStage(fileName);
    }

    /** Make a commit */
    public static void commit(String message) throws IOException {
        // see if staging area is empty
        if (Add.isStageEmpty()) {
            //System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        // create the object folder and the Head folder
        createObjectFolder();
        createHeadFolder();
        // Find this commit's parent, should be the head ref in the directory.
        String parent = Commit.getHead();
        // copy the parent commit
        Commit parentCommit = Commit.findCommit(parent);
        TreeMap<String, String> blobsTree = parentCommit.getBlobs();

        // iterate through the staging area to change the blobs in the parent commit.
        for (String fileName : Utils.plainFilenamesIn(STAGING_ADDITION_FOLDER)) {
            if (blobsTree != null && blobsTree.containsKey(fileName)) {
                blobsTree.remove(fileName);
            }
            File fileStaged =Utils.join(STAGING_ADDITION_FOLDER, fileName);
            String hash = Utils.sha1((Object) Utils.readContents(fileStaged));
            File fileToCommit = Utils.join(OBJECT_FOLDER, hash);

            // put the new blobs in the tree map
            if (blobsTree == null) {
                blobsTree = new TreeMap<>();
            }
            blobsTree.put(fileName, hash);
            // Copy the file from staging area to the object folder
            Add.copyFile(fileStaged, fileToCommit);
        }

        Commit newCommit = new Commit(message, blobsTree, parent, parentCommit.getBranch());
        newCommit.makeHead();
        newCommit.makeBranchHead(newCommit.getBranch());
        newCommit.writeCommit();

        // clear the staging area after a commit
        Add.clearStagingArea();
    }

    /** run rm */
    public static void remove(String fileName) throws IOException {
        Remove.remove(fileName);
    }

    /** run log */
    public static void log() {
        Log.log();
    }

    /** run global-log */
    public static void globalLog() {
        Log.globalLog();
    }

    /** run find */
    public static void find(String message) {
        Find.find(message);
    }

    /** run status */
    public static void status() {
        Status.printBranch();
        System.out.println();
        Status.printStaged();
        System.out.println();
        Status.printRemoval();
        System.out.println();
        Status.printModification();
        System.out.println();
        Status.printUntracked();
        System.out.println();
    }

    public static void branch(String branch) {
        Branch.createBranch(branch);
    }

    public static void rmBranch(String branch) {
        Branch.deleteBranch(branch);
    }

    /** The command is essentially checkout of an arbitrary commit
     *  that also changes the current branch head.*/
    public static void reset(String commitID) {
        Checkout.checkoutByCommitID(commitID);
        Commit commit = Commit.findCommit(commitID);
        commit.makeHead();
    }

}
