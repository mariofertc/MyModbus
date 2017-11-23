package com.ambatosystem.mymodbus;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
//import android.widget.Toast;

//import com.ambatosystem.mymodbus.ui.fragments.ModbusDataViewFragment;
import com.ambatosystem.mymodbus.MMConstants;
import com.ambatosystem.mymodbus.R;
import com.ambatosystem.mymodbus.ui.fragments.BasicSlidingMenuFragment;
import com.ambatosystem.mymodbus.ui.fragments.ModbusDataViewFragment;
import com.serotonin.modbus4j.BatchResults;

import java.util.ArrayList;
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = "MM/MainActivity";
    private static MMConstants mConstants = new MMConstants();

    private static final String PREF_FIRST_RUN = "first_run";

    //private Fragment mMenu;
  //  private Fragment mBatchProviderMenu; //This should also implement the menu and batch interface
//    private ActionBar mActionBar;
    private Fragment mBatchProviderMenu;
    private DrawerLayout mMainDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private SharedPreferences mSettings;
    private Boolean mFirstRun = false;
    private int mVersionNumber = -1;
    private Handler mBatchResponseHandler;

    private CharSequence mDrawerTitle = "Connection Options";
    private CharSequence mTitle = "Mobile Modbus";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Get the current version from package */
        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "package info not found.");
        }
        if (pinfo != null)
            mVersionNumber = pinfo.versionCode;
        /*end current version*/

        /* Get settings */
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);

        /* Check if it's the first run */
        mFirstRun = mSettings.getInt(PREF_FIRST_RUN, -1) != mVersionNumber;
        if (mFirstRun) {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putInt(PREF_FIRST_RUN, mVersionNumber);
            editor.commit();
        }
        /*End check first run*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                //startService(new Intent(MainActivity.this, MyService.class));
                startService(new Intent(MainActivity.this, MyService.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        intent = new Intent(this,MyService.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        startService(intent);
        registerReceiver(broadcastReceiver, new IntentFilter(MyService.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        stopService(intent);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.e(TAG, "package info not found.");
            updateUI(intent);
        }
    };

    private void updateUI(Intent intent) {
        //ArrayList<String> response = intent.getStringArrayListExtra("response");


//        Log.d(TAG, response.get(0));
        /*for (ArrayList<String> innerList : response) {
            Log.d(TAG, innerList.get(0))

        }*/
        //Log.d(TAG, intent.getStringExtra("63"));
        //(TextView) findViewById(R.id.modbus_value).setText();
        /*TextView txtModbusValue = (TextView) findViewById(R.id.modbus_value);
        txtModbusValue.setText(intent.getStringExtra("63"));*/
        TextView txtModbusValueInicial = (TextView) findViewById(R.id.modbus_value_inicial);
        txtModbusValueInicial.setText(intent.getStringExtra("63"));

        /*TextView txtView = (TextView) ((Activity)context).findViewById(R.id.text);
        txtView.setText("Hello");*/

        /*TextView txtModbusValue = (TextView) findViewById(R.id.modbus_value);
        txtModbusValue.setText(intent.getStringExtra("63"));*/

        //TextView txtCounter = (TextView) findViewById(R.id.txtCounter);
        //txtDateTime.setText(time);
        //txtCounter.setText(counter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Fragment myfragment;
            myfragment = new BasicSlidingMenuFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.mainFrame, myfragment);
            fragmentTransaction.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            /*fragment = new ModbusDataViewFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.drawer_layout, fragment);
            ft.commit();*/

            //setContentView(R.layout.activity_main);


            /*Toast.makeText(this, "The Wetlands", Toast.LENGTH_SHORT).show();
            TheWetlandsFragment theWetlandsFragment = new TheWetlandsFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.relativelayout_for_fragment, theWetlandsFragment, theWetlandsFragment.getTag()).commit();*/

            //setContentView(R.layout.activity_main);
            fragment = new ModbusDataViewFragment();
            title  = "News";

            //fragment.getFragmentManager();
            //fragment = HomeFragment.getFragInstance();
            //fragment = ModbusDataViewFragment.getFragInstance();


        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        //FragmentTransaction transaction = fragmentManager.beginTransaction();
        /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();;
        transaction.addToBackStack(null);
        transaction.replace(R.id.drawer_layout, fragment);
        transaction.commit();*/
        if (fragment != null) {
            FragmentManager fm = getFragmentManager();
            //FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
        }
        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        //mDrawerToggle.syncState();
    }

    /**
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
