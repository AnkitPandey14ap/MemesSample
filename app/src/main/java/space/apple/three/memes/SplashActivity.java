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
import java.util.Collections;


import space.apple.three.memes.model.Meme;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "Ankit";
    FirebaseDatabase database;
    DatabaseReference myRef;

    private ProgressBar progressBar2;

    public static ArrayList<String> keyList;
    public static ArrayList<Meme> urls = new ArrayList<>();

    private boolean isDataFetched=false;
    private boolean readyToGo=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar2 = findViewById(R.id.progressBar2);

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("memesUrl");


        if(isNetworkAvailable()){
            fetchData();

        }



    }

    private void fetchData() {
        progressBar2.setVisibility(View.VISIBLE);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "onDataChange: splash");
                    urls.clear();
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot child:children) {
                        Meme meme = child.getValue(Meme.class);
                        urls.add(new Meme(meme.getUrl(),meme.getLike(),meme.getRef()));

                    }


                    if(urls.size()!=0){
                        Collections.reverse(urls);
                        if(readyToGo){
                            readyToGo= false;

                            myRef.removeEventListener(this);
                            startActivity(new Intent(SplashActivity.this,MainActivity.class));
                            finish();
                        }

                    }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.i(TAG, "onCancelled: ");
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



    }

    /*private void fetchData() {
        if(isNetworkAvailable()){

            progressBar2.setVisibility(View.VISIBLE);

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    HashMap<String, Meme> temp= (HashMap<String, Meme>) dataSnapshot.getValue();

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
*/

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
