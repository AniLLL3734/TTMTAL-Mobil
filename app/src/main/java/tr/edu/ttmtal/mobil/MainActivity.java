package tr.edu.ttmtal.mobil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationBarView;
import tr.edu.ttmtal.mobil.fragments.AiFragment;
import tr.edu.ttmtal.mobil.fragments.CalendarFragment;
import tr.edu.ttmtal.mobil.fragments.HomeFragment;
import tr.edu.ttmtal.mobil.fragments.ProgramsFragment;
import tr.edu.ttmtal.mobil.fragments.SearchFragment;
import tr.edu.ttmtal.mobil.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.nav_search) {
                    selectedFragment = new SearchFragment();
                } else if (itemId == R.id.nav_settings) {
                    selectedFragment = new SettingsFragment();
                } else if (itemId == R.id.nav_programs) {
                    selectedFragment = new ProgramsFragment();
                } else if (itemId == R.id.nav_calendar) {
                    selectedFragment = new CalendarFragment();
                } else {
                    selectedFragment = new HomeFragment();
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
                return false;
            }
        });
    }

    public void switchToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.drawer_ai) {
            switchToFragment(new AiFragment());
        } else if (id == R.id.drawer_about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (id == R.id.drawer_gallery) {
            startActivity(new Intent(this, GalleryActivity.class));
        } else if (id == R.id.drawer_contact) {
            startActivity(new Intent(this, ContactActivity.class));
        } else if (id == R.id.drawer_web) {
            openUrl("https://turktelekomatl.meb.k12.tr");
        } else if (id == R.id.drawer_ig) {
            openUrl("https://www.instagram.com/pendikttmtal/");
        } else if (id == R.id.drawer_yt) {
            openUrl("https://www.youtube.com/@pendikttmtal");
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void openHomeWithTab(int tabIndex) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt("tab_index", tabIndex);
        fragment.setArguments(args);
        switchToFragment(fragment);
    }
}