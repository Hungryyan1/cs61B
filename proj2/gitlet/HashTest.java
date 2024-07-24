package gitlet;


import java.io.File;
import java.util.Arrays;

public class HashTest {
    public static void main(String[] args) throws Exception {
        File f = new File("dummy.txt");
        String s = "Hello World";
        Utils.writeContents(f, s);
        System.out.println(Utils.sha1("Hello World"));
        System.out.println(Utils.sha1((Object) Utils.readContents(f)));
        long unixTime = System.currentTimeMillis() / 1000L;
        System.out.println(unixTime);
    }
}
