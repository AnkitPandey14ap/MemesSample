package space.apple.three.memes.firebasedata;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import space.apple.three.memes.model.Meme;

import static android.content.ContentValues.TAG;

/**
 * Created by ankit on 16/3/18.
 */

public class MemeData {
    FirebaseDatabase database;
    DatabaseReference myRef;

    ArrayList<Meme> arrayList = new ArrayList<>();

    public MemeData(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("MemesUrls");

    }

    public ArrayList<Meme> getData() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


//                HashMap<String, String> temp = (HashMap<String, String>) dataSnapshot.getValue();

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child:children) {
                    Meme meme = child.getValue(Meme.class);
                    arrayList.add(meme);
                    //Log.i(TAG, "onDataChange: "+child);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return arrayList;
    }




}
