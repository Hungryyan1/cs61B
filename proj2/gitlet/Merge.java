package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeMap;

public class Merge {
    
    private static String getBranchHead(String branch) {
        String currentHead = Commit.getHead();
        String currentBranch = Commit.getCurrentBranch();
        File branchHeadFile = Utils.join(Repository.BRANCHES_FOLDER, branch);
        if (!branchHeadFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (currentHead.equals(currentBranch)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        return Utils.readContentsAsString(branchHeadFile);
    }
    
    /** Return the ID of the split point of the current branch and the given branch.
     * The split point is a latest common ancestor of the current and given branch heads.*/
    private static String splitPoint(String branch) {
        String currentHead = Commit.getHead();
        String branchHead = getBranchHead(branch);
        List<String> branchAncestors = ancestors(branchHead);
        List<String> currentAncestors = ancestors(currentHead);
        if (currentAncestors.contains(branchHead)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
        if (branchAncestors.contains(currentHead)) {
            System.out.println("Current branch fast-forwarded.");
            Checkout.checkoutBranch(branch);
            System.exit(0);
        }
        // Find the latest common ancestors
        int i = 0;
        while (branchAncestors.get(i).equals(currentAncestors.get(i))) {
            i++;
        }
        return branchAncestors.get(i - 1);
    }

    /**
     * @param head the commit ID of head
     * @return Return a list of ancestors of the given head in time sequence.
     */
    private static List<String> ancestors(String head) {
        Commit headCommit = Commit.findCommit(head);
        List<String> ancestors = new ArrayList<>();
        while (headCommit != null) {
            ancestors.addFirst(headCommit.getCommitId());
            String parentID = headCommit.getParent();
            headCommit = Commit.findCommit(parentID);
        }
        return ancestors;
    }

    public static void merge(String branch) {
        if (!Add.isStageEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        String splitPoint = splitPoint(branch);
        List<String> allFileNames = allFileNames(splitPoint, branch);
        String head = Commit.getHead();
        String currentBranch = Commit.getCurrentBranch();
        String branchHead = getBranchHead(branch);

        boolean isConflict = false;

        for (String fileName : allFileNames) {
            if (!existsIn(splitPoint, fileName)) {
                if (!existsIn(head, fileName) && existsIn(branchHead, fileName)) {
                    //case 5
                    Checkout.checkoutFileInCommit(branchHead, fileName);
                    Repository.add(fileName);
                } else if (existsIn(head, fileName) && !existsIn(branchHead, fileName)) {
                    continue; // case 4
                }
            } else { // file exists in splitPoint
                if (!existsIn(branchHead, fileName) && existsIn(head, fileName)) {
                    // case 6
                    if (!isModifiedIn(splitPoint, head, fileName)) {
                        Remove.remove(fileName); // remove the file
                        untracked(head, fileName);
                        continue;
                    }
                }
                if (!existsIn(head, fileName) && existsIn(branchHead, fileName)) {
                    // case 7
                    if (!isModifiedIn(splitPoint, branchHead, fileName)) {
                        continue;
                    }
                }
                if (isModifiedIn(splitPoint, branchHead, fileName) && !isModifiedIn(splitPoint, head, fileName)) {
                    //case 1
                    Checkout.checkoutFileInCommit(branchHead, fileName);
                    Repository.add(fileName);
                    continue;
                }
                if (isModifiedIn(splitPoint, head, fileName) && !isModifiedIn(splitPoint, branchHead, fileName)) {
                    //case 2
                    continue;
                }
                if (isModifiedIn(splitPoint, branchHead, fileName) && isModifiedIn(splitPoint, head, fileName)) {
                    // case 3 and 8
                    if (isFileSameInCommits(branchHead, head, fileName)) {
                        continue;
                    } else {
                        // TODO: deal with conflicts
                        isConflict = true;
                        dealWithConflict(fileName, branchHead, head);
                    }
                }

            }
        }
        Repository.commit("Merged " + branch + " into " + currentBranch + ".");
        String newHead = Commit.getHead();
        Commit newHeadCommit = Commit.findCommit(newHead);
        newHeadCommit.setSecondParent(branchHead);
        newHeadCommit.writeCommit();
        if (isConflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    private static void dealWithConflict(String fileName, String branchID, String headID) {
        return;
    }

    /** Return true if the files in the two commits are identical. */
    private static boolean isFileSameInCommits(String headBranchID, String otherBranchID, String fileName) {
        // the two commit should not be null when we call it
        Commit headCommit = Commit.findCommit(headBranchID);
        Commit otherCommit = Commit.findCommit(otherBranchID);
        TreeMap<String, String> headBlobs = headCommit.getBlobs();
        TreeMap<String, String> otherBlobs = otherCommit.getBlobs();
        // deal with null pointer first
        if (headBlobs == null && otherBlobs == null) {
            return true;
        } else if (headBlobs != null && otherBlobs == null) {
            return headBlobs.containsKey(fileName);
        } else if (headBlobs == null && otherBlobs != null) {
            return otherBlobs.containsKey(fileName);
        } else {
            if (!headBlobs.containsKey(fileName) && !otherBlobs.containsKey(fileName)) {
                return true;
            } else if (!headBlobs.containsKey(fileName) && otherBlobs.containsKey(fileName)) {
                return false;
            } else if (headBlobs.containsKey(fileName) && !otherBlobs.containsKey(fileName)) {
                return false;
            }
            String headFile = headBlobs.get(fileName);
            String otherFile = otherBlobs.get(fileName);
            return headFile.equals(otherFile);
        }
    }

    /** Make the file untracked in the given branchHead. */
    private static void untracked(String branchHead, String fileName) {
        Commit commit = Commit.findCommit(branchHead);
        TreeMap<String, String> blobs = commit.getBlobs();
        if (blobs != null && blobs.containsKey(fileName)) {
            blobs.remove(fileName);
        }
        commit.writeCommit();
    }

    /**
     * @param commitID the given commit ID
     * @param fileName the given file name
     * @return Return true if a file is tracked in the given commit.
     */
    private static boolean existsIn(String commitID, String fileName) {
        Commit commit = Commit.findCommit(commitID);
        TreeMap<String, String> commitBlobs = commit.getBlobs();
        if (commitBlobs == null) {
            return false;
        }
        return commitBlobs.containsKey(fileName);
    }


    /**
     * @param splitID  the ID of the split point
     * @param branchName the given branch name
     * @param fileName the name of the given file
     * @return return true if the given file in the given branch head if modified from
     *         the version in split commit
     */
    private static boolean isModifiedIn(String splitID, String branchName , String fileName) {
        return isFileSameInCommits(splitID, branchName, fileName);
    }

    /**
     * @param splitID the ID of the split point
     * @param branchName the given branch name
     * @return a List of file names tracked in the three commit(split, branchHead, currentHead)
     */
    private static List<String> allFileNames(String splitID, String branchName) {
        List<String> fileNames = new ArrayList<>();

        Commit split = Commit.findCommit(splitID);
        String headID = Commit.getHead();
        Commit headCommit = Commit.findCommit(headID);
        Commit branchCommit = Commit.findCommit(branchName);
        TreeMap<String, String> headBlobs = headCommit.getBlobs();
        TreeMap<String, String> branchBlobs = branchCommit.getBlobs();
        TreeMap<String, String> splitBlobs = split.getBlobs();
        if (headBlobs != null) {
            for (String fileName : headBlobs.keySet()) {
                if (!fileNames.contains(fileName)) {
                    fileNames.add(fileName);
                }
            }
        }
        if (branchBlobs != null) {
            for (String fileName : branchBlobs.keySet()) {
                if (!fileNames.contains(fileName)) {
                    fileNames.add(fileName);
                }
            }
        }
        if (splitBlobs != null) {
            for (String fileName : splitBlobs.keySet()) {
                if (!fileNames.contains(fileName)) {
                    fileNames.add(fileName);
                }
            }
        }
        return fileNames;
    }
}
