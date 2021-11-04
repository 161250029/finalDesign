package Tool;

public class CollectResultEntry {

    private String type;

    private String priority;

    private int rank;

    private String pattern;

    private String packageName;

    private String fileName;

    private String methodName;

    private String signature;

    private int lineNumber;

    private String isBug;

    public CollectResultEntry(ReportEntry reportEntry, String isBug) {
        this.type = reportEntry.getType();
        this.priority = reportEntry.getPriority();
        this.rank = reportEntry.getRank();
        this.pattern = reportEntry.getPattern();
        this.packageName = reportEntry.getPackageName();
        this.fileName = reportEntry.getFileName();
        this.methodName = reportEntry.getMethodName();
        this.signature = reportEntry.getSignature();
        this.lineNumber = reportEntry.getLineNumber();
        this.isBug = isBug;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getIsBug() {
        return isBug;
    }

    public void setIsBug(String isBug) {
        this.isBug = isBug;
    }
}
