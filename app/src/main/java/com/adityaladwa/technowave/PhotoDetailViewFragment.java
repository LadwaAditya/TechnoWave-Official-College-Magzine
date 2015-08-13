package com.adityaladwa.technowave;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PhotoDetailViewFragment extends Fragment {

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
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
        mPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(pos);

        return rootView;
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
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

}
