package com.lazackna.juicefinder.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FilterViewModel extends ViewModel {

    private MutableLiveData<FilterSettings> settings;

    public LiveData<FilterSettings> getSettings() {
        if (settings == null) {
            settings = new MutableLiveData<>(new FilterSettings());
        }
        return settings;
    }

    public void setSettings(FilterSettings settings) {
        this.settings.setValue(settings);
    }


}
