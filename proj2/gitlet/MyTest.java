package gitlet;

import java.io.File;
import java.io.IOException;

public class MyTest {


    public static void main(String[] args) {
        System.out.println(GetDate.getDate0());
        System.out.println(GetDate.getDate());
        System.out.println(Utils.sha1("a", "b", null));
    }
}
