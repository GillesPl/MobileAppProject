package gilles.firemessage.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Objects;

import gilles.firemessage.Models.Message;
import gilles.firemessage.Models.User;
import gilles.firemessage.R;

/**
 * Created by Gilles on 11/04/2017.
 */

public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(@NonNull Context context, ArrayList<Message> resource) {
        super(context,0, resource);
    }

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private User currentuser;


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Message msg = getItem(position);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                currentuser = new User(user.getUid(),user.getEmail());
            }
        };


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_row, parent, false);
        }

        TextView left = (TextView) convertView.findViewById(R.id.lefttext);
        TextView right = (TextView) convertView.findViewById(R.id.righttext);


        //LinearLayout msgline = (LinearLayout) v.findViewById(R.id.msgLine);
        if(Objects.equals(msg.getAuthor().getEmail(), user.getEmail())) {
            left.setText(msg.getMessage());
            right.setText("");
            right.setBackground(null);
        }
        else {
            right.setText(msg.getMessage());

            left.setText("");
            left.setBackground(null);
        }

        return convertView;

    }
}
