package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Date; // TODO: You'll likely use this in this class

/** Represents a gitlet commit object.
 *  Write the commit message(message, time, blobRefs, parentRef)
 *  to a file, then generate its Sha-1 Hash value, add to a
 *  commit tree.
 *  Need some folders(named by hash) to store different versions
 *  of file so that we can use the hash ref to find them.
 *  Need a file to store our commits(hash).(Maybe Link list)
 *  Write and read the list object.
 *
 *  @author Hungry
 */
public class Commit implements Serializable {

    /** The message of this Commit. */
    private String message;
    /** The date and time of the commit */
    private String dateTime;

    /** References(SHA-1 Hash) for the files the commit has.*/
    private String[] blobRefs;
    /** Reference(Hash) of the parent */
    private String parentRef;

    private String Author;

    /** Commit Node to be added to the commit tree(List) */
    private class commitNode {
        public commitNode parent;
        public String commitID;

        public commitNode(commitNode parent, String hash) {
            this.parent = parent;
            this.commitID = hash;
        }
    }
    /* TODO: fill in the rest of this class. */

    /* TODO: deal with the dateTime */
    public Commit(String message, String dateTime,  String[] blobRefs, String parentRef, String author) {
        this.message = message;
        this.dateTime = dateTime;
        this.blobRefs = blobRefs;
        this.parentRef = parentRef;
        this.Author = author;
    }

    /** Create commit ID */
    public String createCommitID() {
        return Utils.sha1(message, dateTime, blobRefs, parentRef, Author);
    }

    /** Make a commit */
    public void makeCommit() {}
}
