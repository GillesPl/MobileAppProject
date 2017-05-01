package gilles.firemessage.Views;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

import gilles.firemessage.Adapters.GroupChatAdapter;
import gilles.firemessage.Adapters.MessageAdapter;
import gilles.firemessage.Constants;
import gilles.firemessage.Models.GroupChat;
import gilles.firemessage.Models.Message;
import gilles.firemessage.Models.User;
import gilles.firemessage.R;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivity";

    //UI elements
    private ListView messageListView;
    private Button sendMsgButton;
    private EditText textMsg;

    ArrayList<Message> msgs = new ArrayList<Message>();;
    private FirebaseListAdapter<Message> mMessageListAdapter;

    //firebase elements
    private DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference messagesref = rootref.child(Constants.MESSAGE_LOCATION);
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;


    private Context ctx = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        final String groupid = extras.getString(Constants.GROUPID);
        final DatabaseReference groupMessagesRef = messagesref.child(groupid);

        if(groupid != null) {
            mMessageListAdapter = new FirebaseListAdapter<Message>(this,Message.class,R.layout.message_row,groupMessagesRef) {
                @Override
                protected void populateView(View v, Message msg, int position) {
                    TextView title = (TextView) v.findViewById(R.id.textView_message);
                    //LinearLayout msgline = (LinearLayout) v.findViewById(R.id.msgLine);
                    title.setText(msg.getMessage());
                    if(Objects.equals(msg.getAuthor().getEmail(), user.getEmail())) {
                        title.setBackground(getDrawable(R.drawable.rounded_corner_primary));
                        title.setGravity(Gravity.RIGHT);
                    }
                    else {
                        title.setGravity(Gravity.LEFT);
                        title.setBackground(getDrawable(R.drawable.rounded_corner_white));
                    }
                }
            };
        }

       /* if(groupid != null) {
            groupMessagesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataSnapshot.getChildrenCount();
                    // TODO:Maak performanter!!!
                    msgs.clear();
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Message post = postSnapshot.getValue(Message.class);
                        msgs.add(post);
                        Log.e("Get Data", post.toString());
                    }

                    MessageAdapter adapter = new MessageAdapter(ctx, msgs);
                    messageListView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }*/




        setContentView(R.layout.activity_message);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Messages");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //UI elements
        messageListView = (ListView) findViewById(R.id.ListViewMessages);
        sendMsgButton = (Button) findViewById(R.id.sendButton);
        textMsg = (EditText) findViewById(R.id.messageToSend);


        messageListView.setAdapter(mMessageListAdapter);

        sendMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (groupid != null) {
                    sendMessage(groupMessagesRef);
                }
            }
        });



        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }



    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void sendMessage(DatabaseReference ref) {
        String id = messagesref.push().getKey();
        String msg = textMsg.getText().toString();
        User u = new User(user.getUid(),user.getEmail());

        //add message to database
        ref.child(id).setValue(new Message(msg,u));

        //clear text
        textMsg.setText("");

    }


    private void showMessages() {

    }

}
