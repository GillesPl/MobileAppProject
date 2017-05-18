package gilles.firemessage.Views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import gilles.firemessage.Adapters.GroupMessageAdapter;
import gilles.firemessage.Adapters.UsersAdapter;
import gilles.firemessage.Constants;
import gilles.firemessage.Models.GroupChat;
import gilles.firemessage.Models.User;
import gilles.firemessage.R;

public class GroupMessageActivity extends AppCompatActivity {

    private DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference chatref = rootref.child(Constants.CHAT_LOCATION);
    private DatabaseReference userref = rootref.child(Constants.USERS_LOCATION);

    private GroupChat groupchat;

    private String groupid = null;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    ArrayList<User> users = new ArrayList<User>();

    private EditText groupTitle;
    private ListView usersList;
    private Button buttonSave;
    private Button buttonLeave;

    private Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //UI
        groupTitle = (EditText) findViewById(R.id.editTextGroupName);
        usersList = (ListView) findViewById(R.id.ListViewGroupUsers);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonLeave = (Button) findViewById(R.id.buttonLeave);

        Bundle extras = getIntent().getExtras();
        groupid = extras.getString(Constants.GROUPID);

        chatref.child(groupid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groupchat = dataSnapshot.getValue(GroupChat.class);
                groupTitle.setText(groupchat.getTitle());
                setTitle(groupchat.getTitle());
                ArrayList<User>users = new ArrayList<User>();
                if(groupchat.getUsers() != null) {
                    for (User usr : groupchat.getUsers()) {
                        if(usr != null) {
                            users.add(usr);
                        }
                    }
                }


                GroupMessageAdapter adapter = new GroupMessageAdapter(ctx,users);
                usersList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //Button actions
        buttonSaveAction();
        buttonLeaveAction();

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void buttonSaveAction() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatref.child(groupid).child("title").setValue(groupTitle.getText().toString());
                Snackbar.make(view,"Settings have been saved",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
            }
        });
    }

    private void buttonLeaveAction() {
        buttonLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatref.child(groupid).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<User> users = new ArrayList<User>();
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            User usr = postSnapshot.getValue(User.class);
                            if(usr.getUid().equals(user.getUid())) {
                                chatref.child(groupid).child("users").child(postSnapshot.getKey()).removeValue();
                                Intent mainact = new Intent();
                                mainact.setClass(ctx, MainActivity.class);
                                startActivity(mainact);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
