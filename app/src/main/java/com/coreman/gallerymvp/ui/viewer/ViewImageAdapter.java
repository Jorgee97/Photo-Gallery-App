package com.coreman.gallerymvp.ui.viewer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.coreman.gallerymvp.R;
import com.coreman.gallerymvp.models.Photo;
import com.coreman.gallerymvp.utils.OnItemClickCustom;

import java.util.List;

public class ViewImageAdapter extends RecyclerView.Adapter<ViewImageAdapter.ViewHolder> {
    private static final String TAG = "ViewImageAdapter";
    private Context mContext;
    private List<Photo> mPhotoList;
    private OnItemClickCustom mClickCustom;

    public ViewImageAdapter(Context context, List<Photo> photoList, OnItemClickCustom clickCustom) {
        mContext = context;
        mPhotoList = photoList;
        mClickCustom = clickCustom;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.slider_image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mPhoto = mPhotoList.get(position);

        Glide.with(mContext)
                .load(holder.mPhoto.getPhotoLocation())
                .thumbnail(0.1f)
                .apply(new RequestOptions().centerCrop().override(100, 100))
                .into(holder.sliderImage);

        holder.sliderImage.setOnClickListener((View v) -> {
            mClickCustom.onClickCustom(holder.mPhoto);
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sliderImage;
        Photo mPhoto;

        ViewHolder(View view) {
            super(view);

            sliderImage = view.findViewById(R.id.image_item);
        }
    }
}
