package com.coreman.gallerymvp.ui.main;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.coreman.gallerymvp.models.PhotoAlbum;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.observers.DisposableLambdaObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class MainPresenter implements MainContract.Presenter {

    private static final String TAG = "MainPresenter";
    
    MainContract.View mainView;
    Context mContext;

    List<PhotoAlbum> mPhotoAlbumList = new ArrayList<>();

    public MainPresenter(MainContract.View view, Context mContext) {
        this.mainView = view;
        this.mContext = mContext;
    }

    @Override
    public void loadAlbums() {
        observeAlbums(mContext).subscribeWith(getObserver());
    }

    private Observable<List<PhotoAlbum>> observeAlbums(final Context context) {
        return Observable.fromCallable(() -> {
            mPhotoAlbumList = new ArrayList<>();
            String[] PROJECTION_BUCKET = {MediaStore.Images.ImageColumns.BUCKET_ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.DATA};
            String BUCKET_GROUP_BY = "1) GROUP BY 1,(2";
            String BUCKET_ORDER_BY = "MAX(datetaken) DESC";
            Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Cursor cur = context.getContentResolver().query(images,
                    PROJECTION_BUCKET, BUCKET_GROUP_BY, null,
                    BUCKET_ORDER_BY);
            PhotoAlbum album;
            if (cur.moveToFirst()) {
                String bucket, date, data;
                long bucketId;
                int bucketColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                int dateColumn = cur.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
                int dataColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);
                int bucketIdColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);

                do {
                    bucket = cur.getString(bucketColumn);
                    date = cur.getString(dateColumn);
                    data = cur.getString(dataColumn);
                    bucketId = cur.getLong(bucketIdColumn);

                    if (bucket != null && bucket.length() > 0) {
                        album = new PhotoAlbum(
                                bucketId,
                                bucket,
                                date,
                                data,
                                countImagesByAlbum(bucket));
                        mPhotoAlbumList.add(album);
                    }
                } while (cur.moveToNext());
            }

            cur.close();

            return mPhotoAlbumList;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private int countImagesByAlbum(final String bucket) {
        String searchParams = "bucket_display_name = \"" + bucket + "\"";
        Cursor cursor = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, searchParams, null, null);

        if (cursor.moveToFirst()) {
            return cursor.getCount();
        }
        cursor.close();
        return  0;
    }

    private DisposableObserver<List<PhotoAlbum>> getObserver() {
        return new DisposableObserver<List<PhotoAlbum>>() {
            @Override
            public void onNext(List<PhotoAlbum> photoAlbums) {
                mainView.showData(photoAlbums);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ", e);
                mainView.showError("Error while getting the albums.");
            }

            @Override
            public void onComplete() {
                mainView.showLoader(false);
            }
        };
    }

}
