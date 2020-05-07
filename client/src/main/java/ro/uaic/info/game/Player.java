package ro.uaic.info.game;

public class Player {
    private String colour;

    public Player(){

    }

    public Player setColour(String colour) {
        this.colour = colour;
        return this;
    }

    public String getColour() {
        return this.colour;
    }
}
