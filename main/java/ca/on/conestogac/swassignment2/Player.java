package ca.on.conestogac.swassignment2;

/**
 * Created by Swry6766 on 11/24/2017.
 */

public class Player {
    private int id;
    private String name;
    private String password;
    private int level;

    public Player () {}

    public Player(int id, String name, String password, int level) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
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

    public String gePassword() {
        return password;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}