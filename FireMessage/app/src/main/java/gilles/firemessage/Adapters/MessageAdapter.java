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

        if(currentuser == msg.getAuthor() ) {
            convertView.setBackgroundColor(Color.BLUE);
        }
        else {
            convertView.setBackgroundColor(Color.WHITE);
        }
        TextView title = (TextView) convertView.findViewById(R.id.textView_message);
        title.setText(msg.getMessage());


        return convertView;

    }
}
