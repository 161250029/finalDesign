package service;


import Entity.BugInfo;
import Entity.Defects4j_Info;
import Entity.OWASP;
import Tool.CsvTool;
import Tool.DomTool;
import Tool.ExcelUtil;
import Tool.FileTool;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import joanacore.JoanaSlicer;
import joanacore.datastructure.Func;
import joanacore.datastructure.Location;
import joanacore.exception.SlicerException;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DoOwaspSlice {
    private static String find_bugs_report = "C:\\Users\\92039\\Documents\\WeChat Files\\wxid_03ft4u1sz80322\\" +
            "FileStorage\\File\\2020-12\\report.xml";
    private static String find_sec_report = "D:\\Benchmark-master\\Benchmark-master\\results\\Benchmark_1.2-findsecbugs-v1.4.6-122.xml";

    private static String file_prefix = "D:\\Downloads\\Benchmark-master\\Benchmark-master\\src\\main\\java\\";
    public static void do_owasp_slice(String report_path , String app_jar) throws ClassHierarchyException, IOException, SlicerException {
        List<File> apps = new ArrayList<>();
        apps.add(new File("D:\\Benchmark-master\\Benchmark-master\\target\\benchmark.war"));
        JoanaSlicer slicer = new JoanaSlicer();
        slicer.config(apps,null , null);
        List<OWASP> owaspList = DomTool.getOWASP(report_path);
        for (OWASP owasp : owaspList) {
            String  slice = slicer.computeSliceByteCode(owasp.getFunction() , owasp.getLocation());
            FileTool.write_content("D:\\OwaspByteCodeSlicer\\" + owasp.getSourcefile().split("\\.")[0] + "#" + owasp.getLocation().getStartLine()
                    + ".txt" , slice );
        }
    }

    public static void do_owsap_find_sec_byte_slice() throws ClassHierarchyException, IOException, SlicerException {
        List<File> apps = new ArrayList<>();
        apps.add(new File("D:\\Downloads\\Benchmark-master\\Benchmark-master\\target\\benchmark.war"));
        JoanaSlicer slicer = new JoanaSlicer();
        slicer.config(apps,null , null);
        List<OWASP> owaspList = DomTool.getOWASP(find_sec_report);
        for (OWASP owasp : owaspList) {
            String  slice = slicer.computeSliceByteCode(owasp.getFunction() , owasp.getLocation());
            FileTool.write_content("D:\\ExperimentData\\findsecByteCode\\" + owasp.getSourcefile().split("\\.")[0] + "#" + owasp.getLocation().getStartLine()
                    + ".txt" , slice );
        }
    }

    public static void do_owsap_find_sec_code_slice() throws ClassHierarchyException, IOException, SlicerException {
        List<File> apps = new ArrayList<>();
        apps.add(new File("D:\\Downloads\\Benchmark-master\\Benchmark-master\\target\\benchmark.war"));
        JoanaSlicer slicer = new JoanaSlicer();
        slicer.config(apps,null , null);
        List<OWASP> owaspList = DomTool.getOWASP(find_sec_report);
        for (OWASP owasp : owaspList) {
            List<Integer> slice = slicer.computeSlice(owasp.getFunction() , owasp.getLocation());
            FileTool.write_some_lines(slice , file_prefix + owasp.getSourcepath() ,"D:\\ExperimentData\\findsecCode\\" + owasp.getSourcefile().split("\\.")[0] + "#" + owasp.getLocation().getStartLine()
                    + ".txt" );
        }
    }

    public static void do_defect4j_slice(String result_path) throws ClassHierarchyException, IOException, SlicerException {
        List<Defects4j_Info> defects4j_infos = CsvTool.readCsvFile(result_path);
        for (Defects4j_Info defects4j_info : defects4j_infos) {
            String jar_dir_path = "D:\\ExperimentData\\JAR\\" + defects4j_info.getProject_id()+ "_"+ defects4j_info.getBug_id()
                    + "_"+ "buggy";
            if (FileTool.findPath(jar_dir_path , "jar").size() == 0)
                continue;
            String app_jar = FileTool.findPath(jar_dir_path , "jar").get(0);
            do_defect4j_slice(app_jar , defects4j_info.getFunc() , defects4j_info.getLocation() , defects4j_info.getProject_id()+ "_"+ defects4j_info.getBug_id()
                    + "_"+ defects4j_info.getLocation().getStartLine()+".txt");
        }
    }

    public static void do_defect4j_slice(String app_jar , Func func , Location location , String store_path) throws ClassHierarchyException, IOException, SlicerException {
        List<File> apps = new ArrayList<>();
        apps.add(new File(app_jar));
        JoanaSlicer slicer = new JoanaSlicer();
        slicer.config(apps,null , null);
        try {
            String slice_result = slicer.computeSliceByteCode( func , location);
            FileTool.write_content("D:\\ExperimentData\\Defects4j\\" + store_path , slice_result);
        }catch (Exception e){
            System.out.println(store_path);
        }
    }

    public static void do_slice(String app_jar , List<BugInfo> bugInfos ,String prefix) throws ClassHierarchyException, IOException, SlicerException {
        List<File> apps = new ArrayList<>();
        apps.add(new File(app_jar));
        JoanaSlicer slicer = new JoanaSlicer();
        slicer.config(apps,null , null);
        for (int i = 0; i < bugInfos.size() ; i ++) {
            String store_path = "D:\\DataSet\\commons-codec\\commons-codec-0ec741ff0b60221c47f047d5ca6266d00fc3050c\\"  + bugInfos.get(i).getFunc().getClazz().split("\\.")[
                    bugInfos.get(i).getFunc().getClazz().split("\\.").length - 1] +"_"+bugInfos.get(i).getLocation().startLine + "_" +
                    "_" +bugInfos.get(i).isWarning() + ".java";
                List<Integer> slice = slicer.computeSlice(bugInfos.get(i).getFunc(), bugInfos.get(i).getLocation());
                FileTool.write_some_lines(slice , prefix + bugInfos.get(i).getLocation().getSourceFile() ,store_path);
//                String slice_result = slicer.computeSliceByteCode( bugInfos.get(i).getFunc(), bugInfos.get(i).getLocation());
//                FileTool.write_content("D:\\ExperimentData\\blynk-server\\" + bugInfos.get(i).getVersion_id() + "_" + bugInfos.get(i).getFunc().getClazz().split("\\.")[
//                        bugInfos.get(i).getFunc().getClazz().split("\\.").length - 1] +"_"+bugInfos.get(i).getLocation().startLine + "_" +
//                        "_" +bugInfos.get(i).isWarning() + ".java", slice_result);
        }
    }




    public static void main(String[] args) throws ClassHierarchyException, SlicerException, IOException, BiffException {
//        do_owasp_slice(find_bugs_report,"D:\\Benchmark-master\\Benchmark-master\\target\\benchmark.war");
//        fixbarce("D:\\OwaspSlicer\\BenchmarkTest00001\\74\\BenchmarkTest00001.java");
        //  "D:\\OwaspSlicer\\BenchmarkTest00001\\74\\BenchmarkTest00001.java"

//        do_owsap_find_sec_code_slice();

//        do_defect4j_slice("D:\\Downloads\\jar\\usr\\JAR\\Cli_1_buggy\\commons-cli-1.1-SNAPSHOT.jar" ,
//                new Func("org.apache.commons.cli.HelpFormatter" , "printHelp" , "(ILjava/lang/String;Ljava/lang/String;Lorg/apache/commons/cli/Options;Ljava/lang/String;Z)V") ,
//                new Location("org/apache/commons/cli/HelpFormatter.java" , 343 ,343));
//        do_defect4j_slice("C:\\Users\\92039\\Desktop\\result.csv");

//        do_slice("D:\\project1\\webmagic-WebMagic-0.7.0\\webmagic-WebMagic-0.7.0\\webmagic-core\\target\\webmagic-core-0.7.0.jar" ,
//                ExcelUtil.readBugExcel("C:\\User\\92039\\Desktop\\webmagic.xls"));

        do_slice("D:\\DataSet\\commons-bcel\\commons-bcel-31dcc10240df3b9d0c2abce5610aaaeb04d0b864\\commons-bcel-31dcc10240df3b9d0c2abce5610aaaeb04d0b864\\target\\bcel-6.4.0-SNAPSHOT.jar" ,
                ExcelUtil.readBugExcel("D:\\DataSet\\commons-bcel\\commons-bcel-31dcc10240df3b9d0c2abce5610aaaeb04d0b864.xls")
        ,"D:\\DataSet\\commons-bcel\\commons-bcel-31dcc10240df3b9d0c2abce5610aaaeb04d0b864\\commons-bcel-31dcc10240df3b9d0c2abce5610aaaeb04d0b864\\src\\main\\java\\");
    }
}
