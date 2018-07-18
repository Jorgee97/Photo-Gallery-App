package com.coreman.gallerymvp.ui.viewer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.coreman.gallerymvp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewImageActivity extends AppCompatActivity implements ViewImageContract.View, View.OnClickListener {

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

    private void setupViews() {
        mRecyclerSlider.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerSlider.setHasFixedSize(true);

        if (getIntent().hasExtra("IMAGE_PATH")) {
            Glide.with(this)
                    .load(getIntent().getStringExtra("IMAGE_PATH"))
                    .into(mImageDetail);
        }

        if (getIntent().hasExtra("IMAGE_ON_ALBUM")) {
            mViewImageAdapter = new ViewImageAdapter(this, getIntent().getParcelableArrayListExtra("IMAGE_ON_ALBUM"));
            mRecyclerSlider.setAdapter(mViewImageAdapter);
        }

        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    private void setupMVP() {
        mPresenter = new ViewImagePresenter(this, this);
    }

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

    @Override
    public void showImage(String imagePath) {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void onClick(View view) {

    }
}
