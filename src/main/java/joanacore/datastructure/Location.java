package joanacore.datastructure;


import java.util.Objects;

public class Location {

     public String sourceFile;
     public Integer startLine;
     public Integer endLine;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Objects.equals(sourceFile, location.sourceFile) &&
                Objects.equals(startLine, location.startLine) &&
                Objects.equals(endLine, location.endLine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceFile, startLine, endLine);
    }

    public Location(String sourceFile, Integer startLine, Integer endLine) {
        this.sourceFile = sourceFile;
        this.startLine = startLine;
        this.endLine = endLine;
    }

    public Location() {
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public Integer getStartLine() {
        return startLine;
    }

    public void setStartLine(Integer startLine) {
        this.startLine = startLine;
    }

    public Integer getEndLine() {
        return endLine;
    }

    public void setEndLine(Integer endLine) {
        this.endLine = endLine;
    }

    @Override
    public String toString() {
        return "Location{sourceFile='" + sourceFile + '\'' + ", [" + startLine + ", " + endLine + "]}";
    }

}
