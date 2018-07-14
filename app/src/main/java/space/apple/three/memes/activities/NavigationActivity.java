package space.apple.three.memes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import space.apple.three.memes.R;
import space.apple.three.memes.adapter.ScreenSlidePagerAdapter;
import space.apple.three.memes.data_manager.SharedPref;
import space.apple.three.memes.utils.transformer.HingeTransformation;
import space.apple.three.memes.model.Meme;
import space.apple.three.memes.model.RowPostion;

import static space.apple.three.memes.activities.SplashActivity.urls;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Ankit";
    private LinearLayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    Toolbar toolbar;

    ArrayList<Meme> arrayList = new ArrayList<>();
    private boolean doubleBackToExitPressedOnce;
    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This line enables to use vector recourse in app for device below API 21
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_navigation);
        arrayList = urls;
        setUpUi();
    }

    private void setUpUi() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("iMemes");
        setSupportActionBar(toolbar);
        setUpDrawer();
        setupViewPager();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPager.setCurrentItem(0,true);
            }
        });
    }
    private void setupViewPager() {
        mPager = findViewById(R.id.view_pager);
        int size=arrayList.size();
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),size);
        mPager.setAdapter(mPagerAdapter);
        //AntiClockSpinTransformation transformation = new AntiClockSpinTransformation();
        HingeTransformation transformation=new HingeTransformation();
        mPager.setPageTransformer(true,transformation);
    }

    private void setUpDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //make navigationView clickable
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Snackbar snackbar = Snackbar.make(toolbar, "Press BACK again", Snackbar.LENGTH_SHORT);
            snackbar.show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 1000);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favourite) {
            RowPostion.pos = -1;
            toolbar.setTitle("Favourite");
            SharedPref sp = new SharedPref(this);
            arrayList = new ArrayList<>();
            for (int i = 0; i < urls.size(); i++) {
                Log.i(TAG, "onNavigationItemSelected: " + sp.isLiked(urls.get(i).getRef()));
                if (sp.isLiked(urls.get(i).getRef())) {
                    arrayList.add(urls.get(i));
                }
            }
            mPagerAdapter.notifyDataSetChanged();



            // Handle the camera action
        } else if (id == R.id.nav_home) {
            RowPostion.pos = -1;
            toolbar.setTitle("iMemes");

            arrayList = urls;
            mPagerAdapter.notifyDataSetChanged();

        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Car Buddies");

            String shareBody = "Hey,This is the best app for MEMES and TROLLS, " +
                    "install this app for all the latest MEMES and TROLS from facebook and Twitter \n\nAndroid\n";
            shareBody = shareBody + "https://play.google.com/store/apps/details?id=space.apple.three.memes \n\n";
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share iMemes"));

        } else if (id == R.id.nav_send) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"3applespace@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "iMemes Feedback");
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(NavigationActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
