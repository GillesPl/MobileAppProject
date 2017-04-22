package gilles.firemessage.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gilles on 11/04/2017.
 */

public class GroupChat {

    private String id;
    private String title;
    private List<Message> messages;
    private ArrayList<User> users;
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

    public GroupChat() {

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
}
