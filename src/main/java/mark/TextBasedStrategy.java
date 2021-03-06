package mark;

import entity.AlarmDO;
import tool.FileTool;

import java.util.List;
import java.util.Map;

public class TextBasedStrategy implements Strategy{
    @Override
    public void label(Map<String, List<AlarmDO>> targetMap, List<Map<String, List<AlarmDO>>> standardMaps) {
        for (String filename : targetMap.keySet()) {
            // 待标记文件的警告
            List<AlarmDO> targetDOs = targetMap.get(filename);
            // 对标文件中的警告
            for (AlarmDO targetDO : targetDOs) {
                int count = 0;
                for (Map<String , List<AlarmDO>> standardMap : standardMaps) {
                    List<AlarmDO> standardDOs = standardMap.get(filename);
                    if (standardDOs == null) {
                        continue;
                    }
                    for (AlarmDO standardDO : standardDOs) {
                        if (isSame(targetDO , standardDO)) {
                            count ++;
                            break;
                        }
                    }
                }
                if (count == standardMaps.size()) {
                    targetDO.setPositive(false);
                }
            }
        }
    }

    private boolean isSame(AlarmDO targetDO , AlarmDO standardDO) {
        if (!targetDO.getType().equals(standardDO.getType())) {
            return false;
        }
        List<String> targetContents = FileTool.readRangeLine(targetDO.getAbsolutePath() , targetDO.getStart() , targetDO.getEnd());
        List<String> standardContents = FileTool.readRangeLine(standardDO.getAbsolutePath() , standardDO.getStart() , standardDO.getEnd());
        // 比对两个警告对应的代码是否一致
        if(standardContents == null) {
            return false;
        }

//        try {
//            System.out.println("textbased " + targetDO.getClazz()+ " " + standardDO.getClazz() + " " + targetContents.retainAll(standardContents));
//        }catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(targetDO.getClazz() + "_" + targetDO.getStart() +
//                    "_" + targetDO.getEnd() + "_" +targetDO.getPackageName() + "_" + targetDO.getAbsolutePath());
//            System.out.println(standardDO.getClazz() + "_" + standardDO.getStart() +
//                    "_" + standardDO.getEnd() + "_" +standardDO.getPackageName() + "_" +standardDO.getAbsolutePath());
//        }

        System.out.println("textbased " + targetDO.getClazz()+ " " + standardDO.getClazz() + " " + targetContents.retainAll(standardContents));
        return targetContents.retainAll(standardContents);
    }
}
