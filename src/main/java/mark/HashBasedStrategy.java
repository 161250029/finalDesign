package mark;

import Entity.AlarmDO;
import Tool.FileTool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HashBasedStrategy implements Strategy{

    public static final int Threshold = 50;
    @Override
    public void label(Map<String, List<AlarmDO>> targetMap, Map<String, List<AlarmDO>> standardMap) {
        List<AlarmDO> targetDOs = new ArrayList<>();
        List<AlarmDO> standardDOs = new ArrayList<>();

        targetMap.forEach((key, value) -> {
            targetDOs.addAll(targetMap.get(key));
        });
        standardMap.forEach((key , value) -> {
            standardDOs.addAll(standardMap.get(key));
        });

        for (AlarmDO targetDO : targetDOs) {
            for (AlarmDO standardDO : standardDOs) {
                if (isSame(targetDO , standardDO)) {
                    targetDO.setPositive(false);
                    break;
                }
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