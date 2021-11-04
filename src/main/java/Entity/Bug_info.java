package Entity;

public class Bug_info {
    private String clazz;
    private String method;
    private String sig;
    private int location;
    private boolean ispositive;

    public Bug_info(String clazz, String method, String sig, int location, boolean ispositive) {
        this.clazz = clazz;
        this.method = method;
        this.sig = sig;
        this.location = location;
        this.ispositive = ispositive;
    }

    public Bug_info() {
        this.ispositive = true;
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

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public boolean isIspositive() {
        return ispositive;
    }

    public void setIspositive(boolean ispositive) {
        this.ispositive = ispositive;
    }
}
