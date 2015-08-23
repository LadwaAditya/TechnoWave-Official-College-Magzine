package com.adityaladwa.technowave;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import uk.co.senab.photoview.PhotoViewAttacher;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    public ViewPager mViewPager;
    public ArticlePagerAdapter mPagerAdapter;
    private String[] mArticleList;
    private ListView mDrawerListView;
    private static final int NAVDRAWER_LAUNCH_DELAY = 300;
    private ActionBar ab;
    private FloatingActionButton fab, fab1;
    private FloatingActionMenu fabMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Setup toolbar as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        ab.setTitle(R.string.app_name);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);


        mArticleList = getResources().getStringArray(R.array.article_list);
        mDrawerListView = (ListView) findViewById(R.id.left_drawer);

        mDrawerListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mArticleList));
        mDrawerListView.setOnItemClickListener(new DrawerItemClickListner());

        mViewPager = (ViewPager) findViewById(R.id.pager_article);
        mPagerAdapter = new ArticlePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        fab = (FloatingActionButton) findViewById(R.id.fab_menu_item_download);
        fab1 = (FloatingActionButton) findViewById(R.id.fab_menu_item_share);
        fabMenu = (FloatingActionMenu) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);

        selectItem(1);


        mDrawerToggle.syncState();

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

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

        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_menu_item_download) {
            fabMenu.close(true);
            int currentImagePos = mViewPager.getCurrentItem();

            Bitmap bm = BitmapFactory.decodeResource(getResources(), Utility.mImageId[currentImagePos]);

            String path = Environment.getExternalStorageDirectory().toString();


            boolean success = false;

            try {
                new File(path + "/TechnoWave").mkdirs();

                File img = new File(path, "TechnoWave/Technowave" + currentImagePos + ".png");

                FileOutputStream outStream = new FileOutputStream(img);
                bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);

                MediaStore.Images.Media.insertImage(getContentResolver(), img.getAbsolutePath(), img.getName(), img.getName());
                MediaScannerConnection.scanFile(this, new String[]{img.getPath()}, new String[]{"image/png"}, null);
                outStream.flush();
                outStream.close();
                success = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (success) {
                Toast.makeText(getApplicationContext(), "Image saved",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Error saving image", Toast.LENGTH_LONG).show();
            }

        }

        if (view.getId() == R.id.fab_menu_item_share) {
            fabMenu.close(true);
            int currentImagePos = mViewPager.getCurrentItem();

            Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), Utility.mImageId[currentImagePos]);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "TechnoWave");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values);

            OutputStream outstream;
            try {
                outstream = getContentResolver().openOutputStream(uri);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                outstream.close();
            } catch (Exception e) {
                System.err.println(e.toString());
            }

            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(shareIntent, "Share Image"));

        }

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


            mDrawerLayout.closeDrawer(mDrawerListView);

        }
    }

    private void selectItem(int position) {
        //  mDrawerListView.setItemChecked(position, true);
        Bundle data = new Bundle();
        data.putInt("pos", position);

    }

    private void startGalleryActivity() {
        startActivity(new Intent(MainActivity.this, GalleryActivity.class));
    }


    public class ArticlePagerAdapter extends FragmentStatePagerAdapter {

        public ArticlePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new PhotoFragment();
            Bundle args = new Bundle();
            args.putInt("POSITION", position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return Utility.mImageId.length;
        }
    }


    static public class PhotoFragment extends Fragment {


        public PhotoFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View rootView = inflater.inflate(R.layout.fragment_photo, container, false);
            int pos = getArguments().getInt("POSITION");
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageview_photo);
            PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);


            Glide.with(getActivity())
                    .load(Utility.mImageId[pos])
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);


            return rootView;
        }

    }

}


