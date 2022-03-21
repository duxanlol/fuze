package rs.dvcci.fuze.data;

import lombok.Data;
import lombok.ToString;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.BoundExtractedResult;
import org.springframework.beans.factory.annotation.Autowired;
import rs.dvcci.fuze.util.FileCrawler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@ToString
public class Sector {
    String lesson;
    String activity;
    List<Guess> guesses;
    Guess picked;

    public Sector(String lesson, String activity, FileCrawler fileCrawler){
        this.lesson = lesson;
        this.activity = activity;
        this.guesses = getGuesses(fileCrawler);
    }



    public List<Guess> getGuesses(FileCrawler fileCrawler) {

        if (this.guesses == null && fileCrawler != null){
            this.guesses = new ArrayList<>();
            List<File> files = fileCrawler.getFiles();
            for (File file: files){
                int match = FuzzySearch.partialRatio(this.lesson + " " + this.activity, file.getName());
                this.guesses.add(new Guess(file, match));
            }
            Collections.sort(this.guesses);
        }
        return this.guesses;
    }
}
