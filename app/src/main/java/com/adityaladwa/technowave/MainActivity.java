package com.adityaladwa.technowave;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String[] mArticleList;
    private ListView mDrawerListView;
    private static final int NAVDRAWER_LAUNCH_DELAY = 300;
    private ActionBar ab;
    private boolean isTwoPane = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Setup toolbar as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        ab.setTitle(R.string.app_name);

        if (findViewById(R.id.drawer_layout) != null)
            isTwoPane = false;

        if (!isTwoPane) {
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
            mDrawerLayout.setDrawerListener(mDrawerToggle);

            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }

        mArticleList = getResources().getStringArray(R.array.article_list);
        mDrawerListView = (ListView) findViewById(R.id.left_drawer);

        mDrawerListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mArticleList));
        mDrawerListView.setOnItemClickListener(new DrawerItemClickListner());


        selectItem(1);
        if (!isTwoPane)
            mDrawerToggle.syncState();

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!isTwoPane)
            mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (!isTwoPane)
            if (mDrawerToggle.onOptionsItemSelected(item))
                return true;
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class DrawerItemClickListner implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

            if (position == 0) {
                startGalleryActivity();
                return;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    selectItem(position);

                }
            }, NAVDRAWER_LAUNCH_DELAY);

            if (!isTwoPane)
                mDrawerLayout.closeDrawer(mDrawerListView);

        }
    }

    private void selectItem(int position) {
        //  mDrawerListView.setItemChecked(position, true);
        Bundle data = new Bundle();
        data.putInt("pos", position);

        Fragment fragment = new ArticalFragment();
        fragment.setArguments(data);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        ft.replace(R.id.framecontent, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        ft.commit();
        findViewById(R.id.my_scroll_view).scrollTo(0, 0);
    }

    private void startGalleryActivity() {
        startActivity(new Intent(MainActivity.this, GalleryActivity.class));
    }
}


