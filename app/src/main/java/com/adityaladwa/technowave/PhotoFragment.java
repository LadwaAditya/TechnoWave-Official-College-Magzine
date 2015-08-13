package com.adityaladwa.technowave;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;



public class PhotoFragment extends Fragment {


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



        Picasso.with(getActivity())
                .load(Utility.mImageId[pos])
                .resize(200, 200)
                .centerCrop()
                .into(imageView);
        return rootView;
    }


}
