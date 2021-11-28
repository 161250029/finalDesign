package metricsFeature.model;

public class MetricsFeature {

    private String fileName;

    private int lineNum;

    private int statementNum;

    private int branchStatementNum;

    private int callNum;

    private int cycleComplexity;

    private int depth;

    public MetricsFeature(String fileName, int lineNum, int statementNum, int branchStatementNum, int callNum, int cycleComplexity, int depth) {
        this.fileName = fileName;
        this.lineNum = lineNum;
        this.statementNum = statementNum;
        this.branchStatementNum = branchStatementNum;
        this.callNum = callNum;
        this.cycleComplexity = cycleComplexity;
        this.depth = depth;
    }

    public MetricsFeature() {
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public int getStatementNum() {
        return statementNum;
    }

    public void setStatementNum(int statementNum) {
        this.statementNum = statementNum;
    }

    public int getBranchStatementNum() {
        return branchStatementNum;
    }

    public void setBranchStatementNum(int branchStatementNum) {
        this.branchStatementNum = branchStatementNum;
    }

    public int getCallNum() {
        return callNum;
    }

    public void setCallNum(int callNum) {
        this.callNum = callNum;
    }

    public int getCycleComplexity() {
        return cycleComplexity;
    }

    public void setCycleComplexity(int cycleComplexity) {
        this.cycleComplexity = cycleComplexity;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
