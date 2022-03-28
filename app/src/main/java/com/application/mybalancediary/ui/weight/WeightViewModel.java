package com.application.mybalancediary.ui.weight;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WeightViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public WeightViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is weight fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}