package gilles.firemessage.Models;

/**
 * Created by Gilles on 14/04/2017.
 */

public class User {

    private String uid;
    private String email;
    private String displayName;


    public User(String uid, String email, String displayName) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
    }

    public User(String uid, String email) {
        this.uid = uid;
        this.email = email;
    }

    public User() {
    }



    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }
}
