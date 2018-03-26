package space.apple.three.memes;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import space.apple.three.memes.model.Meme;

import static android.content.ContentValues.TAG;

/**
 * Created by ankit on 26/3/18.
 */

public class DataManager {
    FirebaseDatabase database;
    DatabaseReference myRef;
    Meme meme;

    Context context;

    public DataManager(Context context, Meme meme) {
        this.context = context;
        this.meme = meme;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("memesUrl").child(meme.getRef()).child("like");

    }

/*
    int getLikeCount(){

        final String[] c = new String[1];
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                c[0] = String.valueOf(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return Integer.parseInt(c[0]);
    }
*/

    public void increaseLike(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Ankit", "onDataChange: increase"+String.valueOf(dataSnapshot.getValue()));
                increase(String.valueOf(dataSnapshot.getValue()));
                myRef.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void decreaseLike(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Ankit", "onDataChange: "+String.valueOf(dataSnapshot.getValue()));
                decrease(String.valueOf(dataSnapshot.getValue()));
                myRef.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    void increase(String like){
        int r = Integer.parseInt(like)+1;
        like= String.valueOf(r);
        myRef.setValue(like);
    }

    void decrease(String like){
        int r = Integer.parseInt(like)-1;
        like= String.valueOf(r);
        myRef.setValue(like);
    }



}
