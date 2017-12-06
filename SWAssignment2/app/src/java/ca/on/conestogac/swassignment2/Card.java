package ca.on.conestogac.swassignment2;

public class Card {
    private int id;
    private int value;
    private String type;

    public Card () {}

    public Card(int id, int value, String type) {
        this.id = id;
        this.value = value;
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}