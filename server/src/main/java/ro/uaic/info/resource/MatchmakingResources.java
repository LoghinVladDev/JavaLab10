package ro.uaic.info.resource;

import java.util.ArrayList;
import java.util.List;

public class MatchmakingResources {
    private List<Lobby> lobbyList;

    public MatchmakingResources(){
        this.lobbyList = new ArrayList<>();
    }

    public void addLobby(Lobby lobby){
        if(!this.lobbyList.contains(lobby))
            this.lobbyList.add(lobby);
    }

    public void removeLobby(String creator){
        this.lobbyList.removeIf(lobby -> lobby.getCreator().equals(creator));
    }

    public int addToLobby(String creatorUsername, String username){
        for(Lobby lobby : this.lobbyList)
            if(lobby.getCreator().equals(creatorUsername))
                if(lobby.getOtherPlayer() != null){
                    System.out.println(lobby.getOtherPlayer() + " plm ");
                    return 1;
                }
                else {
                    lobby.setOtherPlayer(username);
                    return 0;
                }
        return -1;
    }

    public List<Lobby> getLobbyList() {
        return this.lobbyList;
    }
}
