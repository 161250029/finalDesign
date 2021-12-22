package tool.entity;

public class Entity {

    private String projectName;
    private int count;

    public Entity(String projectName, int count) {
        this.projectName = projectName;
        this.count = count;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
