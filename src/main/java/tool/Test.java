package tool;

import prepareData.Config;
import tool.entity.Entity;

import java.util.*;

public class Test {

    public static void main(String[] args){
        List<String> files = FileTool.findFileNames(Config.sliceFuncDirPath , ".java");
        List<String> projects = new ArrayList<>();
        files.forEach(file -> {
            projects.add(file.split("#")[0]);
        });
        Map<String , Integer> map =  new TreeMap<>();
        for (String project : projects) {
            map.put(project , map.getOrDefault(project , 0) + 1);
        }
        System.out.println(map);
        List<Entity> entities = new ArrayList<>();
        for (String key : map.keySet()) {
            Entity entity = new Entity(key , map.get(key));
            entities.add(entity);
        }
        ExcelUtil.writeExcelWithTitle(entities , "res.xls");
    }

}
