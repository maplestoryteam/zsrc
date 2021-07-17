
package client;

public class CharacterNameAndId {

    private int id, level, job;
    private String name, group;

    public CharacterNameAndId(int id, String name, int level, int job, String group) {
        super();
        this.id = id;
        this.name = name;
        this.level = level;
        this.job = job;
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public int getLevel() {
        return level;
    }

    public int getJob() {
        return job;
    }
}
