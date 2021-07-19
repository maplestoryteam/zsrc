
package client;

public class CharacterNameAndId {

    private final int id;
    private final int level;
    private final int job;
    private final String name;
    private final String group;

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
