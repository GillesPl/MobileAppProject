package gilles.firemessage.Models;

/**
 * Created by Gilles on 16-May-17.
 */

public class Notification {

    private String email;
    private String uid;
    private String text;
    private String topic;


    public Notification() {

    }


    public Notification(String email, String uid, String text, String topic) {
        this.email = email;
        this.uid = uid;
        this.text = text;
        this.topic = topic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
