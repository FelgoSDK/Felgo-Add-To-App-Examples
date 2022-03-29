package com.felgo.demos.NativeIntegrationExamples.ui.gridview;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felgo.demos.NativeIntegrationExamples.R;
import com.felgo.ui.FelgoAndroidView;

import org.qtproject.qt5.android.bindings.QmlInitializedListener;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class GridViewFragment extends Fragment {
  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_grid_view, container, false);

    initGridTile(root.findViewById(R.id.felgo_view_1), 1);
    initGridTile(root.findViewById(R.id.felgo_view_2), 2);
    initGridTile(root.findViewById(R.id.felgo_view_3), 3);
    initGridTile(root.findViewById(R.id.felgo_view_4), 4);

    return root;
  }

  private void initGridTile(FelgoAndroidView felgo, int index) {
    felgo.setQmlInitializedListener(() -> {
      // set color with random hue in the hex format "#xxxxxx"
      felgo.setQmlProperty("color", String.format("#%06x",
          Color.HSVToColor(new float[] { new Random().nextFloat() * 360, 0.15f, 0.85f }))
      );
      felgo.setQmlProperty("index", index);
    });
  }

}
