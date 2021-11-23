package tool;

import java.util.Objects;

public class ReportEntry {

    private String type;

    private String priority;

    private int rank;

    private String pattern;

    private String packageName;

    private String fileName;

    private String methodName;

    private String signature;

    private int lineNumber;

    public ReportEntry(String type, String priority, int rank, String pattern, String packageName, String fileName, String methodName, String signature, int lineNumber) {
        this.type = type;
        this.priority = priority;
        this.rank = rank;
        this.pattern = pattern;
        this.packageName = packageName;
        this.fileName = fileName;
        this.methodName = methodName;
        this.signature = signature;
        this.lineNumber = lineNumber;
    }

    public ReportEntry() {
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

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportEntry that = (ReportEntry) o;
        return rank == that.rank &&
                lineNumber == that.lineNumber &&
                Objects.equals(type, that.type) &&
                Objects.equals(priority, that.priority) &&
                Objects.equals(packageName, that.packageName) &&
                Objects.equals(fileName, that.fileName) &&
                Objects.equals(methodName, that.methodName) &&
                Objects.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, priority, rank, packageName, fileName, methodName, signature, lineNumber);
    }
}
