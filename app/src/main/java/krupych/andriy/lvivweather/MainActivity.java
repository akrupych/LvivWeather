package krupych.andriy.lvivweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import krupych.andriy.lvivweather.model.DailyWeatherModel;

public class MainActivity extends AppCompatActivity implements ListingFragment.ListingCallback {

    private ListingFragment mListingFragment;
    private WeatherFragment mContentFragment;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupNavigationDrawer();
        if (savedInstanceState == null) {
            // first run
            setListingFragment(new LoadingFragment());
            setContentFragment(new LoadingFragment());
            loadWeather();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void setupNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout != null) {
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
            mDrawerLayout.setDrawerListener(mDrawerToggle);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            }
        }
    }

    private void loadWeather() {
        App.getInstance().loadWeather(new Runnable() {
            @Override
            public void run() {
                mListingFragment = new ListingFragment();
                setListingFragment(mListingFragment);
                mContentFragment = WeatherFragment.forCurrentWeather();
                setContentFragment(mContentFragment);
            }
        });
    }

    private void setListingFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_listing, fragment).commit();
    }

    private void setContentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, fragment).commit();
    }

    @Override
    public void onWeatherItemSelected(DailyWeatherModel weather) {
        setContentFragment(WeatherFragment.forDate(weather));
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawers();
        }
    }

    @Override
    public void onCurrentWeatherItemSelected() {
        setContentFragment(WeatherFragment.forCurrentWeather());
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawers();
        }
    }
}