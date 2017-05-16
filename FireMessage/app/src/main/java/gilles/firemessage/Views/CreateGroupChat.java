package gilles.firemessage.Views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import gilles.firemessage.Adapters.MessageAdapter;
import gilles.firemessage.Adapters.UsersAdapter;
import gilles.firemessage.Constants;
import gilles.firemessage.Models.GroupChat;
import gilles.firemessage.Models.Message;
import gilles.firemessage.Models.User;
import gilles.firemessage.R;

public class CreateGroupChat extends AppCompatActivity {

    private static final String TAG = "CreateGroupChatActivity";

    private EditText editgroup;
    private Button btngroup;
    private ListView usersList;

    private ArrayList<User> users = new ArrayList<>();

    private ArrayList<User> addedUsers = new ArrayList<>();

    private Context ctx = this;

    private DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference usersref = rootref.child(Constants.USERS_LOCATION);
    private DatabaseReference chatsref = rootref.child(Constants.CHAT_LOCATION);

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_chat);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Create group");

        editgroup = (EditText) findViewById(R.id.editTextgroup);
        btngroup = (Button) findViewById(R.id.buttonCreateGroup);
        usersList = (ListView) findViewById(R.id.ListViewUsers);


        btngroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editgroup.getText().toString().equals("") && user != null) {
                    //create the group or send back to main activity
                    Intent intent = new Intent();
                    intent.putExtra("grouptext", editgroup.getText().toString());
                    User currentUser = new User(user.getUid(),user.getEmail());
                    addedUsers.add(currentUser);

                    String id = chatsref.push().getKey();
                    GroupChat groupchat = new GroupChat(id,editgroup.getText().toString());
                    chatsref.child(id).setValue(groupchat);
                    chatsref.child(id).child("users").setValue(addedUsers);

                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            }
        });

        usersref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    User post = postSnapshot.getValue(User.class);
                    if(user != null) {
                        if (!post.getEmail().equals(user.getEmail())) {
                            users.add(post);
                        }
                    }
                }

                UsersAdapter adapter = new UsersAdapter(ctx,users);
                usersList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User clickedUser = (User) adapterView.getItemAtPosition(i);

                for (User usr : addedUsers) {
                    if(usr.getUid().equals(clickedUser.getUid())) {
                        addedUsers.remove(clickedUser);
                        view.findViewById(R.id.addImageView).setBackgroundResource(R.drawable.add);
                        return;
                    }
                }
                view.findViewById(R.id.addImageView).setBackgroundResource(R.drawable.remove);
                addedUsers.add(clickedUser);
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

    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
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
}
