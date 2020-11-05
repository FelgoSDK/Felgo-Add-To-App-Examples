package com.felgo.demos.NativeIntegrationExamples.ui.cube3d;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.felgo.demos.NativeIntegrationExamples.R;

public class Cube3dFragment extends Fragment {
  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_3d, container, false);

    return root;
  }
}
