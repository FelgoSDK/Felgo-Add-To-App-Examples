package com.felgo.demos.NativeIntegrationExamples.ui.camera;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.felgo.demos.NativeIntegrationExamples.NavigationViewModel;
import com.felgo.demos.NativeIntegrationExamples.R;
import com.felgo.ui.FelgoAndroidFragment;
import com.google.android.material.snackbar.Snackbar;

import org.qtproject.qt5.android.bindings.QtFragment;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class CameraFragment extends Fragment {

  private View m_startBtn;

  private NavigationViewModel m_navModel;
  private ImageView m_imagePreview;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_camera, container, false);

    m_imagePreview = root.findViewById(R.id.image_preview);
    m_startBtn = root.findViewById(R.id.btn_capture);

    m_navModel = new ViewModelProvider(getActivity()).get(NavigationViewModel.class);
    m_navModel.getImageUrl().observe(getViewLifecycleOwner(), new Observer<String>() {
      @Override public void onChanged(String imageUrl) {
        Uri uri = Uri.parse(imageUrl);

        Log.d("CameraFragment", "Set preview image URI:" + uri);

        m_imagePreview.setImageURI(uri);
      }
    });

    m_startBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        showCameraView();
      }
    });

    getChildFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
      @Override public void onBackStackChanged() {
        if (getChildFragmentManager().getBackStackEntryCount() == 0) {
          m_startBtn.setVisibility(View.VISIBLE);
        }
      }
    });

    return root;
  }

  private void showCameraView() {
    try {
      final FelgoAndroidFragment felgo = new FelgoAndroidFragment();

      getChildFragmentManager().beginTransaction()
          .replace(R.id.qt_fragment_container, felgo
                  .setQmlSource(getContext(), "qml/Camera.qml"),
              null)
          .addToBackStack(null)
          .commit();

      m_startBtn.setVisibility(View.GONE);

      // can only interact with QML after it was initialized:
      felgo.setQmlInitializedListener(new QtFragment.QmlInitializedListener() {
        @Override public void onQmlInitialized() {
          felgo.addSignalHandler("imageCaptured", m_imageCapturedListener);
        }
      });
    } catch (IOException ignored) {
      // asset QML file always exists
    }
  }

  private QtFragment.QmlSignalHandler m_imageCapturedListener = new QtFragment.QmlSignalHandler() {
    @Override public void onSignalEmitted(Object[] params) {
      final String imageUrl = (String) params[0];

      Log.d("CameraFragment", "image captured: " + imageUrl);

      getActivity().runOnUiThread(new Runnable() {
        @Override public void run() {
          m_navModel.getImageUrl().setValue(imageUrl);

          getChildFragmentManager().popBackStack();

          Snackbar.make(getView(), R.string.capture_success, Snackbar.LENGTH_LONG).show();
        }
      });
    }
  };
}
