package com.coreman.gallerymvp.models;

public class PhotoAlbum {

    private long bucketId;
    private String buckedName;
    private String dataTaken;
    private String data;
    private int totalCount;

    public PhotoAlbum(long bucketId, String buckedName, String dataTaken, String data, int totalCount) {
        this.bucketId = bucketId;
        this.buckedName = buckedName;
        this.dataTaken = dataTaken;
        this.data = data;
        this.totalCount = totalCount;
    }

    public long getBucketId() {
        return bucketId;
    }

    public String getBuckedName() {
        return buckedName;
    }

    public String getDataTaken() {
        return dataTaken;
    }

    public String getData() {
        return data;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
