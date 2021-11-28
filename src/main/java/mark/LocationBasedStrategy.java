package mark;

import entity.AlarmDO;
import tool.FileTool;

import java.util.List;
import java.util.Map;

public class LocationBasedStrategy implements Strategy{
    public static final int Threshold = 3;
    @Override
    public void label(Map<String, List<AlarmDO>> targetMap, List<Map<String, List<AlarmDO>>> standardMaps) {
        targetMap.forEach((targetFile , targetDOs) -> {
            for (int i = 0 ; i < targetDOs.size() ; i ++) {
                int count = 0;
                for (Map<String, List<AlarmDO>> standardMap : standardMaps) {
                    List<AlarmDO> standardDOs = standardMap.get(targetFile);
                    if (standardDOs == null || i > standardDOs.size() - 1) {
                        break;
                    }
                    if (isSame(targetDOs.get(i) , standardDOs.get(i))) {
                        count ++;
                    }
                }
                if (count == standardMaps.size()) {
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
        try {
            if (targetContents.retainAll(standardContents)) {
                System.out.println("locationbased " + "匹配对 " + targetDO.getClazz() + " " + standardDO.getClazz() + " " + (distance1 == distance2));
                return distance1 == distance2;
            }
            else {
                System.out.println("locationbased " + "非匹配对 " + targetDO.getClazz() + " " + standardDO.getClazz() + " " + (Math.abs(distance1 - distance2) <= Threshold));
                return Math.abs(distance1 - distance2) <= Threshold;
            }
        }catch (Exception e) {
            System.out.println(targetDO.getClazz() + "_" + targetDO.getStart() +
                    "_" + targetDO.getEnd() + "_" +targetDO.getPackageName());
            System.out.println(standardDO.getClazz() + "_" + standardDO.getStart() +
                    "_" + standardDO.getEnd() + "_" +standardDO.getPackageName());
            e.printStackTrace();
        }
        return false;
    }
}
