package org.meruvian.midas.showcase.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.meruvian.midas.showcase.R;
import org.meruvian.midas.showcase.activity.social.SocialLoginActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewMainActivity extends AppCompatActivity {

    @Bind(R.id.main_toolbar) Toolbar mainToolbar;
    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        ButterKnife.bind(this);
        Toast.makeText(this, "Login succes", Toast.LENGTH_LONG).show();
        setSupportActionBar(mainToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        setNav();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, mainToolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


    }

    private void setNav() {
        NavigationView navi = (NavigationView) findViewById(R.id.nav_view);
        navi.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        int backStack = getFragmentManager().getBackStackEntryCount();
                        while (backStack > 0) {
                            getFragmentManager().popBackStack();
                        }

                        break;

                    case R.id.nav_social_login:
                        Intent intent = new Intent(NewMainActivity.this, SocialLoginActivity.class);
                        startActivity(intent);

                        break;
                }

                return false;
            }
        });

    }
}
