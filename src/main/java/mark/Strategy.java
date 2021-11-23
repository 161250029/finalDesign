package mark;

import entity.AlarmDO;

import java.util.List;
import java.util.Map;

public interface Strategy {
    /**
     * 标记策略
     * @param targetMap
     * @param standardMaps
     * @return
     */
    public void label(Map<String , List<AlarmDO>> targetMap , List<Map<String , List<AlarmDO>>> standardMaps);
}
