package com.coreman.gallerymvp.ui.photos;

import com.coreman.gallerymvp.models.Photo;

import java.util.List;

public interface PhotosContract {

    interface View {
        void showImages(List<Photo> photos);
        void showError(String Error);
    }

    interface Presenter {
        void loadImages(long bucketId);
    }

}
