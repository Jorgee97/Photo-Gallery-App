package com.coreman.gallerymvp.ui.photos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.coreman.gallerymvp.R;
import com.coreman.gallerymvp.models.Photo;
import com.coreman.gallerymvp.ui.viewer.ViewImageActivity;
import com.coreman.gallerymvp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
    private static final String TAG = "PhotosAdapter";
    private List<Photo> mPhotoList;
    private Context mContext;

    public PhotosAdapter(Context context, List<Photo> photoList) {
        mContext = context;
        mPhotoList = photoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.photo_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mItem = mPhotoList.get(position);

        Log.i(TAG, "onBindViewHolder: " + holder.mItem.getPhotoLocation());
        
        Glide.with(mContext)
                .load(holder.mItem.getPhotoLocation())
                .apply(new RequestOptions().centerCrop().override(120, 120))
                .into(holder.mImageItem);

        holder.mImageItem.setOnClickListener((View v) -> {
            Intent imageViewer = new Intent(mContext, ViewImageActivity.class);
            imageViewer.putExtra("IMAGE_PATH", holder.mItem);
            imageViewer.putParcelableArrayListExtra("IMAGE_ON_ALBUM", new ArrayList<>(mPhotoList));
            mContext.startActivity(imageViewer);
        });
    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageItem;
        Photo mItem;

        public ViewHolder(View view) {
            super(view);
            mImageItem = view.findViewById(R.id.image_photo_item);
        }

    }

}
