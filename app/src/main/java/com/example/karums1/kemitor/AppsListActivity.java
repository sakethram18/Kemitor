package com.example.karums1.kemitor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.example.karums1.kemitor.AppConstants.KEMITOR_ACCESSIBILITY_SERVICE_ENABLED;

public class AppsListActivity extends AppCompatActivity {

    //TODO: Might need to change the progressbar style as per Medium app
    //TODO: Save previously saved apps and display the status while loading the list
    ProgressBar mProgressBar;
    Context mContext;
    ArrayList<AppModel> installedApps;
    AppsArrayAdapter listArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_list);
        setTitle(getString(R.string.select_apps));
        mContext = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                installedApps = Utils.getInstalledApps(mContext);
                final ListView appsListView = (ListView) findViewById(R.id.appsList);
                listArrayAdapter = new AppsArrayAdapter(mContext, installedApps);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        appsListView.setAdapter(listArrayAdapter);
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

            }
        }).start();

//        appsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String url = adapter.getItem(position).getUrl();
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.apps_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.saveSettings:
                handleSaveSettings();
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleSaveSettings() {
        //TODO: Add a dialog warning that service will be started and changes may no longer be
        // possible
        ArrayList<AppModel> selectedApps = getSelectedApps(listArrayAdapter.getItems());
        Intent serviceIntent = new Intent(this, KemitorAccessibilityService.class);
        serviceIntent.putExtra(AppConstants.KEMITOR_ACCESSIBILITY_SERVICE_ENABLED, true);
        serviceIntent.putStringArrayListExtra(AppConstants.LIST_OF_SELECTED_APPS,
                getPackageNameList(selectedApps));
        startService(serviceIntent);
    }

    private ArrayList<AppModel> getSelectedApps(ArrayList<AppModel> list) {
        ArrayList<AppModel> resultList = new ArrayList<>();
        for(AppModel model: list) {
            if (model.isSelected()) {
                resultList.add(model);
            }
        }
        return resultList;
    }

    private ArrayList<String> getPackageNameList(ArrayList<AppModel> selectedApps) {
        ArrayList<String> packageNamesList = new ArrayList<>();
        for(AppModel model: selectedApps) {
            packageNamesList.add(model.getPackageName());
        }
        return packageNamesList;
    }
}
