package space.apple.three.memes.firebasedata;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import space.apple.three.memes.model.Meme;

/**
 * Created by ankit on 16/3/18.
 */

public class MemeData {
    FirebaseDatabase database;
    DatabaseReference myRef;

    ArrayList<Meme> arrayList = new ArrayList<>();
    private boolean isDataFetched=false;
    private Context context;
    private String TAG="Ankit";


    public MemeData(Context context){
        this.context = context;
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("memesUrl");

    }

    public boolean isDataFetched() {

        return isDataFetched;
    }

    public ArrayList<Meme> getData(){
        return arrayList;
    }




}
