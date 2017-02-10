package com.example.karums1.kemitor;

import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import static com.example.karums1.kemitor.AppConstants.KEMITOR_ACCESSIBILITY_SERVICE_ENABLED;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        Button endButton = (Button) findViewById(R.id.endButton);
        endButton.setOnClickListener(this);
        Button permButton = (Button) findViewById(R.id.permissionButton);
        permButton.setOnClickListener(this);
        Button overlayButton = (Button) findViewById(R.id.drawOverOtherAppsButton);
        overlayButton.setOnClickListener(this);
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
        getMenuInflater().inflate(R.menu.main2, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navAppsList) {
            Intent appsActivity = new Intent(this, AppsListActivity.class);
            startActivity(appsActivity);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startButton:
                if (handleAccessibilityPermissions()) {
//                    Intent startIntent = new Intent(this, KemitorAccessibilityService.class);
//                    startIntent.putExtra(KEMITOR_ACCESSIBILITY_SERVICE_ENABLED, true);
//                    startService(startIntent);
                }
                Snackbar.make(v, "Start service is clicked", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.endButton:
                Intent endIntent = new Intent(this, KemitorAccessibilityService.class);
                endIntent.putExtra(KEMITOR_ACCESSIBILITY_SERVICE_ENABLED, false);
                startService(endIntent);
                Snackbar.make(v, "End service is clicked", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.permissionButton:
//                Intent newAct = new Intent(this, MainActivity.class);
//                startActivity(newAct);
                handlePermissions(v);
                break;
            case R.id.drawOverOtherAppsButton:
                handleSystemOverlayPermissions();
                break;
        }
    }

    private void handleSystemOverlayPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // You don't have permission
                Toast.makeText(this, "Permission not granted...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission granted... :)", Toast.LENGTH_SHORT).show();
            }
        }



    }

    private boolean handleAccessibilityPermissions() {
        if (!Utils.isAccessibilityEnabled(this)) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            return false;
        }
        return true;
    }

    private void handlePermissions(View v) {
//        int permissionResult = Utils.checkUsagePermission(this);
        List<UsageStats> usageList = Utils.getUsageStatsList(this);
        if (usageList.isEmpty()) {

            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "Please grant permissions...", Toast.LENGTH_SHORT);
            Snackbar.make(v, "Please grant permissions...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        } else {
            Utils.printUsageStats(usageList);
            Utils.getUsageEventsList(this);
            Toast.makeText(this, "Permission granted :)", Toast.LENGTH_SHORT);
            Snackbar.make(v, "Permission granted :)", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
}
