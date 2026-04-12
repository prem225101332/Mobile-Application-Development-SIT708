package com.example.istreamapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> selectedUrl = new MutableLiveData<>();

    public MutableLiveData<String> getSelectedUrl() {
        return selectedUrl;
    }

    public void setSelectedUrl(String url) {
        selectedUrl.setValue(url);
    }
}