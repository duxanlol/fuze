package rs.dvcci.fuze.rest;


import jxl.read.biff.BiffException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.dvcci.fuze.data.Sector;
import rs.dvcci.fuze.data.Session;
import rs.dvcci.fuze.util.ExcelReader;
import rs.dvcci.fuze.util.FileCrawler;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
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
        fileCrawler.setDirectoryName("/home/dusan/fuzedata/Burmese Mixing/");
        Map<Integer, List<String>> data = excelReader.readJExcel("/home/dusan/fuze-root/fuze/yes.xls");
        System.out.println(data);
        List<Session> sessions = excelReader.processExcel(data);
        return sessions;
    }
    public Boolean concatenateFiles(List<String> sourceFilesList, String destinationFileName) throws Exception {
        Boolean result = false;

        AudioInputStream audioInputStream = null;
        List<AudioInputStream> audioInputStreamList = null;
        AudioFormat audioFormat = null;
        Long frameLength = null;

        try {
            // loop through our files first and load them up
            for (String sourceFile : sourceFilesList) {
                audioInputStream = AudioSystem.getAudioInputStream(new File(sourceFile));

                // get the format of first file
                if (audioFormat == null) {
                    audioFormat = audioInputStream.getFormat();
                }

                // add it to our stream list
                if (audioInputStreamList == null) {
                    audioInputStreamList = new ArrayList<>();
                }
                audioInputStreamList.add(audioInputStream);

                // keep calculating frame length
                if(frameLength == null) {
                    frameLength = audioInputStream.getFrameLength();
                } else {
                    frameLength += audioInputStream.getFrameLength();
                }
            }

            // now write our concatenated file
            AudioSystem.write(new AudioInputStream(new SequenceInputStream(Collections.enumeration(audioInputStreamList)), audioFormat, frameLength), AudioFileFormat.Type.WAVE, new File(destinationFileName));

            // if all is good, return true
            result = true;
        } finally {
            if (audioInputStream != null) {
                audioInputStream.close();
            }
            if (audioInputStreamList != null) {
                audioInputStreamList = null;
            }
        }

        return result;
    }
    @PostMapping("/exportSession")
    @CrossOrigin(origins = "http://localhost:4200")
    public boolean exportSession(@RequestBody Session session) throws Exception {
        System.out.println("ZVAO EXPORT SESSION SA SESSION " + session);
        ArrayList<String> zaExport = new ArrayList<>();
        for (Sector sector : session.getSectors()){
            zaExport.add(sector.getPicked().getF().getAbsolutePath());
        }

        return concatenateFiles(zaExport, "session_"+session.getId()+".wav");
    }
}
