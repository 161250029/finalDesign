package entity;


import joanaCore.datastructure.Func;
import joanaCore.datastructure.Location;

public class OWASP {
    Func function;
    Location location;
    String sourcepath;
    String sourcefile;
    String type;
    String category;
    int level;

    public OWASP(Func function, Location location, String sourcepath, String sourcefile, String type, String category, int level) {
        this.function = function;
        this.location = location;
        this.sourcepath = sourcepath;
        this.sourcefile = sourcefile;
        this.type = type;
        this.category = category;
        this.level = level;
    }

    public OWASP() {
    }

    public Func getFunction() {
        return function;
    }

    public void setFunction(Func function) {
        this.function = function;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getSourcepath() {
        return sourcepath;
    }

    public void setSourcepath(String sourcepath) {
        this.sourcepath = sourcepath;
    }

    public String getSourcefile() {
        return sourcefile;
    }

    public void setSourcefile(String sourcefile) {
        this.sourcefile = sourcefile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
