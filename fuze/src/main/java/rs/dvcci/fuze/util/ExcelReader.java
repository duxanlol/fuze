package rs.dvcci.fuze.util;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.dvcci.fuze.data.Sector;
import rs.dvcci.fuze.data.Session;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelReader {

    @Autowired FileCrawler fileCrawler;

    public Map<Integer, List<String>> readJExcel(String fileLocation)
            throws IOException, BiffException {

        Map<Integer, List<String>> data = new HashMap<>();

        Workbook workbook = Workbook.getWorkbook(new File(fileLocation));
        Sheet sheet = workbook.getSheet(0);
        int rows = sheet.getRows();
        int columns = sheet.getColumns();

        for (int i = 0; i < columns; i++) {
            data.put(i, new ArrayList<String>());
            for (int j = 0; j < rows; j++) {
                data.get(i)
                        .add(sheet.getCell(i, j)
                                .getContents());
            }
        }
        return data;
    }

    public List<Session> processExcel(Map<Integer, List<String>> data){
        List<String> lessons = data.get(0);
        List<Session> sessions = new ArrayList<>();
        for(int i : data.keySet()){
            List<String> sessionStringList = data.get(i);
            if(sessionStringList.get(0).equalsIgnoreCase("session")){
                Session currentSession = new Session();
                currentSession.setId(Integer.parseInt(sessionStringList.get(1)));
                for(int j = 2; j < sessionStringList.size(); j++){
                    if (!lessons.get(j).isEmpty() && !sessionStringList.get(j).isEmpty())
                        currentSession.getSectors().add(new Sector(lessons.get(j), sessionStringList.get(j), fileCrawler));
                }
                sessions.add(currentSession);
            }
        }
        return sessions;
    }


    public static void main(String[] args) throws BiffException, IOException {
        ExcelReader excelReader = new ExcelReader();
        Map<Integer, List<String>> data = excelReader.readJExcel("/home/dusan/fuze/yes.xls");
        System.out.println(data);
        excelReader.processExcel(data);

    }
}
