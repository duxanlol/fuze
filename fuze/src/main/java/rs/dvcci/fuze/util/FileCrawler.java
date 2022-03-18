package rs.dvcci.fuze.util;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileCrawler {

    public List<File> files;
    public String directoryName;

    public List<File> getFiles() {
        if (this.files == null){
            this.files = new ArrayList<>();
            listf(directoryName, this.files);
        }
        return this.files;
    }

    public void listf(String directoryName, List<File> files) {
        File directory = new File(directoryName);

        // Get all files from a directory.
        File[] fList = directory.listFiles();
        if(fList != null)
            for (File file : fList) {
                if (file.isFile()) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    listf(file.getAbsolutePath(), files);
                }
            }
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }
}
