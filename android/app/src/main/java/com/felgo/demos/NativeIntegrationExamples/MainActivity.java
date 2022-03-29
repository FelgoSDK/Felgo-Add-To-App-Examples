package com.felgo.demos.NativeIntegrationExamples;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.felgo.ui.FelgoAndroidActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends FelgoAndroidActivity {

  private AppBarConfiguration m_appBarConfiguration;

  private NavigationViewModel m_viewModel;
  private DrawerLayout m_drawer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Set up view model
    m_viewModel = new ViewModelProvider(this).get(NavigationViewModel.class);

    // Set up Activity UI
    setContentView(R.layout.activity_main);
    setSupportActionBar(this.<Toolbar>findViewById(R.id.toolbar));

    m_drawer = findViewById(R.id.drawer_layout);

    // Set up navigation framework
    m_appBarConfiguration = new AppBarConfiguration.Builder(
        R.id.nav_home, R.id.nav_camera, R.id.nav_chartview, R.id.nav_3d,
        R.id.nav_image_view, R.id.nav_animations, R.id.nav_sensors, R.id.nav_grid_view
    )
        .setDrawerLayout(m_drawer)
        .build();

    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    NavigationUI.setupActionBarWithNavController(this, navController, m_appBarConfiguration);

    NavigationView navigationView = findViewById(R.id.nav_view);
    NavigationUI.setupWithNavController(navigationView, navController);

    // React to a profile picture change from anywhere in the app (e.g. CameraFragment):
    final ImageView profileImage = navigationView.getHeaderView(0).findViewById(R.id.profile_image);
    m_viewModel.getImageUrl().observe(this, new Observer<String>() {
      @Override public void onChanged(String imageUrl) {
        profileImage.setImageURI(Uri.parse(imageUrl));
      }
    });
  }

  @Override public void onBackPressed() {
    // Close drawer with back button if it is opened instead of closing Activity:
    if(m_drawer.isDrawerOpen(GravityCompat.START)) {
      m_drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onSupportNavigateUp() {
    // opens the navigation drawer when clicking the menu icon:
    
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    return NavigationUI.navigateUp(navController, m_appBarConfiguration)
        || super.onSupportNavigateUp();
  }
}
