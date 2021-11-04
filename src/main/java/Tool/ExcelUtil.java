package Tool;

import Entity.AlarmVO;
import Entity.BugInfo;
import joanacore.datastructure.Func;
import joanacore.datastructure.Location;
import jxl.read.biff.BiffException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {
    // 带标题写入Excel
    public static <T> void writeExcelWithTitle(List<T> beans, String path) {
        writeExcel(beans,path,true);
    }

    // 带标题和sheet名写入excel
    public static <T> void writeExcelWithTitleAndSheet(List<T> beans, String path , String sheetName) {
        writeExcel(beans,path,true , sheetName);
    }

    // 仅把数据写入Excel
    public static <T> void writeExcel(List<T> beans, String path) {
        writeExcel(beans,path,false);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

    private static <T> void writeExcel(List<T> beans, String path, boolean writeTitle) {
        if(beans == null || beans.size() == 0) return;
        Workbook workbook = new HSSFWorkbook();
        FileOutputStream fos = null;
        int offset = writeTitle ? 1 : 0;
        try {
            Sheet sheet = workbook.createSheet();
            for (int i = 0; i < beans.size() + offset; ++i) {
                if(writeTitle && i == 0) {createTitle(beans, sheet);continue;}
                Row row = sheet.createRow(i);
                T bean = beans.get(i - offset);
                Field[] fields = bean.getClass().getDeclaredFields();
                for (int j = 0; j < fields.length; j++) {
                    Field field = fields[j];
                    field.setAccessible(true);
                    Cell cell = row.createCell(j);
                    //Date,Calender都可以 使用  +"" 操作转成字符串
                    cell.setCellValue(field.get(bean)+"");
                }
            }
            fos = new FileOutputStream(path);
            workbook.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static <T> void writeExcel(List<T> beans, String path, boolean writeTitle ,String sheetName) {
        if(beans == null || beans.size() == 0) return;
        Workbook workbook = new HSSFWorkbook();
        FileOutputStream fos = null;
        int offset = writeTitle ? 1 : 0;
        try {
            Sheet sheet = workbook.createSheet();
            workbook.setSheetName( workbook.getNumberOfSheets() - 1 ,sheetName);
            for (int i = 0; i < beans.size() + offset; ++i) {
                if(writeTitle && i == 0) {createTitle(beans, sheet);continue;}
                Row row = sheet.createRow(i);
                T bean = beans.get(i - offset);
                Field[] fields = bean.getClass().getDeclaredFields();
                for (int j = 0; j < fields.length; j++) {
                    Field field = fields[j];
                    field.setAccessible(true);
                    Cell cell = row.createCell(j);
                    //Date,Calender都可以 使用  +"" 操作转成字符串
                    cell.setCellValue(field.get(bean)+"");
                }
            }
            fos = new FileOutputStream(path);
            workbook.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static  void writeExcelTitle(List<String> beans, String path, List<String> title) {
        if(beans == null || beans.size() == 0) return;
        Workbook workbook = new HSSFWorkbook();
        FileOutputStream fos = null;
        try {
            Sheet sheet = workbook.createSheet();
            create_Title(title , sheet);
            for (int i = 0; i < beans.size(); i ++ ) {
                Row row = sheet.createRow(i + 1);
                String bean = beans.get(i);
                Cell cell = row.createCell(0);
                    //Date,Calender都可以 使用  +"" 操作转成字符串
                cell.setCellValue(bean+"");
            }
            fos = new FileOutputStream(path);
            workbook.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static  void create_Title(List<String> title,Sheet sheet){
        Row row = sheet.createRow(0);
        for (int i = 0; i < title.size() ; i ++) {
            Cell cell = row.createCell(i + 1);
            cell.setCellValue(title.get(i));
        }
    }

    private static <T> void createTitle(List<T> beans,Sheet sheet){
        Row row = sheet.createRow(0);
        T bean = beans.get(0);
        Field[] fields = bean.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            Cell cell = row.createCell(i);
            cell.setCellValue(field.getName());
        }

    }

    public static List<String> readExcel(String file_path) throws IOException, BiffException {
        jxl.Workbook wb =null;
        List<String> stringList = new ArrayList<>();
        InputStream is = new FileInputStream(file_path);
        wb = jxl.Workbook.getWorkbook(is);
        jxl.Sheet sheet = wb.getSheet(0);
        int row_total = sheet.getRows();
        for (int i = 1 ; i < row_total ; i ++) {
            jxl.Cell[] cells = sheet.getRow(i);
            String project_id = cells[0].getContents();
            String bug_id = cells[1].getContents();
            String file_name = cells[5].getContents().split("\\.")[cells[5].getContents().split("\\.").length - 1];
            String file_identifier = project_id + "_" +bug_id + "_" + file_name;
            stringList.add(file_identifier);
            System.out.println(file_identifier);
        }
        return stringList;
    }

    public static List<String> readExcelreal(String file_path) throws IOException, BiffException {
        jxl.Workbook wb =null;
        List<String> stringList = new ArrayList<>();
        InputStream is = new FileInputStream(file_path);
        wb = jxl.Workbook.getWorkbook(is);
        jxl.Sheet sheet = wb.getSheet(0);
        int row_total = sheet.getRows();
        for (int i = 1 ; i < row_total ; i ++) {
            jxl.Cell[] cells = sheet.getRow(i);
            String project_id = cells[0].getContents();
            String bug_id = cells[1].getContents();
            String file_name = cells[6].getContents();
            String file_identifier = project_id + "_" +bug_id + "_" + file_name;
            stringList.add(file_identifier);
            System.out.println(file_identifier);
        }
        return stringList;
    }

    public static List<BugInfo> readBugExcel(String file_path) throws IOException, BiffException {
        jxl.Workbook wb =null;
        List<BugInfo> bugs = new ArrayList<>();
        InputStream is = new FileInputStream(file_path);
        wb = jxl.Workbook.getWorkbook(is);
        jxl.Sheet sheet = wb.getSheet(0);
        int row_total = sheet.getRows();
        for (int i = 1 ; i < row_total ; i ++) {
            jxl.Cell[] cells = sheet.getRow(i);
//            String project_name = cells[1].getContents();
//            String version_id = cells[2].getContents();
            String clazz = cells[5].getContents().replaceAll("/" , "\\.");
            if (clazz.equals("null")) {
                continue;
            }
            clazz = clazz.substring(0 , clazz.length() - 5);
            Func func = new Func(clazz , cells[6].getContents() , cells[7].getContents());
            Location location = new Location(cells[5].getContents(),
                    Integer.parseInt(cells[8].getContents()) , Integer.parseInt(cells[8].getContents()));
            boolean isWarning = Boolean.parseBoolean(cells[9].getContents());
            BugInfo bugInfo = new BugInfo();
            bugInfo.setFunc(func);
            bugInfo.setLocation(location);
            bugInfo.setWarning(isWarning);
            bugs.add(bugInfo);
        }
        return bugs;
    }

    public static List<AlarmVO> getAlarmVO(String filePath) {
        List<AlarmVO> alarmVOs = new ArrayList<>();
        try {
            InputStream is = new FileInputStream(filePath);
            jxl.Workbook wb = jxl.Workbook.getWorkbook(is);
            jxl.Sheet sheet = wb.getSheet(0);
            int row_total = sheet.getRows();
            for (int i = 1 ; i < row_total ; i ++) {
                jxl.Cell[] cells = sheet.getRow(i);
                String packageName = cells[0].getContents();
                String absolutePath = cells[1].getContents();
                String fileName = cells[3].getContents();
                String type = cells[4].getContents();
                String desc = cells[5].getContents();
                String priority = cells[6].getContents();
                String clazz = cells[7].getContents();
                Func func = new Func(clazz , cells[8].getContents() , cells[9].getContents());
                Location location = new Location(cells[2].getContents(),
                        Integer.parseInt(cells[10].getContents()) , Integer.parseInt(cells[11].getContents()));
                boolean isPositive = Boolean.parseBoolean(cells[12].getContents());
                AlarmVO alarmVO = new AlarmVO();
                alarmVO.setPackageName(packageName);
                alarmVO.setAbsolutePath(absolutePath);
                alarmVO.setFileName(fileName);
                alarmVO.setType(type);
                alarmVO.setDesc(desc);
                alarmVO.setPriority(priority);
                alarmVO.setFunc(func);
                alarmVO.setLocation(location);
                alarmVO.setPositive(isPositive);
                alarmVOs.add(alarmVO);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        return alarmVOs;
    }

    public static void main(String[] args) throws IOException, BiffException {
        // scenics这里是自定义景点集合, 不用管
        // 带标题写入excel
//        ExcelUtil.writeExcelWithTitle(scenics,
//                "C:\\Users\\unive\\Documents\\景点信息\\scenics.xlsx");
//
//        // 不带标题写入excel
//        ExcelUtil.writeExcel(scenics,
//                "C:\\Users\\unive\\Documents\\景点信息\\scenicsWithoutTitle.xlsx");
//        ExcelUtil.writeExcelWithTitle(DomTool.handleinfo("D:\\SDK\\findsecbugs-cli-1.10.1\\findsecbugs-cli-1.10.1\\find_sec_bugs_report.xml")
//        , "D:\\find_sec_report.xlsx");
//        readExcel("C:\\Users\\92039\\Documents\\WeChat Files\\wxid_03ft4u1sz80322\\FileStorage\\File\\2020-12\\Defect4j_real_results.xlt");
//        List<String> labels = readExcel("C:\\Users\\92039\\Documents\\WeChat Files\\wxid_03ft4u1sz80322\\FileStorage\\File\\2020-12\\Defect4j_label_results.xls");
//        List<String > reals = readExcelreal("C:\\Users\\92039\\Documents\\WeChat Files\\wxid_03ft4u1sz80322\\FileStorage\\File\\2020-12\\Defect4j_real_results.xlt");
//        List<String> result = new ArrayList<>();
//        for (String label : labels) {
//            for (int i = 0 ; i < reals.size() ; i ++) {
//                if (label.equals(reals.get(i))) {
//                    result.add("true");
//                    break;
//                }
//                if (i == reals.size() - 1)
//                    result.add("false");
//            }
//        }
//        File f = new File("D:\\defects4j_labels.txt");
//        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
//        for (int i = 0 ; i < labels.size() ; i ++) {
//            bw.write(labels.get(i) + " " + result.get(i));
//            bw.newLine();
//            bw.flush();
//        }
//        bw.close();

        readBugExcel("C:\\Users\\92039\\Desktop\\webmagic.xls");
    }

}
