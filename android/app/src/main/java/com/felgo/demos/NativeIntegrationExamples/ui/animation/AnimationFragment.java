package com.felgo.demos.NativeIntegrationExamples.ui.animation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.felgo.demos.NativeIntegrationExamples.R;
import com.felgo.ui.FelgoAndroidFragment;

public class AnimationFragment extends Fragment {

  private FelgoAndroidFragment m_felgo;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_animations, container, false);

    m_felgo = (FelgoAndroidFragment) getChildFragmentManager().findFragmentById(R.id.qt_fragment_container);

    root.findViewById(R.id.btn_add_item).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        m_felgo.callQmlMethod("createItem");
      }
    });

    root.findViewById(R.id.btn_animate_items).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        m_felgo.callQmlMethod("animateItems");
      }
    });

    root.findViewById(R.id.btn_clear_items).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        m_felgo.callQmlMethod("clearItems");
      }
    });

    return root;
  }

}
