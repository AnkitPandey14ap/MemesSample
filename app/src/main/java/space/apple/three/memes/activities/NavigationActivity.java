package space.apple.three.memes.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import space.apple.three.memes.R;
import space.apple.three.memes.adapter.MyAdapter;
import space.apple.three.memes.data_manager.SharedPref;
import space.apple.three.memes.model.Meme;
import space.apple.three.memes.model.RowPostion;

import static space.apple.three.memes.activities.SplashActivity.keyList;
import static space.apple.three.memes.activities.SplashActivity.urls;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Ankit";
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    Toolbar toolbar;
    private ShareActionProvider mShareActionProvider;

    ArrayList<Meme> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("iMemes");

//        toolbar.setLogo();
        setSupportActionBar(toolbar);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //*******************************************************************
        arrayList = urls;
        mRecyclerView = findViewById(R.id.recycler_view);
        SnapHelper snapHelper=new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);


        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter=new MyAdapter(arrayList,keyList,NavigationActivity.this);
        mRecyclerView.setAdapter(mAdapter);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });


        //ask permission if already not granted
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(NavigationActivity.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }




        //*****************************************************
    }

    boolean doubleBackToExitPressedOnce=false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
//            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(toolbar, "Press BACK again", Snackbar.LENGTH_SHORT);
            snackbar.show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 1000);

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favourite) {
            RowPostion.pos=-1;
            toolbar.setTitle("Favourite");
            SharedPref sp = new SharedPref(this);
            arrayList=new ArrayList<>();
            for(int i=0;i<urls.size();i++){
                Log.i(TAG, "onNavigationItemSelected: "+sp.isLiked(urls.get(i).getRef()));
                if(sp.isLiked(urls.get(i).getRef())){
                    arrayList.add(urls.get(i));
                }
            }

                mRecyclerView = findViewById(R.id.recycler_view);
            /*SnapHelper snapHelper=new PagerSnapHelper();
            snapHelper.attachToRecyclerView(mRecyclerView);
            */mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);


                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter=new MyAdapter(arrayList,keyList,NavigationActivity.this);
                mRecyclerView.setAdapter(mAdapter);




            // Handle the camera action
        } else if (id == R.id.nav_home) {
            RowPostion.pos=-1;
            toolbar.setTitle("iMemes");

            arrayList = urls;
            mRecyclerView = findViewById(R.id.recycler_view);
            /*SnapHelper snapHelper=new PagerSnapHelper();
            snapHelper.attachToRecyclerView(mRecyclerView);
            */mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);


            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter=new MyAdapter(arrayList,keyList,NavigationActivity.this);
            mRecyclerView.setAdapter(mAdapter);

        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Car Buddies");

            String shareBody = "Hey,This is the best app for MEMES and TROLLS, " +
                    "install this app for all the latest MEMES and TROLS from facebook and Twitter \n\nAndroid\n";
            shareBody= shareBody + "https://play.google.com/store/apps/details?id=space.apple.three.memes \n\n";

            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

            //then set the sharingIntent
//            mShareActionProvider.setShareIntent(sharingIntent);
//            startActivity(Intent.CreateChooser(sharingIntent, "Test"));
            startActivity(Intent.createChooser(sharingIntent,"Share iMemes"));



        } else if (id == R.id.nav_send) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"3applespace@gmail.com"});
//        i.putExtra(Intent.EXTRA_EMAIL  , arrayList);
            i.putExtra(Intent.EXTRA_SUBJECT, "iMemes Feedback");
//            i.putExtra(Intent.EXTRA_TEXT   , feedbackEditText.getText().toString());
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(NavigationActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
