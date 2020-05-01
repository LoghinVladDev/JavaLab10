package ro.uaic.info.resource;

import java.util.ArrayList;
import java.util.List;

public class MatchmakingResources {
    private List<Lobby> lobbyList;

    public MatchmakingResources(){
        this.lobbyList = new ArrayList<>();
    }

    public void addLobby(Lobby lobby){
        this.lobbyList.add(lobby);
    }

    public List<Lobby> getLobbyList() {
        return this.lobbyList;
    }
}
