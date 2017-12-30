package space.apple.three.memes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "Ankit";
    FirebaseDatabase database;
    DatabaseReference myRef;

    private ProgressBar progressBar2;

    public static ArrayList<String> valueList;
    public static ArrayList<String> keyList;
//    HashMap<String, String> urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar2 = findViewById(R.id.progressBar2);
        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("MemesUrls");




        fetchData();
    }

    private void fetchData() {
        if(isNetworkAvailable()){

            progressBar2.setVisibility(View.VISIBLE);

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    HashMap<String, String> temp= (HashMap<String, String>) dataSnapshot.getValue();
                    temp.remove("\"6\"");


/*
                    TreeMap<String,String> tm = new TreeMap<String, String>(temp);

                    Set set2 = tm.entrySet();
                    Iterator it2 = set2.iterator();

                    while(it2.hasNext()){
                        Map.Entry me2 = (Map.Entry)it2.next();
                        Log.i(TAG, " Key : "+me2.getKey()+" Value : "+me2.getValue()+" \n");
                    }

*/

                    valueList = new ArrayList<String>(temp.values());
                    keyList = new ArrayList<String>(temp.keySet());




                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    progressBar2.setVisibility(View.GONE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                    builder.setTitle("ERROR")
                            .setMessage("Something went wrong")
                            .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    fetchData();
                                }
                            });


                    AlertDialog dialog = builder.create();
                    dialog.show();


                }
            });


        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
            builder.setTitle("ERROR")
                    .setMessage("Check your Internet connection")
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            fetchData();
                        }
                    });


            AlertDialog dialog = builder.create();
            dialog.show();

        }



    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    void sortMapByKey(HashMap<String,String> map){
        TreeMap<String,String> mapSorted = new TreeMap<String, String>(map);


//        mapSorted.forEach((key,value));

        /*mapSorted.forEach((key, value) -> {
            System.out.println(key + ", " + value);
        });*/
    }
}
