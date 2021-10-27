package Entity;

public class OWASPINFO {
    private String class_name;
    private int start;
    private int end;
    private int level;
    private String desc;
    private String method;
    private String method_signature;
    private boolean tag;

    public OWASPINFO(String class_name, int start, int end, int level, String desc, String method, String method_signature, boolean tag) {
        this.class_name = class_name;
        this.start = start;
        this.end = end;
        this.level = level;
        this.desc = desc;
        this.method = method;
        this.method_signature = method_signature;
        this.tag = tag;
    }

    public OWASPINFO() {
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod_signature() {
        return method_signature;
    }

    public void setMethod_signature(String method_signature) {
        this.method_signature = method_signature;
    }

    public boolean isTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }

    public String toString() {
        return this.class_name + " " + this.desc + " " + this.level+  " " + this.start;
    }
}
