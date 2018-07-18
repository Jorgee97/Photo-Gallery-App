package com.coreman.gallerymvp.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {

    private Uri mPhotoLocation;
    private int mPhotoId;

    public Photo(Uri mPhotoLocation) {
        this.mPhotoLocation = mPhotoLocation;
    }

    public Photo(Uri mPhotoLocation, int mPhotoId) {
        this.mPhotoLocation = mPhotoLocation;
        this.mPhotoId = mPhotoId;
    }

    public Uri getPhotoLocation() {
        return mPhotoLocation;
    }

    public int getPhotoId() {
        return mPhotoId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mPhotoLocation, flags);
        dest.writeInt(this.mPhotoId);
    }

    protected Photo(Parcel in) {
        this.mPhotoLocation = in.readParcelable(Uri.class.getClassLoader());
        this.mPhotoId = in.readInt();
    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
