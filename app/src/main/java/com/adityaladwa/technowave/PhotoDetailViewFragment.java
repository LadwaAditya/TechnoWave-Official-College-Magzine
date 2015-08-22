package com.adityaladwa.technowave;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import uk.co.senab.photoview.PhotoViewAttacher;


public class PhotoDetailViewFragment extends Fragment {

    private ViewPager mViewPager;
    private PhotoPagerAdapter mPagerAdapter;
    public int pos;

    public PhotoDetailViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_photo_detail_view, container, false);
        pos = getArguments().getInt("POS");

        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mPagerAdapter = new PhotoPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(pos);

        return rootView;
    }

    private class PhotoPagerAdapter extends FragmentStatePagerAdapter {

        public PhotoPagerAdapter(FragmentManager fm) {
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
            // Required empty public constructor
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
                    .centerCrop()
                    .into(imageView);
            return rootView;
        }


    }


}
