package Tool;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCollectTool extends DomTool{

    public List<ReportEntry> getReportEntry(String FilePath) {
        return super.getReportEntries(FilePath);
    }

    public Map<ReportEntry , String> collect(String latestDirPath , String latestFilePath , String otherFilePath) {
        List<ReportEntry> latestEntries = getReportEntry(latestFilePath);

        List<ReportEntry> labelEntries = new ArrayList<>();
        Map<ReportEntry , String> map = new HashMap<>();
        labelEntries.addAll(getReportEntry(otherFilePath));
        for (ReportEntry entry : labelEntries) {
            if (latestEntries.contains(entry)) {
                if (entry.getFileName() == null){
                    continue;
                }
                map.put(entry , "false");
            }
            else {
                if (entry.getFileName() == null ) {
                    continue;
                }
                if (isExisted(latestDirPath , entry.getFileName().split("/")[entry.getFileName().split("/").length - 1])) {
                    map.put(entry , "true");
//                    System.out.println(entry.getFileName().split("/")[entry.getFileName().split("/").length - 1]);
                }
                else {
                    map.put(entry , "unknown");
                    System.out.println(entry.getFileName().split("/")[entry.getFileName().split("/").length - 1]);
                }
            }
        }
        return map;
    }

    public boolean isExisted(String path , String fileName) {
        boolean flag = false;
        File file = new File(path);
        if (file.isFile() && file.getName().contains(fileName)) {
            return true;
        }
        else if (file.isFile() && !file.getName().contains(fileName)) {
            return false;
        }
        else {
            File[] childs = file.listFiles();
            for (File f : childs) {
                if (f.getName().contains(fileName)) {
                    return true;
                }
                else {
                    flag = isExisted(f.getAbsolutePath() , fileName);
                    if (flag) {
                        return flag;
                    }
                }
            }
        }
        return flag;
    }

    public void writeToexcel(String latestDirPath ,String latestFilePath , String otherFilePath , String storePath) {
        Map<ReportEntry , String> map = collect(latestDirPath , latestFilePath , otherFilePath);
        List<CollectResultEntry> collectResultEntries = new ArrayList<>();
        for (Map.Entry<ReportEntry , String> entry : map.entrySet()) {
            collectResultEntries.add(new CollectResultEntry(entry.getKey() , entry.getValue()));
        }
        ExcelUtil.writeExcelWithTitle(collectResultEntries , storePath);
    }

    public static void main(String[] args) {

        String latestFilePath = "D:\\DataSet\\commons-collections\\commons-collections-14ff6fae576085767045bff074c35f2b0220b317\\commons-collections-14ff6fae576085767045bff074c35f2b0220b317\\report.xml";
        String latestDirPath = "D:\\DataSet\\commons-collections\\commons-collections-14ff6fae576085767045bff074c35f2b0220b317\\commons-collections-14ff6fae576085767045bff074c35f2b0220b317";
        String FilePath = "D:\\DataSet\\commons-collections\\commons-collections-ea9b4b51b0811114c006e18c841f0493ee826014\\commons-collections-ea9b4b51b0811114c006e18c841f0493ee826014\\report.xml";
        String storePath = "D:\\DataSet\\commons-collections\\commons-collections-ea9b4b51b0811114c006e18c841f0493ee826014.xls";
        new DataCollectTool().writeToexcel(latestDirPath , latestFilePath , FilePath , storePath);
    }
}
