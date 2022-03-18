package rs.dvcci.fuze.data;

import lombok.Data;
import lombok.ToString;

import java.io.File;

@Data
@ToString

public class Guess implements Comparable<Guess> {
    File f;
    int match;
    String fileName;

    public Guess(File f, int match){
        this.f = f;
        this.match = match;
        this.fileName = f.getName();

    }

    @Override
    public int compareTo(Guess g) {
        return Integer.compare(g.match, match);
    }
}
