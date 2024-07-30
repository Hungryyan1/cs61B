package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Branch {
    /** Creates a new branch with the given name,
     * and points it at the current head commit. */
    public static void createBranch(String branchName) {
        if (isBranchExist(branchName)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        // head is the SHA-1 hash of the current commit
        String head = Commit.getHead();
        File branchFile = Utils.join(Repository.BRANCHES_FOLDER, branchName);
        if (!branchFile.exists()) {
            try {
                branchFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // points it at the current head commit.
            Utils.writeContents(branchFile, head);
        }
    }

    public static boolean isBranchExist(String branchName) {
        List<String> branches = Utils.plainFilenamesIn(Repository.BRANCHES_FOLDER);
        if (branches == null) {
            return false;
        }
        return branches.contains(branchName);
    }

    /**Deletes the branch with the given name.
     * This only means to delete the pointer associated
     * with the branch; it does not mean to delete all commits
     * that were created under the branch, or anything like that.
     * */
    public static void deleteBranch(String branchName) {
        if (!isBranchExist(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (branchName.equals(Commit.getCurrentBranch())) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        File branchFile = Utils.join(Repository.BRANCHES_FOLDER, branchName);
        if (branchFile.exists()) {
            branchFile.delete();
        }
    }

}
