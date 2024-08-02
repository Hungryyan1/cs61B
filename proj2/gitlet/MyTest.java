package gitlet;

import java.io.File;
import java.io.IOException;

public class MyTest {


    public static void main(String[] args) {
        System.out.println(GetDate.getDate0());
        System.out.println(GetDate.getDate());
        System.out.println();
        System.out.println(GetDate.getDate());
        System.out.println();
        String s = "";
        System.out.println(s.isBlank());
        System.out.println("Hello World" + null);
        System.out.println(Commit.findCommit(null));
    }
}
