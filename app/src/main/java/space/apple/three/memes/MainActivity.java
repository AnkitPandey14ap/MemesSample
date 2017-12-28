package space.apple.three.memes;

import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Ankit";
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    private ArrayList<String> list;
    HashMap<String, String> urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("MemesUrls");



/*
        HashMap<String, String> names = new HashMap<>();

        names.put("John", "John");
        names.put("Tim", "Tim");
        names.put("Sam", "Sam");
        names.put("Ben", "Ben");
*/

//        myRef.setValue(names);



        // Read from the database

        list = new ArrayList<String>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                HashMap<String, String> temp= (HashMap<String, String>) dataSnapshot.getValue();
                urls = temp;

//                ArrayList<String> l = new ArrayList<String>(urls.values());
                list = new ArrayList<String>(urls.values());
                mAdapter.notifyDataSetChanged();
                Log.i(TAG, "onDataChange: notifyDataSetChanged()");

                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter=new MyAdapter(list,MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


/*

        list = new ArrayList<String>();
        list.add("https://scontent.fdel8-1.fna.fbcdn.net/v/t1.0-9/26047176_183185412284140_5898557795865010042_n.jpg?oh=3629c95e285b443f2d848da7d7337fd5&oe=5AF515A6");
        list.add("https://scontent.fdel8-1.fna.fbcdn.net/v/t1.0-9/25443267_969138979900758_5405573714344048029_n.png?oh=473a8a6ed8a0890645909760997e5e11&oe=5AB11B27");
        list.add("http://i.imgur.com/DvpvklR.png");
*/

        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter=new MyAdapter(list,MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);





    }
}
