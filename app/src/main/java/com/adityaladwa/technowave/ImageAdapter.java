package com.adityaladwa.technowave;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


/**
 * Created by AdityaLadwa on 17-Jul-15.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;


    public ImageAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return Utility.mImageId.length;
    }

    @Override
    public Object getItem(int i) {
        return Utility.mImageId[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ImageView imgV;
        if (view == null)
            imgV = new ImageView(mContext);
        else
            imgV = (ImageView) view;


        Glide.with(mContext)
                .load(Utility.mImageId[i])
                .centerCrop()
                .into(imgV);

        return imgV;
    }


}

