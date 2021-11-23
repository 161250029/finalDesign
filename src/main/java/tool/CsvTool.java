package tool;


import entity.Defects4j_Info;
import joanaCore.datastructure.Func;
import joanaCore.datastructure.Location;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvTool {
    public static List<Defects4j_Info> readCsvFile(String filePath) throws IOException {
        List<Defects4j_Info> defects4j_infos = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
        String line = "";
        br.readLine();
        while ((line = br.readLine()) != null) {
            Defects4j_Info defects4j_info = new Defects4j_Info();
            String[] items= line.split(",");
            String project_id = items[0].trim();
            if (!project_id.equals("Cli") && !project_id.equals("Csv") && !project_id.equals("Jsoup")
            && !project_id.equals("Lang") && ! project_id.equals("Math"))
                continue;
            String bug_id = items[1].trim();
            String clazz = items[5].trim();
            String source_path = items[6].trim();
            String method = items[7].trim();
            String sig = items[8].trim();
            int location = Integer.parseInt(items[9].trim());
            defects4j_info.setProject_id(project_id);
            defects4j_info.setBug_id(bug_id);
            defects4j_info.setFunc(new Func(clazz , method , sig));
            defects4j_info.setLocation(new Location(source_path , location , location));
            defects4j_infos.add(defects4j_info);
        }
        br.close();
        return defects4j_infos;
    }

    public static void main(String[] args) throws IOException {
        String filePath = "C:\\Users\\92039\\Desktop\\result.csv";
        System.out.println(readCsvFile(filePath).size());
    }
}