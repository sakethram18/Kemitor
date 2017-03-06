package com.example.karums1.kemitor.presentation.activities;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.karums1.kemitor.R;
import com.example.karums1.kemitor.data_access.ProfileCardModel;
import com.example.karums1.kemitor.presentation.widgets.ProfileCardsAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class ProfileCardsActivity extends AppCompatActivity {
    private static String LOG_TAG = "ProfileCardsActivity";
    private RecyclerView.Adapter mAdapter;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mAdapter = new ProfileCardsAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        FloatingActionButton fabAddProfile = (FloatingActionButton) findViewById(R.id
                .fabAddProfile);
        fabAddProfile.setImageResource(R.drawable.ic_add_white_18dp);
        fabAddProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddNewProfileDialog();
            }
        });
        // Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteItem(index);

        ((ProfileCardsAdapter) mAdapter).setOnItemClickListener(new ProfileCardsAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                ((ProfileCardsAdapter) mAdapter).deleteItem(position);
            }

            @Override
            public void onItemLongClickListener(int position, View v) {
                v.setElevation(12);
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
                        if (newProfile.length() > 0) {
                            ((ProfileCardsAdapter) mAdapter).addItem(new ProfileCardModel(newProfile,
                                    "Default settings", false), mAdapter.getItemCount());
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

    private ArrayList<ProfileCardModel> getDataSet() {
        ArrayList results = new ArrayList<>();
//        for (int index = 0; index < 20; index++) {
//            ProfileCardModel obj = new ProfileCardModel("Some Profile " + index,
//                    "Default settings", true);
//            results.add(index, obj);
//        }
        return results;
    }
}
