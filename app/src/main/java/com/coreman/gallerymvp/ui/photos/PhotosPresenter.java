package com.coreman.gallerymvp.ui.photos;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.coreman.gallerymvp.models.Photo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class PhotosPresenter implements PhotosContract.Presenter {
    private static final String TAG = "PhotosPresenter";
    private Uri EXTERNAL_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    private Context mContext;
    private PhotosContract.View photosView;
    private List<Photo> mPhotos = new ArrayList<>();

    public PhotosPresenter(Context context, PhotosContract.View photosView) {
        this.mContext = context;
        this.photosView = photosView;
    }

    @Override
    public void loadImages(long bucketId) {
        getPhotosOnAlbum(bucketId).subscribeWith(getObserver());
    }

    private Observable<List<Photo>> getPhotosOnAlbum(long bucketId) {
        return Observable.fromCallable(() -> {
            String[] projection = new String[] {
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_TAKEN,
                    MediaStore.Images.Media.DATA
            };
            String order = MediaStore.Images.Media.DATE_ADDED + " DESC";
            String selection = MediaStore.Images.Media.BUCKET_ID + " = " + bucketId;

            Cursor cursor = mContext.getContentResolver().query(EXTERNAL_URI, projection, selection, null, order);
            if (cursor.moveToFirst()) {
                int imagePath = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                int imageId = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                do {
                    mPhotos.add(new Photo(
                            Uri.fromFile(new File(cursor.getString(imagePath))),
                            cursor.getInt(imageId)
                    ));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return mPhotos;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableObserver<List<Photo>> getObserver() {
        return new DisposableObserver<List<Photo>>() {
            @Override
            public void onNext(List<Photo> photos) {
                photosView.showImages(photos);
            }

            @Override
            public void onError(Throwable e) {
                photosView.showError("There were and error showing the photos on this folder.");
            }

            @Override
            public void onComplete() {

            }
        };
    }
}
