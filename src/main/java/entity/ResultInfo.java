package entity;

public class ResultInfo {
    private String type;
    private String clazz;
    private String method;
    private int location;

    public ResultInfo(String type, String clazz, String method, int location) {
        this.type = type;
        this.clazz = clazz;
        this.method = method;
        this.location = location;
    }

    public ResultInfo() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }
}
