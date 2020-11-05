package com.felgo.demos.NativeIntegrationExamples;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NavigationViewModel extends ViewModel {
  private MutableLiveData<String> m_imageUrl = new MutableLiveData<>();

  public MutableLiveData<String> getImageUrl() {
    return m_imageUrl;
  }
}
