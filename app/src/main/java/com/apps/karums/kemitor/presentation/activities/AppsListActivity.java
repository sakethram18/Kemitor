package com.apps.karums.kemitor.presentation.activities;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.apps.karums.kemitor.presentation.widgets.AppsArrayAdapter;
import com.apps.karums.kemitor.R;
import com.apps.karums.kemitor.Utils;
import com.apps.karums.kemitor.data_access.IAppModel;
import com.apps.karums.kemitor.data_access.KemitorDataResolver;

import java.util.ArrayList;

public class AppsListActivity extends AppCompatActivity {

    //TODO: Might need to change the progressbar style as per Medium app
    //TODO: Save previously saved apps and display the status while loading the list
    ProgressBar mProgressBar;
    Context mContext;
    ArrayList<IAppModel> mInstalledApps;
    AppsArrayAdapter listArrayAdapter;
    KemitorDataResolver mDataResolver;
    String mCurrentProfileId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_list);
        setTitle(getString(R.string.select_apps));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
        mDataResolver = new KemitorDataResolver(this);
        mCurrentProfileId = getIntent().getStringExtra(ProfileSettingsActivity.PROFILE_ID);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mInstalledApps = Utils.getInstalledApps(mContext);
                final ListView appsListView = (ListView) findViewById(R.id.appsList);
                listArrayAdapter = new AppsArrayAdapter(mContext, getFilteredAndUpdatedApps(false));
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
                handleSaveSettings();
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
        //TODO: Handle updating tables packages whenever there is a package change event received.
        //Also, handle starting the service with all selected apps.
        mDataResolver.deleteMapRecordsOfProfileModel(mCurrentProfileId);
        ArrayList<IAppModel> selectedApps = getSelectedApps(listArrayAdapter.getItems());
        mDataResolver.insertProfileAppModelMap(mCurrentProfileId, selectedApps);
        finish();
        // Update database with latest settings
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                KemitorDataResolver dataResolver = new KemitorDataResolver(mContext);
//                dataResolver.deleteAllAppModels();
//                dataResolver.bulkInsertAppModels(listArrayAdapter.getItems());
//                dataResolver.bulkInsertAppModels(getFilteredAndUpdatedApps(true));
//            }
//        }).start();
        //Update the data model cache
//        DataModel.getInstance().clearAppsList();
//        DataModel.getInstance().updateAppsList(listArrayAdapter.getItems());
//        DataModel.getInstance().updateAppsList(getFilteredAndUpdatedApps(true));
//
//        ArrayList<IAppModel> selectedApps = DataModel.getInstance().getSelectedApps();
////        selectedApps.addAll(DataModel.getInstance().getLauncherApps(false));
//        Intent serviceIntent = new Intent(this, KemitorAccessibilityService.class);
//        serviceIntent.putExtra(AppConstants.KEMITOR_ACCESSIBILITY_SERVICE_ENABLED, true);
//        serviceIntent.putStringArrayListExtra(AppConstants.LIST_OF_SELECTED_APPS,
//                getPackageNameList(selectedApps));
//        startService(serviceIntent);
//        finish();
    }

    public ArrayList<IAppModel> getSelectedApps(ArrayList<IAppModel> allApps) {
        ArrayList<IAppModel> resultList = new ArrayList<>();
        for(IAppModel model: allApps) {
            if (model.isSelected()) {
                resultList.add(model);
            }
        }
        return resultList;
    }

    private ArrayList<String> getPackageNameList(ArrayList<IAppModel> selectedApps) {
        ArrayList<String> packageNamesList = new ArrayList<>();
        for(IAppModel model: selectedApps) {
            packageNamesList.add(model.getPackageName());
        }
        return packageNamesList;
    }

    /**
     * Returns launcher apps or the other apps based on the boolean flag parameter
     * @param includeLauncherApps
     * @return
     */
    private ArrayList<IAppModel> getFilteredAndUpdatedApps(boolean includeLauncherApps) {
        ArrayList<IAppModel> resultList = new ArrayList<>();
        for (IAppModel model: mInstalledApps) {
            if ((includeLauncherApps && model.getIsLauncherApp()) || (!includeLauncherApps && !model
                    .getIsLauncherApp())) {
                resultList.add(model);
            }
        }
        return updateStatusInstalledApps(resultList);
    }

    private ArrayList<IAppModel> updateStatusInstalledApps(ArrayList<IAppModel> listOfApps) {
        // Check what all apps are already selected for the given profile
        ArrayList<String> dbAllApps = mDataResolver
                .getAppModelsForProfileModel(mCurrentProfileId);
        for(IAppModel model: listOfApps) {
            if (dbAllApps.contains(model.getPackageName())) {
                model.setSelected(true);
            }
        }
        return listOfApps;
    }
}
