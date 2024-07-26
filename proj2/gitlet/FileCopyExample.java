package gitlet;

import org.apache.commons.beanutils.ConvertingWrapDynaBean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileCopyExample {
    private static void copyFileUsingJava7Files(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }
    public static void main(String[] args) throws IOException {
        //System.out.println(Repository.CWD);
        File fileToCopy = Utils.join(Repository.CWD,  "dummy.txt");
        File placeToCopy = Utils.join(Repository.CWD, "testing", "place.txt");
        copyFileUsingJava7Files(fileToCopy, placeToCopy);
    }
}