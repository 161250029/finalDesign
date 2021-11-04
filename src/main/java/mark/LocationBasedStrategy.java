package mark;

import Entity.AlarmDO;
import Tool.FileTool;

import java.util.List;
import java.util.Map;

public class LocationBasedStrategy implements Strategy{
    public static final int Threshold = 3;
    @Override
    public void label(Map<String, List<AlarmDO>> targetMap, Map<String, List<AlarmDO>> standardMap) {
        targetMap.forEach((targetFile , targetDOs) -> {
            List<AlarmDO> standardDOs = standardMap.get(targetFile);
            for (int i = 0 ; i < targetDOs.size() ; i ++) {
                if (standardDOs == null || i > standardDOs.size() - 1) {
                    break;
                }
                if (isSame(targetDOs.get(i) , standardDOs.get(i))) {
                    targetDOs.get(i).setPositive(false);
                }
            }
        });
    }

    private boolean isSame(AlarmDO targetDO , AlarmDO standardDO) {
        if (!targetDO.getType().equals(standardDO.getType())) {
            return false;
        }
        List<String> targetContents = FileTool.readRangeLine(targetDO.getAbsolutePath() , targetDO.getStart() , targetDO.getEnd());
        List<String> standardContents = FileTool.readRangeLine(standardDO.getAbsolutePath() , standardDO.getStart() , standardDO.getEnd());

        int distance1 = targetDO.getEnd() - targetDO.getStart();
        int distance2 = standardDO.getEnd() - standardDO.getStart();
        // 如果是匹配对
        if (targetContents.retainAll(standardContents)) {
            System.out.println("locationbased " + "匹配对 " + targetDO.getClazz() + " " + standardDO.getClazz() + " " + (distance1 == distance2));
            return distance1 == distance2;
        }
        else {
            System.out.println("locationbased " + "非匹配对 " + targetDO.getClazz() + " " + standardDO.getClazz() + " " + (Math.abs(distance1 - distance2) <= Threshold));
            return Math.abs(distance1 - distance2) <= Threshold;
        }
    }
}
