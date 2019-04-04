package com.tracking.m2comsys.adapplication.extras;

/**
 * Created by tikson on 24/1/18.
 */

public interface BaseView<T> {
    void setPresenter(T presenter);
    void makeToast(String message);
}
