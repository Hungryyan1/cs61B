package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.Utils.join;

/** Represent a gitlet init object.
 *  Create .gitlet file if not already exists
 *  (if exists, exit with error message)
 *  start with one commit "initial commit" with no file
 *  Should use method in Commit
 */

public class Init {
    static final File CWD = new File(System.getProperty("user.dir"));

    static final File GITLET_FOLDER = join(CWD, ".gitlet");

    public static void setGitlet() throws IOException {
        if (!GITLET_FOLDER.exists()) {
            GITLET_FOLDER.mkdir();
            Commit firstCommit = new Commit("initial commit", null, null);
            Repository.createHeadFolder();
            Repository.createBranchFolder();
            Repository.createObjectFolder();
            firstCommit.writeCommit();
            firstCommit.makeBranchHead("master");
            firstCommit.makeHead("master");
        } else {
            System.out.println("A Gitlet version-control system" +
                    " already exists in the current directory.");
            System.exit(0);
        }
    }
}
