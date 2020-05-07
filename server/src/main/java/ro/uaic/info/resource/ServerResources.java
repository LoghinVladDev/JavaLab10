package ro.uaic.info.resource;

import ro.uaic.info.server.ClientThread;

import java.util.ArrayList;
import java.util.List;

public class ServerResources {
    private List<ClientThread> threadList;

    public ServerResources(){
        this.threadList = new ArrayList<>();
    }

    public void addToThreads(ClientThread thread){
        this.threadList.add(thread);
    }

    public ClientThread getThreadByID(int threadID){
        for(ClientThread thread : this.threadList)
            if(thread.getThreadID() == threadID)
                return thread;
        return null;
    }

    public ClientThread getByThreadLinkedUsername(String username){
        for(ClientThread thread : this.threadList)
            if(thread.getUsername().equals(username))
                return thread;
        return null;
    }

    public List<ClientThread> getThreadList(){
        return this.threadList;
    }
}
