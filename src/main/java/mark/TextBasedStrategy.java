package mark;

import Entity.AlarmDO;
import Tool.FileTool;

import java.util.List;
import java.util.Map;

public class TextBasedStrategy implements Strategy{
    @Override
    public void label(Map<String, List<AlarmDO>> targetMap, Map<String, List<AlarmDO>> standardMap) {
        for (String filename : targetMap.keySet()) {
            // 待标记文件的警告
            List<AlarmDO> targetDOs = targetMap.get(filename);
            // 对标文件中的警告
            List<AlarmDO> standardDOs = standardMap.get(filename);
            if (standardDOs == null) {
                continue;
            }
            for (AlarmDO targetDO : targetDOs) {
                for (AlarmDO standardDO : standardDOs) {
                    if (isSame(targetDO , standardDO)) {
                        targetDO.setPositive(false);
                        break;
                    }
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
        System.out.println("textbased " + targetDO.getClazz()+ " " + standardDO.getClazz() + " " + targetContents.retainAll(standardContents));
        return targetContents.retainAll(standardContents);
    }
}
