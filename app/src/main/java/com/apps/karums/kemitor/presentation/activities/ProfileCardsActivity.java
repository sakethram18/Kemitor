package com.apps.karums.kemitor.presentation.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.apps.karums.kemitor.R;
import com.apps.karums.kemitor.Utils;
import com.apps.karums.kemitor.data_access.KemitorDataResolver;
import com.apps.karums.kemitor.data_access.ProfileModel;
import com.apps.karums.kemitor.presentation.widgets.ProfileCardsAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class ProfileCardsActivity extends AppCompatActivity {
    private static String LOG_TAG = "ProfileCardsActivity";
    private RecyclerView.Adapter mAdapter;
    private ArrayList<ProfileModel> mProfiles;
    private AdView mAdView;
    private Context mContext;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_profile_cards);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.profiles));

        mAdView = (AdView) findViewById(R.id.adViewProfiles);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBarProfiles);
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mProfiles = Utils.getSavedProfiles(mContext);
                mAdapter = new ProfileCardsAdapter(mContext, mProfiles);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.setAdapter(mAdapter);
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
                ((ProfileCardsAdapter) mAdapter).setOnItemClickListener(new ProfileCardsAdapter
                        .MyClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent appsActivity = new Intent(ProfileCardsActivity.this, AppsListActivity.class);
                        startActivity(appsActivity);
                    }

                    @Override
                    public void onItemLongClickListener(int position, View v) {
                    }
                });

            }
        }).start();

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        final FloatingActionButton fabAddProfile = (FloatingActionButton) findViewById(R.id
                .fabAddProfile);
        fabAddProfile.setImageResource(R.drawable.ic_add_white_18dp);
        fabAddProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddNewProfileDialog();
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0)
                    fabAddProfile.hide();
                else if (dy < 0)
                    fabAddProfile.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createAddNewProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_profile, null);

        final EditText input = (EditText) dialogView.findViewById(R.id.newProfileName);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView)
                .setTitle(getString(R.string.add_new_profile))
                // Add action buttons
               .setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String newProfile = input.getText().toString();
                        if (!TextUtils.isEmpty(newProfile)) {
                            ProfileModel model = new ProfileModel(newProfile, true, false);
                            ((ProfileCardsAdapter) mAdapter).addItem(model, mAdapter.getItemCount
                                    ());
                            KemitorDataResolver resolver = new KemitorDataResolver(mContext);
                            resolver.insertProfileModel(model);
                        }
                    }
                })
               .setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private ArrayList<ProfileModel> getDataSet() {
        ArrayList results = new ArrayList<>();
        for (int index = 0; index < 20; index++) {
            ProfileModel obj = new ProfileModel("Some Profile " + index,
                    true, true);
            results.add(index, obj);
        }
        return results;
    }
}
