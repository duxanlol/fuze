package rs.dvcci.fuze.rest;


import jxl.read.biff.BiffException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.dvcci.fuze.data.Session;
import rs.dvcci.fuze.util.ExcelReader;
import rs.dvcci.fuze.util.FileCrawler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
@Slf4j
public class MainController {

    final ExcelReader excelReader;
    final FileCrawler fileCrawler;

    @GetMapping("/test")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Session> test() throws BiffException, IOException {
        fileCrawler.setDirectoryName("/home/dusan/fuze-root/fuze/Burmese Mixing");
        Map<Integer, List<String>> data = excelReader.readJExcel("/home/dusan/fuze-root/fuze/yes.xls");
        System.out.println(data);
        List<Session> sessions = excelReader.processExcel(data);
        return sessions;
    }
}
