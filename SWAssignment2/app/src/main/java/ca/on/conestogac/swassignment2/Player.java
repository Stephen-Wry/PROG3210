package ca.on.conestogac.swassignment2;

public class Player {
    private int id;
    private String name;
    private String password;
    private int level;
    private int xp;
    private int cash;

    public Player () {}

    public Player(int id, String name, String password, int level, int xp, int cash) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.xp = xp;
        this.cash = cash;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getXp() {
        return xp;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public int getCash() {
        return cash;
    }
}