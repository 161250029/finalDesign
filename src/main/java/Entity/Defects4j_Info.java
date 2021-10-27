package Entity;

import joanacore.datastructure.Func;
import joanacore.datastructure.Location;

public class Defects4j_Info {
    Func func;
    Location location;
    String project_id;
    String bug_id;

    public Defects4j_Info(Func func, Location location, String project_id, String bug_id) {
        this.func = func;
        this.location = location;
        this.project_id = project_id;
        this.bug_id = bug_id;
    }

    public Defects4j_Info() {
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

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getBug_id() {
        return bug_id;
    }

    public void setBug_id(String bug_id) {
        this.bug_id = bug_id;
    }
}
