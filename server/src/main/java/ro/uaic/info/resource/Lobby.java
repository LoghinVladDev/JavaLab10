package ro.uaic.info.resource;

import java.util.Objects;

public class Lobby {
    private String creator;
    private String otherPlayer;

    public Lobby(String creator){
        this.creator = creator;
        this.otherPlayer = null;
    }

    public Lobby setOtherPlayer(String otherPlayer){
        this.otherPlayer = otherPlayer;
        return this;
    }

    public String toString(){
        return
            "{Lobby:creator=" +
            this.creator +
            ";otherPlayer=" +
            (this.otherPlayer == null ? "null" : this.otherPlayer) +
            "}";
    }

    public String getCreator() {
        return this.creator;
    }

    public String getOtherPlayer() {
        return this.otherPlayer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lobby lobby = (Lobby) o;
        return Objects.equals(creator, lobby.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creator, otherPlayer);
    }
}
