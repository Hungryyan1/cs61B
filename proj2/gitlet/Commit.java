package gitlet;

// TODO: any imports you need here

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

/** Represents a gitlet commit object.
 *  Write the commit message(message, time, blobRefs, parentRef)
 *  to a file, then generate its Sha-1 Hash value, add to a
 *  commit tree.
 *  Need some folders(named by hash) to store different versions
 *  of file so that we can use the hash ref to find them.
 *  Need a file to store our commits(hash).(Maybe Link list)
 *  Write and read the list object.
 *
 *  Usage:
 *  new commit(,,,), createObjectFolder, createHeadsFolder
 *  makeHead(), writeCommit()
 *  @author Hungry
 */
public class Commit implements Serializable {

    /** The message of this Commit. */
    private String message;
    /** The date and time of the commit */
    private String timestamp;
    /** References(SHA-1 Hash) for the files the commit has.
     * <Filename, Hash> */
    private TreeMap<String, String> blobs;
    /** Reference(Hash) of the parent */
    private String parent;
    private String secondParent;
    /** SHA-1 Hash for the current commit */
    private String commitId;
    /** Record the current branch the commit at */
    /* TODO: fill in the rest of this class. */


    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getParent() {
        return parent;
    }

    public String getCommitId() {
        return commitId;
    }

    public String getSecondParent() { return secondParent; }

    public TreeMap<String, String> getBlobs() {return blobs;}

    public Commit(String message,  TreeMap<String, String> blobs, String parent) {
        if (Objects.equals(message, "initial commit")){
            this.timestamp = GetDate.getDate0();

        } else {
            this.timestamp = GetDate.getDate();
        }
        this.message = message;
        this.blobs = blobs;
        this.parent = parent;
        this.commitId = createCommitID();
        this.secondParent = null;
    }

    /** Create commit ID */
    public String createCommitID() {
        if (parent == null) { //first commit
            return Utils.sha1(message, timestamp);
        } else if (blobs == null) {
            return Utils.sha1(message,  parent, timestamp);
        }
        return Utils.sha1(message, blobs.toString(), parent, timestamp);
    }

    /** Make the Head pointer point at the current commit(in master branch)
     *  The structure is Heads/branchName/Head of the branch(hash)
     *                        /head
     *  In other words, we use the file directory to represent branch info.
     * */
    public void makeHead(String branchName) {
        List<String> headFiles = Utils.plainFilenamesIn(Repository.HEADS_FOLDER);
        if (headFiles != null) {
            for (String file : headFiles) {
                File fileToDelete = Utils.join(Repository.HEADS_FOLDER, file);
                fileToDelete.delete();
            }
        }
        File headFile = Utils.join(Repository.HEADS_FOLDER, branchName);
        try {
            headFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Utils.writeContents(headFile, commitId);
    }

    /** make a branch head with the given name, store it in the Branches folder
     * file name is the branch name, content is the commit ID */
    public void makeBranchHead(String BranchName) {
        File branchFile = Utils.join(Repository.BRANCHES_FOLDER, BranchName);
        if (branchFile.exists()) {
            branchFile.delete();
        }
        try {
            branchFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Utils.writeContents(branchFile, commitId);
    }

    /** Get the Head pointer of the given branch as the parent commit */
    public static String getHead() {
        File headFile = Utils.join(Repository.GITLET_DIR,"Heads");
        List<String> headBranchNames = Utils.plainFilenamesIn(headFile);
        // only one file should be in above
        String headBranch = headBranchNames.get(0);
        File head = Utils.join(Repository.HEADS_FOLDER, headBranch);
        return Utils.readContentsAsString(head);
    }

    /** return the name of the current branch */
    public static String getCurrentBranch() {
        File headFile = Utils.join(Repository.GITLET_DIR,"Heads");
        List<String> headBranchNames = Utils.plainFilenamesIn(headFile);
        // only one file should be in above
        return headBranchNames.get(0);
    }

    /** Make persistence. Save Commit Object in object/Commits folder*/
    public void writeCommit() {
        File COMMIT_FOLDER = Utils.join(Repository.OBJECT_FOLDER, "Commits");
        if (!COMMIT_FOLDER.exists()) {
            COMMIT_FOLDER.mkdir();
        }
        File file = Utils.join(COMMIT_FOLDER, commitId);
        if (file.exists()){
            file.delete();
        }
        Utils.writeObject(file, this);
    }

    /** Find the commit in the object/Commits folder according to commitID */
    public static Commit findCommit(String commitId) {
        File file = Utils.join(Repository.OBJECT_FOLDER, "Commits", commitId);
        if (!file.exists()){
            return null;
        }
        return Utils.readObject(file, Commit.class);
    }

}
