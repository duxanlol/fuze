package rs.dvcci.fuze.data;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import rs.dvcci.fuze.util.FileCrawler;

import java.util.ArrayList;
import java.util.List;
@Data
@ToString
public class Session {

    int id;
    List<Sector> sectors;

    public Session(){
        sectors = new ArrayList<>();
    }
}
