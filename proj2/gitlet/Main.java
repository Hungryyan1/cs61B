package gitlet;

import java.io.IOException;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Hungry
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                validateNumArgs(args, 1);
                Repository.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                checkGitlet();
                validateNumArgs(args, 2);
                String fileName = args[1];
                Repository.add(fileName);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                checkGitlet();
                if (args.length == 1) {
                    System.out.println("Please enter a commit message.");
                }
                validateNumArgs(args, 2);
                String message = args[1];
                try {
                    Repository.commit(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "rm":
                checkGitlet();
                validateNumArgs(args, 2);
                String fileToRemove = args[1];
                try {
                    Repository.remove(fileToRemove);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "log":
                checkGitlet();
                validateNumArgs(args, 1);
                Repository.log();
                break;
            case "global-log":
                checkGitlet();
                validateNumArgs(args, 1);
                Repository.globalLog();
                break;
            case "find":
                checkGitlet();
                validateNumArgs(args, 2);
                Repository.find(args[1]);
                break;
            case "status":
                checkGitlet();
                validateNumArgs(args, 1);
                Repository.status();
                break;
            case "checkout":
                checkGitlet();
                if (args.length == 2) {
                    Checkout.checkoutBranch(args[1]);
                } else if (args.length == 3 && args[1].equals("--")) {
                    Checkout.checkoutFile(args[2]);
                } else if (args.length == 4 && args[2].equals("--")) {
                    Checkout.checkoutFileInCommit(args[1], args[3]);
                } else {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                break;
            default:
            System.out.println("No command with that name exists.");
            System.exit(0);
        }
    }

    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }

    public static void checkGitlet() {
        if (!Repository.isGitlet()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }
}
