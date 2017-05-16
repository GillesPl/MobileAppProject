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
import java.util.HashMap;
import java.util.Iterator;

import gilles.firemessage.Adapters.GroupChatAdapter;
import gilles.firemessage.Adapters.MessageAdapter;
import gilles.firemessage.Constants;
import gilles.firemessage.Models.GroupChat;
import gilles.firemessage.Models.Message;
import gilles.firemessage.Models.User;
import gilles.firemessage.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ListView messageListView;

    //firebase elements
    private DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference chatsref = rootref.child(Constants.CHAT_LOCATION);

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseUser user;
    private ArrayList<GroupChat> groupchats = new ArrayList<>();
    private Context ctx = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Firemessage");

        //UI
        messageListView = (ListView) findViewById(R.id.ListViewMessages);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
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

        messageListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if(user != null) {
                            GroupChat currentChat = (GroupChat) adapterView.getItemAtPosition(i);
                            Intent messagesintent = new Intent(view.getContext(), MessageActivity.class);
                            messagesintent.putExtra(Constants.GROUPID,currentChat.getId());
                            messagesintent.putExtra(Constants.GROUPNAME,currentChat.getTitle());
                            startActivity(messagesintent);

                        }
                        else {
                            Toast.makeText(MainActivity.this, "You must be logged in", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    getChats();
                } else {
                    // User is signed out
                    messageListView.setAdapter(null);
                    Toast.makeText(MainActivity.this, "User is signed out", Toast.LENGTH_SHORT).show();
                }
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.ADDGROUPRESULT) {
            if (resultCode == RESULT_OK) {
                String groupname = data.getStringExtra("grouptext");
                Toast.makeText(MainActivity.this,"Group " + groupname + " has been made",Toast.LENGTH_SHORT).show();
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void getChats() {
        chatsref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    dataSnapshot.getChildrenCount();
                    groupchats.clear();
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        GroupChat post = postSnapshot.getValue(GroupChat.class);
                        User currentuser = new User(user.getUid(),user.getEmail());
                        ArrayList<User> groupusers = post.getUsers();
                        if(post.getUsers() != null && user != null) {
                            for (int i =0; i< groupusers.size(); i++) {
                                User usr = groupusers.get(i);
                                if(user != null && usr != null) {
                                    if(usr.getUid().equals(user.getUid())){
                                        groupchats.add(post);
                                    }
                                }

                            }
                        }
                    }

                    GroupChatAdapter adapter = new GroupChatAdapter(ctx, groupchats);
                    messageListView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
