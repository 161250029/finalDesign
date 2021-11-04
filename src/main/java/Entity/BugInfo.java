package Entity;

import joanacore.datastructure.Func;
import joanacore.datastructure.Location;

public class BugInfo {
    private String project_name;
    private String version_id;
    private Func func;
    private Location location;
    private boolean isWarning;

    public BugInfo(String project_name, String version_id, Func func, Location location, boolean isWarning) {
        this.project_name = project_name;
        this.version_id = version_id;
        this.func = func;
        this.location = location;
        this.isWarning = isWarning;
    }

    public BugInfo() {
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getVersion_id() {
        return version_id;
    }

    public void setVersion_id(String version_id) {
        this.version_id = version_id;
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

    public boolean isWarning() {
        return isWarning;
    }

    public void setWarning(boolean warning) {
        isWarning = warning;
    }
}
