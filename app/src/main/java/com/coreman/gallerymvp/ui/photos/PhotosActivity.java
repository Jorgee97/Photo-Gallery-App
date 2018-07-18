package com.coreman.gallerymvp.ui.photos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.coreman.gallerymvp.R;
import com.coreman.gallerymvp.models.Photo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotosActivity extends AppCompatActivity implements PhotosContract.View {
    private static final String TAG = "PhotosActivity";

    @BindView(R.id.recycler_photos)
    RecyclerView mRecyclerPhotos;

    PhotosPresenter mPresenter;
    PhotosAdapter mPhotosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        ButterKnife.bind(this);

        setupMVP();
        setupViews();

        if (getIntent().hasExtra("BUCKED_ID")) {
            mPresenter.loadImages(Long.parseLong(getIntent().getStringExtra("BUCKED_ID")));
        }
    }

    private void setupMVP() {
        mPresenter = new PhotosPresenter(this, this);
    }

    private void setupViews() {
        mRecyclerPhotos.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerPhotos.setHasFixedSize(true);
    }

    @Override
    public void showImages(List<Photo> photos) {
        mPhotosAdapter = new PhotosAdapter(this, photos);
        mRecyclerPhotos.setAdapter(mPhotosAdapter);
    }

    @Override
    public void showError(String Error) {
        Toast.makeText(this, Error, Toast.LENGTH_SHORT).show();
    }
}
