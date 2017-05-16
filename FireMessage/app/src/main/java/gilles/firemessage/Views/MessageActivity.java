package gilles.firemessage.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Objects;

import gilles.firemessage.Constants;
import gilles.firemessage.Models.Message;
import gilles.firemessage.Models.Notification;
import gilles.firemessage.Models.User;
import gilles.firemessage.R;
import gilles.firemessage.Services.MyFirebaseMessagingService;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivity";

    //UI elements
    private RecyclerView messageListView;
    private ListView LmessageListView;
    private Button sendMsgButton;
    private EditText textMsg;
    private LinearLayoutManager mLinearLayoutManager;

    ArrayList<Message> msgs = new ArrayList<Message>();;
    private FirebaseListAdapter<Message> mMessageListAdapter;
    //private FirebaseRecyclerAdapter<Message,MessageViewHolder> mMessageRecylceradapter;

    //firebase elements
    private DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference messagesref = rootref.child(Constants.MESSAGE_LOCATION);
    private DatabaseReference groupref = rootref.child(Constants.CHAT_LOCATION);
    private DatabaseReference notifref = rootref.child(Constants.NOTIFICATIONS_LOCATION);

    private FirebaseUser user;

    private String groupid = null;

    private Context ctx = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        groupid = extras.getString(Constants.GROUPID);
        final DatabaseReference groupMessagesRef = messagesref.child(groupid);

        setContentView(R.layout.activity_message);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        setTitle(extras.getString(Constants.GROUPNAME));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);

        user = FirebaseAuth.getInstance().getCurrentUser();

        //UI elements


        sendMsgButton = (Button) findViewById(R.id.sendButton);
        textMsg = (EditText) findViewById(R.id.messageToSend);

        LmessageListView = (ListView) findViewById(R.id.ListViewMessages);
        if(groupid != null) {
            mMessageListAdapter = new FirebaseListAdapter<Message>(this,Message.class,R.layout.message_row,groupMessagesRef) {
                @Override
                protected void populateView(View v, Message msg, int position) {
                    TextView left = (TextView) v.findViewById(R.id.lefttext);
                    TextView right = (TextView) v.findViewById(R.id.righttext);
                    TextView leftsub = (TextView) v.findViewById(R.id.leftsubtext);
                    TextView rightsub = (TextView) v.findViewById(R.id.rightsubtext);
                    RelativeLayout leftLayout = (RelativeLayout) v.findViewById(R.id.leftlayout);
                    RelativeLayout rightLayout = (RelativeLayout) v.findViewById(R.id.rightLayout);

                    left.setTextSize(17);
                    right.setTextSize(17);
                    leftsub.setTextSize(10);
                    rightsub.setTextSize(10);
                    leftsub.setTextColor(Color.rgb(229,230,230));
                    rightsub.setTextColor(Color.rgb(163,173,194));

                        if(!Objects.equals(msg.getAuthor().getEmail(), user.getEmail())) {
                            left.setText(msg.getMessage());
                            leftLayout.setBackground(getDrawable(R.drawable.rounded_corner_primary));
                            left.setTextColor(Color.WHITE);
                            leftsub.setText(msg.getAuthor().getEmail().toString());
                            right.setText("");
                            rightsub.setText("");
                            rightLayout.setBackground(null);
                        }
                        else {
                            right.setText(msg.getMessage());
                            rightLayout.setBackground(getDrawable(R.drawable.rounded_corner_white));
                            rightsub.setText(msg.getAuthor().getEmail().toString());
                            leftsub.setText("");
                            left.setText("");
                            leftLayout.setBackground(null);
                        }

                    }
            };
        }

        LmessageListView.setAdapter(mMessageListAdapter);


        /*messageListView = (RecyclerView) findViewById(R.id.ListViewMessages);
        if(groupid != null) {
            mMessageRecylceradapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(Message.class,R.layout.message_row,MessageViewHolder.class,groupMessagesRef) {
                @Override
                protected void populateViewHolder(MessageViewHolder v, Message msg, int position) {
                    v.left.setTextSize(17);
                    v.right.setTextSize(17);
                    v.leftsub.setTextSize(10);
                    v.rightsub.setTextSize(10);
                    v.leftsub.setTextColor(Color.rgb(229,230,230));
                    v.rightsub.setTextColor(Color.rgb(163,173,194));

                    if(!Objects.equals(msg.getAuthor().getEmail(), user.getEmail())) {
                        v.left.setText(msg.getMessage());
                        v.leftLayout.setBackground(getDrawable(R.drawable.rounded_corner_primary));
                        v.left.setTextColor(Color.WHITE);
                        v.leftsub.setText(msg.getAuthor().getEmail().toString());
                        v.right.setText("");
                        v.rightsub.setText("");
                        v.rightLayout.setBackground(null);
                    }
                    else {
                        v.right.setText(msg.getMessage());
                        v.rightLayout.setBackground(getDrawable(R.drawable.rounded_corner_white));
                        v.rightsub.setText(msg.getAuthor().getEmail().toString());
                        v.leftsub.setText("");
                        v.left.setText("");
                        v.leftLayout.setBackground(null);
                    }
                }
            };
        }
        messageListView.setAdapter(mMessageRecylceradapter);*/

        sendMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (groupid != null && user != null) {
                    sendMessage(groupMessagesRef,groupid);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_messages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_messagesetting:
                if (groupid != null) {
                    Intent settingsintent = new Intent(this ,GroupMessageActivity.class);
                    settingsintent.putExtra(Constants.GROUPID,groupid);
                    startActivity(settingsintent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void sendMessage(DatabaseReference ref, String groupid) {
        String msg = textMsg.getText().toString();
        if(!msg.equals("")) {
            String id = messagesref.push().getKey();
            User u = new User(user.getUid(),user.getEmail());

            //add message to database
            ref.child(id).setValue(new Message(msg,u));
            groupref.child(groupid).child("lastMessage").setValue(msg.toString());


            Notification notifs = new Notification();
            notifs.setUid(user.getUid());
            notifs.setEmail(user.getEmail());
            //The uid and topic are the same. since the topic of this notification is
            // the postId of this post, any subscribed to this post will recieve this
            // notification
            String notikey = notifref.push().getKey();
            notifs.setUid(notikey);
            notifs.setTopic(groupid);
            notifs.setText(msg);
            notifref.child(notikey).setValue(notifs);
            FirebaseMessaging.getInstance().subscribeToTopic(groupid);
            //clear text
            textMsg.setText("");
        }
    }



    /*public static class MessageViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public TextView left;
        public TextView right;
        public TextView leftsub;
        public TextView rightsub;

        public RelativeLayout leftLayout;
        public RelativeLayout rightLayout;


        public MessageViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            left = (TextView) itemView.findViewById(R.id.lefttext);
            right = (TextView) itemView.findViewById(R.id.righttext);

            leftsub = (TextView) itemView.findViewById(R.id.leftsubtext);
            rightsub = (TextView) itemView.findViewById(R.id.rightsubtext);

            leftLayout = (RelativeLayout) itemView.findViewById(R.id.leftlayout);
            rightLayout = (RelativeLayout) itemView.findViewById(R.id.rightLayout);

        }
    }*/




}