package Entity;

import joanacore.datastructure.Func;
import joanacore.datastructure.Location;

public class AlarmVO {
    private String packageName;
    private String absolutePath;
    private String fileName;
    private String type;
    private String desc;
    private String priority;
    private Func func;
    private Location location;
    private boolean isPositive;

    public AlarmVO(String packageName, String absolutePath, String fileName, String type, String desc, String priority, Func func, Location location, boolean isPositive) {
        this.packageName = packageName;
        this.absolutePath = absolutePath;
        this.fileName = fileName;
        this.type = type;
        this.desc = desc;
        this.priority = priority;
        this.func = func;
        this.location = location;
        this.isPositive = isPositive;
    }

    public AlarmVO() {
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Func getFunc() {
        return func;
    }

    public void setFunc(Func func) {
        this.func = func;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isPositive() {
        return isPositive;
    }

    public void setPositive(boolean positive) {
        isPositive = positive;
    }
}
