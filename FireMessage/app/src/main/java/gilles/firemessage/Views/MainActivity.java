package gilles.firemessage.Views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Group;
import java.util.ArrayList;

import gilles.firemessage.Adapters.GroupChatAdapter;
import gilles.firemessage.Adapters.MessageAdapter;
import gilles.firemessage.Constants;
import gilles.firemessage.Models.GroupChat;
import gilles.firemessage.Models.Message;
import gilles.firemessage.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ListView messageListView;

    //firebase elements
    private DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference chatsref = rootref.child(Constants.CHAT_LOCATION);

    private FirebaseUser user;

    private ArrayList<GroupChat> groupchats = new ArrayList<>();

    private Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Group chats");

        messageListView = (ListView) findViewById(R.id.ListViewMessages);
        user = FirebaseAuth.getInstance().getCurrentUser();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            //TODO: onclick creates a new group chat
            @Override
            public void onClick(View view) {
                if(user != null) {
                    Intent intent = new Intent(ctx,CreateGroupChat.class);
                    startActivityForResult(intent,Constants.ADDGROUPRESULT);
                }
                else {
                    Toast.makeText(MainActivity.this, "You must be logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });

        chatsref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(user != null) {
                    dataSnapshot.getChildrenCount();
                    // TODO:get chats based on logged on user
                    // TODO:Maak performanter!!!
                    groupchats.clear();
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        GroupChat post = postSnapshot.getValue(GroupChat.class);
                        //TODO: add when user is subscribed to chat
                        groupchats.add(post);
                        Log.e("Get Data", post.toString());
                    }

                    GroupChatAdapter adapter = new GroupChatAdapter(ctx, groupchats);
                    messageListView.setAdapter(adapter);
                }
                else {
                    Toast.makeText(MainActivity.this, "You must be logged in", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO: toast with database error
            }
        });

        messageListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if(user != null) {
                            GroupChat currentChat = (GroupChat) adapterView.getItemAtPosition(i);
                            Intent messagesintent = new Intent(view.getContext(), MessageActivity.class);
                            messagesintent.putExtra(Constants.GROUPID,currentChat.getId());
                            startActivity(messagesintent);

                        }
                        else {
                            Toast.makeText(MainActivity.this, "You must be logged in", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.ADDGROUPRESULT) {
            if (resultCode == RESULT_OK) {
                if(user != null) {
                    String groupname = data.getStringExtra("grouptext");
                    Toast.makeText(MainActivity.this,"Group " + groupname + "has been made",Toast.LENGTH_SHORT).show();
                }
                else {
                    //TODO: make toast where it says you have to be logged in
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsintent = new Intent(this ,LoginActivity.class);
                startActivity(settingsintent);
                return true;
            case R.id.action_friends:
                //TODO:action friends intent
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
