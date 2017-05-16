package gilles.firemessage.Models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Gilles on 11/04/2017.
 */

public class GroupChat {

    private String id;
    private String title;

    private ArrayList<User> users;
    //private HashMap<String,Boolean> users;
    private String lastMessage;

    public GroupChat(String id,String title) {
        this.title = title;
        this.id = id;
    }

    public GroupChat(String id,String title, String lastMessage) {
        this.title = title;
        this.id = id;
        this.lastMessage = lastMessage;
    }

    public GroupChat(String id, String title, ArrayList<User> users) {
        this.id = id;
        this.title = title;
        this.users = users;
    }


    public GroupChat(String id, String title, ArrayList<User> users, String lastMessage) {
        this.id = id;
        this.title = title;
        this.users = users;
        this.lastMessage = lastMessage;
    }

    public GroupChat() {

    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.title;
    }


    public String getTitle() {
        return this.title;
    }

    public String getLastMessage() {
        return lastMessage;
    }
}
