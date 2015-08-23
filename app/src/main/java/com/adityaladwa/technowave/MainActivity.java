package com.adityaladwa.technowave;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import uk.co.senab.photoview.PhotoViewAttacher;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    public ViewPager mViewPager;
    public ArticlePagerAdapter mPagerAdapter;
    private FloatingActionMenu fabMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Setup toolbar as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setTitle(R.string.app_name);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                selectItem(menuItem);
                return true;
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.pager_article);
        mPagerAdapter = new ArticlePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_menu_item_download);
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab_menu_item_share);
        fabMenu = (FloatingActionMenu) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);


        mDrawerToggle.syncState();

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_menu_item_download) {
            fabMenu.close(true);
            int position = mViewPager.getCurrentItem();
            new DownloadImage().execute(position);


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


    private void selectItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_message:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.drawer_editor:
                mViewPager.setCurrentItem(10);
                break;
            case R.id.drawer_art:
                mViewPager.setCurrentItem(15);
                break;
            case R.id.drawer_kannada:
                mViewPager.setCurrentItem(27);
                break;
            case R.id.drawer_english:
                mViewPager.setCurrentItem(39);
                break;
            case R.id.drawer_gallery:
                mViewPager.setCurrentItem(66);
                break;
        }

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
            new PhotoViewAttacher(imageView);


            Glide.with(getActivity())
                    .load(Utility.mImageId[pos])
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);


            return rootView;
        }

    }

    private class DownloadImage extends AsyncTask<Integer, Boolean, Void> {
        boolean success = false;

        @Override
        protected Void doInBackground(Integer... integers) {
            int currentImagePos = integers[0];

            Bitmap bm = BitmapFactory.decodeResource(getResources(), Utility.mImageId[currentImagePos]);

            String path = Environment.getExternalStorageDirectory().toString();


            try {
                new File(path + "/TechnoWave").mkdirs();

                File img = new File(path, "TechnoWave/Technowave" + currentImagePos + ".png");

                FileOutputStream outStream = new FileOutputStream(img);
                bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);

                MediaStore.Images.Media.insertImage(getContentResolver(), img.getAbsolutePath(), img.getName(), img.getName());
                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{img.getPath()}, new String[]{"image/png"}, null);
                outStream.flush();
                outStream.close();
                success = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (success)
                Toast.makeText(getApplicationContext(), "Image saved", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

}


