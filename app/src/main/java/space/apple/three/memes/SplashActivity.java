package space.apple.three.memes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "Ankit";
    FirebaseDatabase database;
    DatabaseReference myRef;

    private ProgressBar progressBar2;

    public static ArrayList<String> keyList;
    public static HashMap<String, String> urls;

    private boolean isDataFetched=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar2 = findViewById(R.id.progressBar2);
        urls = new HashMap<>();

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("MemesUrls");


/*
        MemeData memeData=new MemeData();
        memeData.getData();

        if(!isDataFetched){
            isDataFetched = true;
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();

        }
*/

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

                    //ref #6 key is a string "6" so it might create error so just remove it from the list
                    temp.remove("\"1\"");

                    urls = temp;
                    keyList = new ArrayList<String>(temp.keySet());

//                    Collections.sort(keyList);

                    Collections.sort(keyList, new Comparator<String>() {
                        @Override
                        public int compare(String product, String t1) {
                            if(Integer.parseInt(product)>=Integer.parseInt(t1)){
                                return -1;
                            }
                            return 1;
                        }
                    });

                    if(!isDataFetched){
                        isDataFetched = true;
                        startActivity(new Intent(SplashActivity.this,MainActivity.class));
                        finish();

                    }
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
}
