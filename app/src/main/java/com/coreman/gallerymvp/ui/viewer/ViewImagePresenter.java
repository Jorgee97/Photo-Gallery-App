package com.coreman.gallerymvp.ui.viewer;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import com.coreman.gallerymvp.models.Photo;

import java.io.File;

import io.reactivex.Observable;

public class ViewImagePresenter implements ViewImageContract.Presenter {
    private static Uri external = MediaStore.Files.getContentUri("external");
    private ViewImageContract.View mainView;
    private Context mContext;

    public ViewImagePresenter(Context context, ViewImageContract.View mainView) {
        this.mContext = context;
        this.mainView = mainView;
    }

    @Override
    public void getImage(String imagePath) {

    }
}
