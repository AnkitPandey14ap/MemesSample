package space.apple.three.memes;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Ankit";
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    private ArrayList<String> valueList;
    private ArrayList<String> keyList;
    private ShareActionProvider mShareActionProvider;
//    HashMap<String, String> urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        valueList = new ArrayList<String>();
        keyList = new ArrayList<String>();

        SplashActivity splashActivity = new SplashActivity();
        valueList =splashActivity.valueList;
        keyList =splashActivity.keyList;

        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter=new MyAdapter(valueList,keyList,MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.shareButton);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Car Buddies");

        String shareBody = "Hey,This is the best app for MEMES and TROLLS, " +
                "install this app for all the latest MEMES and TROLS from facebook and Twitter \n\nAndroid\n";
        shareBody= shareBody + "https://play.google.com/store/apps/details?id=space.apple.three.memes \n\n";

        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

        //then set the sharingIntent
        mShareActionProvider.setShareIntent(sharingIntent);

        return true;
    }
}
