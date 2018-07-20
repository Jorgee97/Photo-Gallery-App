package com.coreman.gallerymvp.ui.viewer;

import android.net.Uri;

import com.coreman.gallerymvp.models.Photo;

import io.reactivex.Observable;

public interface ViewImageContract {

    interface View {
        void showImage(String imagePath);
        void showError(String error);
    }

    interface Presenter {
        void getImage(String imagePath);
    }
}
