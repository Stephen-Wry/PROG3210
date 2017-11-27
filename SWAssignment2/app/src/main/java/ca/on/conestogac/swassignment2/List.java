package ca.on.conestogac.swassignment2;

/**
 * Created by Swry6766 on 11/27/2017.
 */

public class List {
    private int id;
    private String name;

    public List () {}

    public List(String name) {
        this.name = name;
    }

    public List(int id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return name;
    }
}