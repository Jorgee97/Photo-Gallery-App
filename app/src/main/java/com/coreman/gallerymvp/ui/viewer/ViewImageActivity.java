package com.coreman.gallerymvp.ui.viewer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.coreman.gallerymvp.R;
import com.coreman.gallerymvp.models.Photo;
import com.coreman.gallerymvp.utils.OnItemClickCustom;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class ViewImageActivity extends AppCompatActivity implements OnItemClickCustom, ViewImageContract.View {
    private static final String TAG = "ViewImageActivity";
    private static Uri external = MediaStore.Files.getContentUri("external");

    private Photo CURRENT_PICTURE;

    @BindView(R.id.image_detail)
    ImageView mImageDetail;

    @BindView(R.id.recycler_slider)
    RecyclerView mRecyclerSlider;

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;

    ViewImageAdapter mViewImageAdapter;

    ViewImagePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        ButterKnife.bind(this);

        setupMVP();
        setupViews();
    }

    //region Basic Setup
    private void setupViews() {
        mRecyclerSlider.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerSlider.setHasFixedSize(true);

        if (getIntent().hasExtra("IMAGE_PATH")) {
            CURRENT_PICTURE = getIntent().getParcelableExtra("IMAGE_PATH");
            Glide.with(this)
                    .load(CURRENT_PICTURE.getPhotoLocation())
                    .into(mImageDetail);
        }

        if (getIntent().hasExtra("IMAGE_ON_ALBUM")) {
            mViewImageAdapter = new ViewImageAdapter(this, getIntent().getParcelableArrayListExtra("IMAGE_ON_ALBUM"), this::onClickCustom);
            mRecyclerSlider.setAdapter(mViewImageAdapter);
        }

        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    private void setupMVP() {
        mPresenter = new ViewImagePresenter(this, this);
    }
    //endregion

    //region TouchEvents
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 8.0f));

            mImageDetail.setScaleX(mScaleFactor);
            mImageDetail.setScaleY(mScaleFactor);

            return true;
        }
    }
    //endregion

    @Override
    public void onClickCustom(Photo imagePath) {
        Glide.with(this)
                .load(imagePath.getPhotoLocation())
                .into(mImageDetail);
        CURRENT_PICTURE = imagePath;
    }

    @Override
    public void showImage(String imagePath) {

    }

    @Override
    public void showError(String error) {

    }

    //region Menu Setup

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_picture:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, CURRENT_PICTURE);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(shareIntent);
                break;
            case R.id.delete_picture:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.delete_image_dialog)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getContentResolver().delete(external, MediaStore.MediaColumns.DATA + "=?",
                                        new String[]{CURRENT_PICTURE.getPhotoLocation().getPath()});
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        });
                builder.create().show();
                break;
        }
        return false;
    }
    //endregion

}
