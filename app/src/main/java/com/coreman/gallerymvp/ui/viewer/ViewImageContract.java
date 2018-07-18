package com.coreman.gallerymvp.ui.viewer;

import android.net.Uri;

public interface ViewImageContract {

    interface View {
        void showImage(String imagePath);
        void showError(String error);
    }

    interface Presenter {
        void getImage(String imagePath);
    }
}
