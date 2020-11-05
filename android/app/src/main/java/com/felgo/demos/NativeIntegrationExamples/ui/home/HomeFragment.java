package com.felgo.demos.NativeIntegrationExamples.ui.home;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.felgo.demos.NativeIntegrationExamples.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;

public class HomeFragment extends Fragment {


  private NavController m_navController;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    final View root = inflater.inflate(R.layout.fragment_home, container, false);

    ListView m_list = root.findViewById(android.R.id.list);

    // use the NavController to list the other fragments and handle navigation
    m_navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

    NavGraph graph = m_navController.getGraph();

    List<NavDestination> entries = new ArrayList<>();
    int[] navigationEntries = {
        R.id.nav_camera, R.id.nav_chartview, R.id.nav_3d,
        R.id.nav_image_view, R.id.nav_animations, R.id.nav_sensors
    };

    for (int destId : navigationEntries) {
      entries.add(graph.findNode(destId));
    }

    m_list.setAdapter(new NavDestinationArrayAdapter(entries));

    return root;
  }

  private class NavDestinationArrayAdapter extends ArrayAdapter<NavDestination> {
    private final String[] m_detailTexts;
    private final TypedArray m_icons;

    public NavDestinationArrayAdapter(List<NavDestination> entries) {
      super(HomeFragment.this.getContext(),
          R.layout.list_item_home,
          android.R.id.text1,
          entries);

      m_detailTexts = getResources().getStringArray(R.array.navigation_descriptions);
      m_icons = getResources().obtainTypedArray(R.array.navigation_icons);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
      View v = super.getView(position, convertView, parent);

      NavDestination item = getItem(position);

      v.<TextView>findViewById(android.R.id.text1).setText(item.getLabel());
      v.<TextView>findViewById(android.R.id.text2).setText(m_detailTexts[position]);
      v.<ImageView>findViewById(android.R.id.icon).setImageResource(m_icons.getResourceId(position, 0));
      v.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          m_navController.navigate(getItem(position).getId());
        }
      });

      return v;
    }
  }
}
