package com.example.karums1.kemitor.presentation.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.karums1.kemitor.R;
import com.example.karums1.kemitor.data_access.ProfileCardModel;
import com.example.karums1.kemitor.presentation.widgets.ProfileCardsAdapter;

import java.util.ArrayList;

public class ProfileCardsActivity extends AppCompatActivity {
    private static String LOG_TAG = "ProfileCardsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_cards);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new ProfileCardsAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);

        // Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteItem(index);

        ((ProfileCardsAdapter) mAdapter).setOnItemClickListener(new ProfileCardsAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }

    private ArrayList<ProfileCardModel> getDataSet() {
        ArrayList results = new ArrayList<>();
        for (int index = 0; index < 20; index++) {
            ProfileCardModel obj = new ProfileCardModel("Some Primary Text " + index,
                    "Secondary " + index, true);
            results.add(index, obj);
        }
        return results;
    }
}
