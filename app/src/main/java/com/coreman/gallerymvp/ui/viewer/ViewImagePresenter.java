package com.coreman.gallerymvp.ui.viewer;

import android.content.Context;

public class ViewImagePresenter implements ViewImageContract.Presenter {
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
