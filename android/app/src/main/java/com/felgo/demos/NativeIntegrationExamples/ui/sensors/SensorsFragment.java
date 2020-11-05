package com.felgo.demos.NativeIntegrationExamples.ui.sensors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.felgo.demos.NativeIntegrationExamples.R;
import com.felgo.ui.FelgoAndroidFragment;

import org.qtproject.qt5.android.bindings.QtFragment;

import java.util.Map;
import java.util.TreeSet;

public class SensorsFragment extends Fragment {

  private FelgoAndroidFragment m_felgo;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_sensors, container, false);

    final TextView sensorText = root.findViewById(R.id.sensor_text);

    m_felgo = (FelgoAndroidFragment) getChildFragmentManager().findFragmentById(R.id.qt_fragment_container);

    m_felgo.setQmlInitializedListener(new QtFragment.QmlInitializedListener() {
      @Override
      public void onQmlInitialized() {
        m_felgo.addSignalHandler("onCurrentReadingChanged", new QtFragment.QmlSignalHandler() {
          @Override
          public void onSignalEmitted(Object[] objects) {
            updateSensorText(sensorText);
          }
        });
      }
    });

    return root;
  }

  private void updateSensorText(final TextView sensorText) {
    FragmentActivity activity = getActivity();
    if (activity == null) {
      return;
    }

    // this method is called from the Qt thread
    // read QML property on this thread, then update UI on the UI thread:

    final Map<?, ?> reading = (Map<?, ?>) m_felgo.getQmlProperty("currentReading");

    activity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        StringBuilder b = new StringBuilder();

        TreeSet<?> keysSorted = new TreeSet<>(reading.keySet());
        for(Object key : keysSorted) {
          b.append("- ").append(key.toString())
              .append(": ").append(reading.get(key).toString())
              .append("\n");
        }

        sensorText.setText("QML sensor data:\n" + b.toString());
      }
    });
  }

}
