package gitlet;


/**
 *  Displays what branches currently exist,
 *  and marks the current branch with a *.
 *  Also displays what files have been staged
 *  for addition or removal.
 */

public class Status {
    public static void printBranch() {
        System.out.println("===" + " " + "Branches" + " " + "===");
        String currentBranch = Commit.getCurrentBranch();
        System.out.println("*" + currentBranch);
        // print other branches
        for (String branch : Utils.plainFilenamesIn(Repository.BRANCHES_FOLDER)) {
            if (!branch.equals(currentBranch)) {
                System.out.println(branch);
            }
        }
    }

    public static void printStaged() {
        System.out.println("===" + " " + "Staged Files" + " " + "===");
        if (Utils.plainFilenamesIn(Repository.STAGING_ADDITION_FOLDER) == null) {
            return;
        }
        for (String fileName : Utils.plainFilenamesIn(Repository.STAGING_ADDITION_FOLDER)) {
            System.out.println(fileName);
        }
    }

    public static void printRemoval() {
        System.out.println("===" + " " + "Removed Files" + " " + "===");
        if (Utils.plainFilenamesIn(Repository.STAGING_REMOVAL_FOLDER) == null) {
            return;
        }
        for (String fileName : Utils.plainFilenamesIn(Repository.STAGING_REMOVAL_FOLDER)) {
            System.out.println(fileName);
        }
    }

    public static void printModification() {
        System.out.println("===" + " " + "Modifications Not Staged For Commit" + " " + "===");
        // not required for the project
    }

    public static void printUntracked() {
        System.out.println("===" + " " + "Untracked Files" + " " + "===");
        // not required for the project
    }
}
