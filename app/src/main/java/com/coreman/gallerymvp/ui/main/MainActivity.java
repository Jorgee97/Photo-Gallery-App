package com.coreman.gallerymvp.ui.main;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.coreman.gallerymvp.R;
import com.coreman.gallerymvp.models.PhotoAlbum;
import com.coreman.gallerymvp.utils.PermissionUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private static final String TAG = "MainActivity";
    private final int EXTERNAL_STORAGE_PERMISSIONS = 12;

    @BindView(R.id.recycler_albums)
    RecyclerView mRecyclerView;

    PhotoAlbumAdapter mAdapter;

    MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setupMVP();
        setupViews();

        if (PermissionUtils.isStoragePermissionsGranted(this))
            getAlbums();
        else {
            askForPermissions();
            if (PermissionUtils.isStoragePermissionsGranted(this)) {
                getAlbums();
            }
        }
    }

    private void setupMVP() {
        mPresenter = new MainPresenter(this, this);
    }

    private void setupViews() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
    }

    private void getAlbums() {
        mPresenter.loadAlbums();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mPresenter.loadAlbums();
    }

    private void askForPermissions() {
        if (!PermissionUtils.isStoragePermissionsGranted(this)) {
            PermissionUtils.requestPermissions(this, EXTERNAL_STORAGE_PERMISSIONS,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void showLoader(boolean show) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void showData(List<PhotoAlbum> photoAlbums) {
        mAdapter = new PhotoAlbumAdapter(this, photoAlbums);
        mRecyclerView.setAdapter(mAdapter);
    }
}
