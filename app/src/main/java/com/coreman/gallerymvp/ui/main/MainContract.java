package com.coreman.gallerymvp.ui.main;

import com.coreman.gallerymvp.models.PhotoAlbum;

import java.util.List;

public interface MainContract  {

    interface View {
        void showLoader(boolean show);
        void showMessage(String message);
        void showError(String error);
        void showData(List<PhotoAlbum> photoAlbums);
    }

    interface Presenter {
        void loadAlbums();
    }
}
