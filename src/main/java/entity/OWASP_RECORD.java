package entity;

public class OWASP_RECORD {
    boolean tag;
    String filename;

    public OWASP_RECORD(boolean tag, String filename) {
        this.tag = tag;
        this.filename = filename;
    }

    public OWASP_RECORD() {
    }

    public boolean isTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
