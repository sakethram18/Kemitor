package com.example.karums1.kemitor;

import android.app.usage.UsageStats;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import static com.example.karums1.kemitor.AppConstants.KEMITOR_ACCESSIBILITY_SERVICE_ENABLED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static int MY_PERMISSIONS_REQUEST_USAGE_STATS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        Button endButton = (Button) findViewById(R.id.endButton);
        endButton.setOnClickListener(this);
        Button permButton = (Button) findViewById(R.id.permissionButton);
        permButton.setOnClickListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startButton:
                if (handleAccessibilityPermissions()) {
                    Intent startIntent = new Intent(this, KemitorAccessibilityService.class);
                    startIntent.putExtra(KEMITOR_ACCESSIBILITY_SERVICE_ENABLED, true);
                    ComponentName name = startService(startIntent);
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
                handlePermissions(v);
                break;
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



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_USAGE_STATS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permission granted :)", Toast.LENGTH_SHORT);

                } else {
                    Toast.makeText(this, "Permission denied :(", Toast.LENGTH_SHORT);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
