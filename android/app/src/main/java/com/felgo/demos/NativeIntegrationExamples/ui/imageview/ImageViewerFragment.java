package com.felgo.demos.NativeIntegrationExamples.ui.imageview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.felgo.demos.NativeIntegrationExamples.R;
import com.felgo.ui.FelgoAndroidFragment;

import org.apache.commons.io.IOUtils;
import org.qtproject.qt5.android.bindings.QmlInitializedListener;
import org.qtproject.qt5.android.bindings.QtFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ImageViewerFragment extends Fragment {

  public static final int REQUEST_PICK_IMAGE = 1;
  private String imageUrl;
  private TextView m_urlText;
  private FelgoAndroidFragment m_felgo;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_image_viewer, container, false);

    m_felgo = (FelgoAndroidFragment) getChildFragmentManager().findFragmentById(R.id.qt_fragment_container);
    m_urlText = root.findViewById(R.id.input_url);

    m_felgo.setQmlInitializedListener(new QmlInitializedListener() {
      @Override
      public void onQmlInitialized() {
        getActivity().runOnUiThread(new Runnable() {
          @Override
          public void run() {
            setImageUrl("assets:/img/felgo-logo.png");
          }
        });
      }
    });

    root.findViewById(R.id.btn_reset_transform).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        m_felgo.callQmlMethod("resetTransform");
      }
    });
    root.findViewById(R.id.btn_pick_image).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivityForResult(new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
            REQUEST_PICK_IMAGE);
      }
    });

    return root;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if(requestCode == REQUEST_PICK_IMAGE) {
      if(resultCode == Activity.RESULT_OK) {
        try {
          // android returns a content:// URI, which QML cannot use
          // instead copy it to a temporary file

          InputStream is = getContext().getContentResolver().openInputStream(data.getData());
          File tempFile = new File(getContext().getFilesDir(), "temp.jpg");
          IOUtils.copy(is, new FileOutputStream(tempFile, false));
          is.close();

          setImageUrl("file:///" + tempFile.getAbsolutePath());
        } catch (Exception e) {
          Log.w("ImageViewerFragment", "Could not open gallery image", e);
        }
      }
    }
  }

  private void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
    m_urlText.setText(this.imageUrl);

    // set to empty text first, so it is reloaded even if the file changed but the URL stayed the same
    m_felgo.setQmlProperty("imageUrl", "");
    m_felgo.setQmlProperty("imageUrl", ImageViewerFragment.this.imageUrl);
  }
}
