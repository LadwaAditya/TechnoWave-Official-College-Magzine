package com.adityaladwa.technowave;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


public class GalleryActivityFragment extends Fragment {
    private GridView mGridView;
    private GalleryCallback mGalleryCallback;

    public GalleryActivityFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mGalleryCallback = (GalleryCallback) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);


        mGridView = (GridView) rootView.findViewById(R.id.my_grid_view);
        mGridView.setAdapter(new ImageAdapter(getActivity()));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mGalleryCallback.onPhotoClicked(i);
            }
        });
        return rootView;
    }


    public interface GalleryCallback {
        void onPhotoClicked(int i);
    }


}
