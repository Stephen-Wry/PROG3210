package ca.on.conestogac.swassignment2;

public class PlayerLevel {
    private int id;
    private String name;
    private int xp;

    public PlayerLevel () {}

    public PlayerLevel(String name) {
        this.name = name;
    }

    public PlayerLevel(int id, String name, int xp) {
        this.id = id;
        this.name = name;
        this.xp = xp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getXp() {
        return xp;
    }
}