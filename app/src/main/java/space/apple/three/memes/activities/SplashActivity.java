package space.apple.three.memes.activities;

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

import space.apple.three.memes.R;
import space.apple.three.memes.data_manager.SharedPref;
import space.apple.three.memes.model.Meme;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "Ankit";
    private static ArrayList<Meme> urls = new ArrayList<>();
    private DatabaseReference myRef;
    private DatabaseReference myRefToVisits;

    private ProgressBar progressBar2;
    private DatabaseReference myRefToInstalls;
    private boolean readyToGo = true;

    public static ArrayList<Meme> getUrls() {
        Collections.shuffle(urls);
        return urls;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar2 = findViewById(R.id.progressBar2);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("memesUrl");
        myRefToVisits = database.getReference("visits");
        myRefToInstalls = database.getReference("installs");

        startDataFetching();

    }

    private void startDataFetching() {
        if (isNetworkAvailable()) {
            final SharedPref sp = new SharedPref(this);
            if (!sp.isRegistered()) {
                final String[] x = new String[1];
                myRefToInstalls.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        x[0] = String.valueOf(dataSnapshot.getValue());
                        myRefToInstalls.removeEventListener(this);
                        int c = Integer.parseInt(x[0]) + 1;
                        sp.register();
                        myRefToInstalls.setValue(String.valueOf(c));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            final String[] x = new String[1];
            myRefToVisits.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    x[0] = String.valueOf(dataSnapshot.getValue());
                    myRefToVisits.removeEventListener(this);
                    int c = Integer.parseInt(x[0]) + 1;

                    myRefToVisits.setValue(String.valueOf(c));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //fetch meme data now
            fetchData();

        } else showNetworkErrorDialogue();

    }

    private void fetchData() {
        progressBar2.setVisibility(View.VISIBLE);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "onDataChange: splash");
                urls.clear();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    Meme meme = child.getValue(Meme.class);
                    assert meme != null;
                    urls.add(new Meme(meme.getUrl(), meme.getLike(), meme.getRef()));

                }


                if (urls.size() != 0) {
                    Collections.reverse(urls);
                    if (readyToGo) {
                        readyToGo = false;

                        myRef.removeEventListener(this);
                        startActivity(new Intent(SplashActivity.this, NavigationActivity.class));
                        finish();
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.i(TAG, "onCancelled: ");
                showErrorDialogue();

            }
        });


    }

    private void showErrorDialogue() {
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

    private void showNetworkErrorDialogue() {
        progressBar2.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("No Internet")
                .setMessage("Check your internet connection.")
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startDataFetching();
                    }
                });


        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
