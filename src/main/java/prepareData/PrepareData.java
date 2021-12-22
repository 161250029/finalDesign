package prepareData;

import entity.AlarmDO;
import entity.AlarmVO;
import tool.DomTool;
import tool.ExcelUtil;
import tool.FileTool;
import astCore.SliceHandler;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import joanaCore.JoanaSlicer;
import joanaCore.exception.SlicerException;
import mark.HashBasedStrategy;
import mark.LocationBasedStrategy;
import mark.Strategy;
import mark.TextBasedStrategy;


import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PrepareData {

    private Strategy locationBasedStrategy = new LocationBasedStrategy();

    private Strategy textBasedStrategy = new TextBasedStrategy();

    private Strategy hashBasedStrategy = new HashBasedStrategy();

    private Map<String , List<String>> projectMap = new HashMap<>();

    public static void main(String[] args) {
        new PrepareData().sliceFuncBody();
    }

    private void init() {
        List<String> projectNames = getProjectNames();
        projectNames.forEach(projectName -> {
            projectMap.put(projectName , getVersionNames(Config.dirPath + projectName));
        });
        // 按照时间升序排序，时间早的则是旧版本
        boolean versionFlag = false;
        if (versionFlag) {
            projectMap.forEach((project , versions) -> {
                sort(project , versions);
            });
        }
        else {
            projectMap.forEach((project , versions) -> {
                Collections.sort(versions);
            });
        }
    }

    public void run() {
        init();
        projectMap.forEach((project , versions) -> {
            System.out.println("----标记开始----");
            for (int i = 0 ; i < versions.size() - 1 ; i ++) {
                System.out.println("----" + project + ":" + versions.get(i) + "----");
                String targetVersion = versions.get(i);
                String targetDirPath = Config.dirPath + project + "\\" + targetVersion + "\\" + targetVersion + "\\";
                List<AlarmDO> targetDOs = DomTool.getAlarmDOs(targetDirPath + Config.reportName);
                System.out.println("----计算待标文件路径----");
                targetDOs.forEach(targetDO -> {
                    targetDO.setPackageName(targetVersion);
                    targetDO.setAbsolutePath(FileTool.findFile(targetDirPath , targetDO.getFileName()));
//                    System.out.println(targetDO.getAbsolutePath());
                });
                //
                targetDOs = targetDOs.stream().filter(targetDO -> targetDO.getAbsolutePath() != null).collect(Collectors.toList());
                List<List<AlarmDO>> standardDOLists = new ArrayList<>();
                for(int j = i + 1 ; j < versions.size() ; j ++) {
                    String standardVersion = versions.get(j);
                    String standardDirPath = Config.dirPath + project + "\\" + standardVersion + "\\" + standardVersion + "\\";
                    List<AlarmDO> standardDOs = DomTool.getAlarmDOs(standardDirPath + Config.reportName);
                    System.out.println("----计算基准文件路径----");
                    standardDOs.forEach(standardDO -> {
                        standardDO.setAbsolutePath(FileTool.findFile(standardDirPath , standardDO.getFileName()));
//                        System.out.println(standardDO.getFileName() + "  " + standardDO.getAbsolutePath());
                    });
                    standardDOs = standardDOs.stream().filter(standardDO -> standardDO.getAbsolutePath() != null).collect(Collectors.toList());
                    standardDOLists.add(standardDOs);
                }
                Map<String , List<AlarmDO>> res = mark(targetDOs , standardDOLists);
                List<AlarmDO> alarmDOS = new ArrayList<>();
                res.forEach((file , alarmList) -> {
                    alarmDOS.addAll(alarmList);
                });
                ExcelUtil.writeExcelWithTitle(alarmDOS ,Config.dirPath + project + "\\" + targetVersion + "\\" + targetVersion + "\\" + Config.labelName);
            }
        });
    }

    public void labelNewProject() {
        init();
        projectMap.forEach((project , versions) -> {
            System.out.println("----标记开始----");
            for (int i = 0 ; i < versions.size() - 1 ; i ++) {
                System.out.println("----" + project + ":" + versions.get(i) + "----");
                String projectDirPath = Config.dirPath + project + "\\";
                String targetVersion = versions.get(i);
                String targetDirPath = projectDirPath  + targetVersion + "\\";
                List<AlarmDO> targetDOs = DomTool.getAlarmDOs(projectDirPath + targetVersion + ".xml");
                System.out.println("----计算待标文件路径----");
                targetDOs.forEach(targetDO -> {
                    targetDO.setPackageName(targetVersion);
                    targetDO.setAbsolutePath(FileTool.findFilePath(targetDirPath , targetDO.getSourceFile().replaceAll("/" , "\\\\")));
//                    System.out.println(targetDO.getAbsolutePath());
                });
                //
                targetDOs = targetDOs.stream().filter(targetDO -> targetDO.getAbsolutePath() != null).collect(Collectors.toList());
                List<List<AlarmDO>> standardDOLists = new ArrayList<>();
                for(int j = i + 1 ; j < versions.size() ; j ++) {
                    String standardVersion = versions.get(j);
                    String standardDirPath = projectDirPath + standardVersion +  "\\";
                    List<AlarmDO> standardDOs = DomTool.getAlarmDOs(projectDirPath + standardVersion + ".xml");
                    System.out.println("----计算基准文件路径----");
                    standardDOs.forEach(standardDO -> {
                        standardDO.setPackageName(standardVersion);
                        standardDO.setAbsolutePath(FileTool.findFilePath(standardDirPath , standardDO.getSourceFile().replaceAll("/" , "\\\\")));
//                        System.out.println(standardDO.getFileName() + "  " + standardDO.getAbsolutePath());
                    });
                    standardDOs = standardDOs.stream().filter(standardDO -> standardDO.getAbsolutePath() != null).collect(Collectors.toList());
                    standardDOLists.add(standardDOs);
                }
                Map<String , List<AlarmDO>> res = mark(targetDOs , standardDOLists);
                List<AlarmDO> alarmDOS = new ArrayList<>();
                res.forEach((file , alarmList) -> {
                    alarmDOS.addAll(alarmList);
                });
                ExcelUtil.writeExcelWithTitle(alarmDOS ,Config.dirPath + project + "\\" + targetVersion + "\\" + Config.labelName);
            }
        });
    }

    public void doAddSlice() {
        init();
        projectMap.forEach((project , versions) -> {
            System.out.println("----程序切片开始----");
            versions.forEach(version -> {
                String labelFilePath = Config.dirPath + project + "\\" + version  + "\\" + Config.labelName;
                List<AlarmVO> alarmVOs = ExcelUtil.getAlarmVO(labelFilePath);
                List<String> jars = getJars(Config.dirPath + project + "\\" + version);
                List<File> apps = new ArrayList<>();
                jars.forEach(jar -> {
                    apps.add(new File(jar));
                });
                alarmVOs.forEach(alarmVO -> {
                    try {
                        JoanaSlicer slicer = new JoanaSlicer();
                        slicer.config(apps,null , null);
                        List<Integer> slices = slicer.computeSlice(alarmVO.getFunc(), alarmVO.getLocation());
//                        String slicesContent = SliceHandler.sliceFile(new File(alarmVO.getAbsolutePath()) , alarmVO.getFunc().getMethod() , slices);
                        String content = SliceHandler.sliceFile(new File(alarmVO.getAbsolutePath()) , alarmVO.getFunc().getMethod() ,slices);
                        FileTool.write_content(
                                Config.sliceDirPath +  generateFileName(project + "-" +alarmVO.getPackageName() ,
                                        alarmVO.getFileName().split("\\.")[0] ,
                                        alarmVO.getType(),
                                        alarmVO.getDesc(),
                                        alarmVO.getPriority(),
//                                alarmVO.getFunc().getMethod(),
                                        String.valueOf(alarmVO.getLocation().getStartLine()),
                                        String.valueOf(alarmVO.getLocation().getEndLine()),
                                        String.valueOf(alarmVO.isPositive())) , content);
                    } catch (ClassHierarchyException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SlicerException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(
                                generateFileName(alarmVO.getPackageName() ,
                                        alarmVO.getFileName().split("\\.")[0] ,
                                        alarmVO.getType(),
                                        alarmVO.getDesc(),
                                        alarmVO.getPriority(),
//                                alarmVO.getFunc().getMethod(),
                                        String.valueOf(alarmVO.getLocation().getStartLine()),
                                        String.valueOf(alarmVO.getLocation().getEndLine()),
                                        String.valueOf(alarmVO.isPositive()))
                        );
                    }
                });
            });
        });
    }


    public void doSlice() {
        init();
        projectMap.forEach((project , versions) -> {
            System.out.println("----程序切片开始----");
            versions.forEach(version -> {
                String labelFilePath = Config.dirPath + project + "\\" + version + "\\" + version + "\\" + Config.labelName;
                List<AlarmVO> alarmVOs = ExcelUtil.getAlarmVO(labelFilePath);
                List<String> jars = getJars(Config.dirPath + project + "\\" + version);
                List<File> apps = new ArrayList<>();
                jars.forEach(jar -> {
                    apps.add(new File(jar));
                });
                alarmVOs.forEach(alarmVO -> {
                    try {
                        JoanaSlicer slicer = new JoanaSlicer();
                        slicer.config(apps,null , null);
                        List<Integer> slices = slicer.computeSlice(alarmVO.getFunc(), alarmVO.getLocation());
//                        String slicesContent = SliceHandler.sliceFile(new File(alarmVO.getAbsolutePath()) , alarmVO.getFunc().getMethod() , slices);
                        String content = SliceHandler.sliceFile(new File(alarmVO.getAbsolutePath()) , alarmVO.getFunc().getMethod() ,slices);
                        FileTool.write_content(
                                Config.sliceDirPath +  generateFileName(alarmVO.getPackageName() ,
                                alarmVO.getFileName().split("\\.")[0] ,
                                alarmVO.getType(),
                                alarmVO.getDesc(),
                                alarmVO.getPriority(),
//                                alarmVO.getFunc().getMethod(),
                                String.valueOf(alarmVO.getLocation().getStartLine()),
                                String.valueOf(alarmVO.getLocation().getEndLine()),
                                String.valueOf(alarmVO.isPositive())) , content);
                    } catch (ClassHierarchyException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SlicerException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(
                                generateFileName(alarmVO.getPackageName() ,
                                        alarmVO.getFileName().split("\\.")[0] ,
                                        alarmVO.getType(),
                                        alarmVO.getDesc(),
                                        alarmVO.getPriority(),
//                                alarmVO.getFunc().getMethod(),
                                        String.valueOf(alarmVO.getLocation().getStartLine()),
                                        String.valueOf(alarmVO.getLocation().getEndLine()),
                                        String.valueOf(alarmVO.isPositive()))
                        );
                    }
                });
            });
        });
    }

    public void sliceFuncBody() {
        String sliceDataDirPath = Config.sliceDirPath;
        List<String> sliceDataFilePaths = FileTool.findPath(sliceDataDirPath , "java");
        sliceDataFilePaths.forEach(sliceDataFilePath -> {
            try {
                String funcBody = SliceHandler.getFuncBody(new File(sliceDataFilePath));
                if (funcBody == null) {
                    return;
                }
                FileTool.write_content(Config.sliceFuncDirPath + sliceDataFilePath.split("\\\\")
                [sliceDataFilePath.split("\\\\").length - 1] , funcBody);
            } catch (Exception e) {
                System.out.println(sliceDataFilePath);
                e.printStackTrace();
            }
        });
    }

    private List<String> getJars(String dirPath) {
        return FileTool.findPath(dirPath , "jar");
    }

    /**
     *
     * @param targetDOs 标记版本的警告列表
     * @param standardDOLists 基准版本的警告列表
     * @return
     */
    private Map<String , List<AlarmDO>> mark(List<AlarmDO> targetDOs , List<List<AlarmDO>> standardDOLists) {
        Map<String , List<AlarmDO>> targetMap = new HashMap<>();
        targetDOs.forEach(targetDO -> {
            List<AlarmDO> alarmDOList = targetMap.getOrDefault(targetDO.getClazz() , new ArrayList<>());
            alarmDOList.add(targetDO);
            targetMap.put(targetDO.getClazz(), alarmDOList);
        });

        List<Map<String , List<AlarmDO>>> standardMaps = new ArrayList<>();
        standardDOLists.forEach(standardDOList -> {
            Map<String , List<AlarmDO>> standardMap = new HashMap<>();
            standardDOList.forEach(standardDO -> {
                List<AlarmDO> alarmDOList = standardMap.getOrDefault(standardDO.getClazz() , new ArrayList<>());
                alarmDOList.add(standardDO);
                standardMap.put(standardDO.getClazz() , alarmDOList);
            });
            standardMaps.add(standardMap);
        });
        locationBasedStrategy.label(targetMap , standardMaps);
        textBasedStrategy.label(targetMap , standardMaps);
        hashBasedStrategy.label(targetMap , standardMaps);
        return targetMap;
    }

    private void sort(String project , List<String> versions) {
        Collections.sort(versions, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                File f1 = new File(Config.dirPath + project + "//" + o1 + ".zip");
                File f2 = new File(Config.dirPath + project + "//" + o2 + ".zip");
                long diff = f1.lastModified()-f2.lastModified();
                if(diff>0)
                    return 1;
                else if(diff==0)
                    return 0;
                else
                    return -1;
            }
        });
    }

    private List<String> getVersionNames(String dirPath) {
        return FileTool.getDirName(dirPath);
    }
    /**
     *
     * @return 待处理包名
     */
    private List<String> getProjectNames() {
        return FileTool.getDirName(Config.dirPath);
    }

    private String generateFileName(String... infos) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0 ; i < infos.length - 1 ; i ++) {
            sb.append(infos[i]).append("#");
        }
        sb.append(infos[infos.length - 1]).append(".java");
        return sb.toString();
    }
}
