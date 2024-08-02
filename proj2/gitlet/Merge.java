package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Merge {
    
    private static String getBranchHead(String branch) {
        File branchHeadFile = Utils.join(Repository.BRANCHES_FOLDER, branch);
        if (!branchHeadFile.exists()) {
            return null;
        }
        return Utils.readContentsAsString(branchHeadFile);
    }
    
    /** Return the ID of the split point of the current branch and the given branch.
     * The split point is a latest common ancestor of the current and given branch heads.*/
    private static String splitPoint(String branch) {
        String currentHead = Commit.getHead();
        String branchHead = getBranchHead(branch);
        if (branchHead == null) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (currentHead.equals(branchHead)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
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
        //System.out.println("split point: " + branchAncestors.get(i-1));
        //System.out.println("branchAncestors:" + branchAncestors);
        //System.out.println("currentAncestors:" + currentAncestors);
        //System.out.println("i:" + i);
        return branchAncestors.get(i - 1);
    }

    /**
     * @param head the commit ID of head
     * @return Return a list of ancestors of the given head in time sequence.
     */
    private static List<String> ancestors(String head) {
        Commit headCommit = Commit.findCommit(head);
        List<String> ancestors = new LinkedList<>();
        while (headCommit.getParent() != null) {
            ancestors.add(0, headCommit.getCommitId());
            headCommit = Commit.findCommit(headCommit.getParent());
        }
        ancestors.add(0, headCommit.getCommitId());

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
        //System.out.println("All files in this merge: " + allFileNames.toString());
        if (Utils.plainFilenamesIn(Repository.CWD) != null) {
            for (String fileName : Utils.plainFilenamesIn(Repository.CWD))
                checkUntrackedFile(fileName, branchHead, head, splitPoint);
        }


        for (String fileName : allFileNames) {
            if (!existsIn(splitPoint, fileName)) {
                if (!existsIn(head, fileName) && existsIn(branchHead, fileName)) {
                    //case 5
                    Checkout.checkoutFileInCommit(branchHead, fileName);
                    Repository.add(fileName);
                } else if (existsIn(head, fileName) && !existsIn(branchHead, fileName)) {
                    continue; // case 4
                }
            }

            if (!isModifiedIn(splitPoint, head, fileName) && !existsIn(branchHead, fileName)) {
                // case 6
                Remove.remove(fileName); // remove the file
                continue;

            }
            if (!existsIn(head, fileName) && !isModifiedIn(splitPoint, branchHead, fileName)) {
                // case 7
                continue;

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
                    isConflict = true;
                    dealWithConflict(fileName, branchHead, head);
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

    /**
     * Check if the file in the CWD will be removed or overwritten, if so, print error message.
     * @param fileName name of the file
     * @param branchHead ID of the branch head
     * @param head ID of current head
     */
    private static void checkUntrackedFile(String fileName, String branchHead, String head, String splitID) {
        Commit headCommit = Commit.findCommit(head);
        TreeMap<String, String> headBlobs = headCommit.getBlobs();
        Commit branchHeadCommit = Commit.findCommit(branchHead);
        TreeMap<String, String> branchHeadBlobs = branchHeadCommit.getBlobs();
        if (!Checkout.isFileTracked(fileName, headBlobs)) { // if the file is not tracked
            if (Checkout.isFileTracked(fileName, branchHeadBlobs) && Checkout.isToOverwrite(fileName, branchHeadBlobs)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
            if (!Checkout.isFileTracked(fileName, branchHeadBlobs) && !existsIn(branchHead, fileName)) {
                // the case that the file will be deleted. case 6
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }
    }

    /**
     * Deal with the conflicts in two files according to the spec.
     * @param fileName the name of the file
     * @param branchID the ID of the branch head
     * @param headID the ID of the current head
     */
    private static void dealWithConflict(String fileName, String branchID, String headID) {
        String contentInHead = readContentsAsString(fileName, headID);
        String contentConcatenated = "<<<<<<< HEAD" + "\n";
        if (contentInHead == null) {
            contentConcatenated += "\n";
        } else {
            contentConcatenated += contentInHead;
        }
        contentConcatenated += "=======";
        String contentInBranch = readContentsAsString(fileName, branchID);
        if (contentInBranch == null) {
            contentConcatenated += "\n";
        } else {
            contentConcatenated += contentInBranch;
        }
        contentConcatenated += ">>>>>>>";
        File fileInCWD = Utils.join(Repository.CWD, fileName);
        if (fileInCWD.exists()) {
            fileInCWD.delete();
        }
        try {
            fileInCWD.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Utils.writeContents(fileInCWD, contentConcatenated);
        Repository.add(fileName);
    }

    /**
     * @param fileName the name of the file
     * @param headID the ID of the head
     * @return the String contents in the file, if the file doesn't exist, return null;
     */
    private static String readContentsAsString(String fileName, String headID) {
        Commit commit = Commit.findCommit(headID);
        if (commit == null) {
            return null;
        }
        TreeMap<String, String> blobs = commit.getBlobs();
        if (blobs == null) {
            return null;
        }
        if (!blobs.containsKey(fileName)) {
            return null;
        } else {
            String fileID = blobs.get(fileName);
            File file = Utils.join(Repository.OBJECT_FOLDER, fileID);
            return Utils.readContentsAsString(file);
        }
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
     * @param branchID the given branch head ID
     * @param fileName the name of the given file
     * @return return true if the given file in the given branch head if modified from
     *         the version in split commit
     */
    private static boolean isModifiedIn(String splitID, String branchID , String fileName) {
        return !isFileSameInCommits(splitID, branchID, fileName);
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
        String branchHead = getBranchHead(branchName);
        Commit branchCommit = Commit.findCommit(branchHead);

        fileNames = FileNames(split, fileNames);
        //System.out.println("Files in split: " + fileNames);
        fileNames = FileNames(headCommit, fileNames);
        //System.out.println("Files in split and head" + fileNames);
        fileNames = FileNames(branchCommit, fileNames);

        return fileNames;
    }
    private static List<String> FileNames(Commit commit, List<String> fileNames) {
        if (commit != null) {
            TreeMap<String, String> commitBlobs = commit.getBlobs();
            if (commitBlobs != null) {
                //System.out.println("Keys are: "+ commitBlobs.keySet());
                for (String fileName : commitBlobs.keySet()) {
                    if(!fileNames.contains(fileName)) {
                        fileNames.add(fileName);
                    }
                }
            }
        }
        return fileNames;
    }
}
