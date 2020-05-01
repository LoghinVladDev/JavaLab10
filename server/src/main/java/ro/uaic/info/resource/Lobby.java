package ro.uaic.info.resource;

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
}
