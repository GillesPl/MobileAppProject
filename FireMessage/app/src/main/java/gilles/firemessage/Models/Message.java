package gilles.firemessage.Models;

/**
 * Created by Gilles on 11/04/2017.
 */

public class Message {

    private String message;
    private User author;

    public Message(String message,User author) {
        this.message = message;
        this.author = author;
    }

    public Message() {

    }


    public String getMessage() {
        return message;
    }

    public User getAuthor() {
        return author;
    }
}
