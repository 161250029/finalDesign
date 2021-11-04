package Entity;

public class AlarmDO {

    private String packageName;
    private String absolutePath;
    private String sourceFile;
    private String fileName;
    private String type;
    private String desc;
    private String priority;
    private String clazz;
    private String method;
    private String sig;
    private int start;
    private int end;
    private boolean isPositive;

    public AlarmDO(String packageName, String absolutePath, String sourceFile, String fileName, String type, String desc, String priority, String clazz, String method, String sig, int start, int end, boolean isPositive) {
        this.packageName = packageName;
        this.absolutePath = absolutePath;
        this.sourceFile = sourceFile;
        this.fileName = fileName;
        this.type = type;
        this.desc = desc;
        this.priority = priority;
        this.clazz = clazz;
        this.method = method;
        this.sig = sig;
        this.start = start;
        this.end = end;
        this.isPositive = isPositive;
    }

    public AlarmDO() {
        this.isPositive = true;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public boolean isPositive() {
        return isPositive;
    }

    public void setPositive(boolean positive) {
        isPositive = positive;
    }
}
