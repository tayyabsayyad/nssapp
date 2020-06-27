package com.test.nss.ui.camp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CampViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CampViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Camp Hours fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}