package gitlet;

import java.io.File;
import java.util.Objects;

/**
 * Prints out the ids of all commits that have
 * the given commit message, one per line.
 * If there are multiple such commits,
 * it prints the ids out on separate lines. */
public class Find {
    public static void find(String message) {
        File commitFolder = Utils.join(Repository.OBJECT_FOLDER, "Commits");
        int idCounter = 0;
        for (String fileName : Utils.plainFilenamesIn(commitFolder)) {
            Commit commit = Commit.findCommit(fileName);
            if (Objects.equals(commit.getMessage(), message)) {
                System.out.println(commit.getCommitId());
                idCounter++;
            }
        }
        if (idCounter == 0) {
            System.out.println("Found no commit with that message.");
        }
    }
}
