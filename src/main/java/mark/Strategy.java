package mark;

import Entity.AlarmDO;

import java.util.List;
import java.util.Map;

public interface Strategy {
    /**
     * 标记策略
     * @param targetMap
     * @param standardMap
     * @return
     */
    public void label(Map<String , List<AlarmDO>> targetMap , Map<String , List<AlarmDO>> standardMap);
}
