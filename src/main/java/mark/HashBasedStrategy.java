package mark;

import entity.AlarmDO;
import tool.FileTool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HashBasedStrategy implements Strategy{

    public static final int Threshold = 50;
    @Override
    public void label(Map<String, List<AlarmDO>> targetMap, List<Map<String, List<AlarmDO>>> standardMaps) {
        List<AlarmDO> targetDOs = new ArrayList<>();
        targetMap.forEach((key, value) -> {
            targetDOs.addAll(targetMap.get(key));
        });
        for (AlarmDO targetDO : targetDOs) {
            int count = 0;
            for (Map<String, List<AlarmDO>> standardMap : standardMaps) {
                List<AlarmDO> standardDOs = new ArrayList<>();
                standardMap.forEach((key , value) -> {
                    standardDOs.addAll(standardMap.get(key));
                });
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

    private boolean isSame(AlarmDO targetDO , AlarmDO standardDO) {
        if (!targetDO.getType().equals(standardDO.getType())) {
            return false;
        }
        List<String> targetContents = FileTool.read(targetDO.getAbsolutePath());
        List<String> standardContents = FileTool.read(standardDO.getAbsolutePath());

        List<String> preTargetContents = targetContents.subList(0 , targetDO.getStart());
        List<String> postTargetContents = targetContents.subList(targetDO.getEnd() - 1 , targetContents.size());

        List<String> preStandardContents = standardContents.subList(0 , standardDO.getStart());
//        try {
//            preStandardContents = standardContents.subList(0 , standardDO.getStart());
//        }catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(targetDO.getClazz() + "_" + targetDO.getStart() +
//                    "_" + targetDO.getEnd() + "_" +targetDO.getPackageName() + "_" + targetDO.getAbsolutePath());
//            System.out.println(standardDO.getClazz() + "_" + standardDO.getStart() +
//                    "_" + standardDO.getEnd() + "_" +standardDO.getPackageName() + "_" +standardDO.getAbsolutePath());
//            System.out.println(targetContents.size() + "_" + standardContents.size());
//        }

        List<String> postStandardContents = standardContents.subList(standardDO.getEnd() - 1 , standardContents.size());

        List<String> preTargetTokens = new ArrayList<>();
        List<String> postTargetTokens = new ArrayList<>();
        List<String> preStandardTokens = new ArrayList<>();
        List<String> postStandardTokens = new ArrayList<>();
        preTargetContents.forEach(targetContent ->{
            String[] tokens = targetContent.split(" ");
            preTargetTokens.addAll(Arrays.asList(tokens));
        });
        postTargetContents.forEach(targetContent -> {
            String[] tokens = targetContent.split(" ");
            postTargetTokens.addAll(Arrays.asList(tokens));
        });

        preStandardContents.forEach(standardContent -> {
            String[] tokens = standardContent.split(" ");
            preStandardTokens.addAll(Arrays.asList(tokens));
        });

        postStandardContents.forEach(standardContent -> {
            String[] tokens = standardContent.split(" ");
            postStandardTokens.addAll(Arrays.asList(tokens));
        });
        boolean flag1 = false;
        boolean flag2 = false;
        if (preTargetTokens.size() >= Threshold && preStandardTokens.size() >= Threshold) {
            flag1 = preTargetTokens.subList(preTargetTokens.size() - Threshold ,preTargetTokens.size())
                    .retainAll(preStandardTokens.subList(preStandardTokens.size() - Threshold ,preStandardTokens.size()));
        }
        if (postTargetTokens.size() >= Threshold && postStandardTokens.size() >= Threshold) {
            flag2 = postTargetTokens.subList(0 , Threshold).retainAll(postStandardTokens.subList(0 , Threshold));
        }
        System.out.println("hashbased " + targetDO.getClazz()+ " " + standardDO.getClazz() + " " + (flag1 || flag2));
        return flag1 || flag2;
    }
}
