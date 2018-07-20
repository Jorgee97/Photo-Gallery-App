package com.coreman.gallerymvp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.coreman.gallerymvp.R;
import com.coreman.gallerymvp.models.PhotoAlbum;
import com.coreman.gallerymvp.ui.photos.PhotosActivity;

import java.util.List;

public class PhotoAlbumAdapter extends RecyclerView.Adapter<PhotoAlbumAdapter.ViewHolder> {
    private static final String TAG = "PhotoAlbumAdapter";

    private List<PhotoAlbum> mPhotoAlbumList;
    private Context mContext;

    public PhotoAlbumAdapter(Context context, List<PhotoAlbum> photoAlbums) {
        mPhotoAlbumList = photoAlbums;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.album_item_grid, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.albumItem = mPhotoAlbumList.get(position);

        Uri uri = Uri.parse(holder.albumItem.getData());

        Glide.with(mContext)
                .load(uri.toString())
                .thumbnail(0.1f)
                .apply(new RequestOptions().centerCrop().dontAnimate())
                .into(holder.thumbnail);
        holder.title.setText(holder.albumItem.getBuckedName());
        // TODO: Add the count item
        holder.counterItem.setText(mContext.getString(R.string.items_count, holder.albumItem.getTotalCount()));

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photosActivity = new Intent(mContext, PhotosActivity.class);
                Log.i(TAG, "onClick: " + holder.albumItem.getBucketId());
                photosActivity.putExtra("BUCKED_ID", String.valueOf(holder.albumItem.getBucketId()));
                mContext.startActivity(photosActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhotoAlbumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView counterItem;
        ImageView thumbnail;
        PhotoAlbum albumItem;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.txt_folder_name);
            counterItem = view.findViewById(R.id.txt_folder_items);
            thumbnail = view.findViewById(R.id.thumbnail);
        }
    }
}
