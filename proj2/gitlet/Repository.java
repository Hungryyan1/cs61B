package gitlet;

import java.io.File;
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
    public static final File COMMITS_FOLDER = Utils.join(GITLET_DIR, "Commits");
    /** The Heads folder to store the head pointer and branchNames and their heads. */
    public static final File HEADS_FOLDER = Utils.join(GITLET_DIR, "Heads");
    /* TODO: fill in the rest of this class. */


    public static void createCommitsFolder() {
        if (!COMMITS_FOLDER.exists()) {
            COMMITS_FOLDER.mkdir();
        }
    }

    public static void createHeadFolder() {
        if (!HEADS_FOLDER.exists()) {
            HEADS_FOLDER.mkdir();
        }
    }

    /** Make a commit */
    public static void commit(String message) {
        // Should somehow get blobs from the staging area,
        // see if staging area is empty

        createCommitsFolder();
        createHeadFolder();
        // Find this commit's parent, should be the head ref in the directory.
        String parent = Commit.getHead();
        // copy the parent commit
        Commit parentCommit = Commit.findCommit(parent);
        Commit newCommit = new Commit(message, parentCommit.getBlobs(), parent, parentCommit.getBranch());

    }
}
