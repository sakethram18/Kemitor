package com.example.karums1.kemitor.presentation.widgets;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.karums1.kemitor.R;
import com.example.karums1.kemitor.data_access.ProfileCardModel;

import java.util.ArrayList;

/**
 * Created by karums1 on 3/5/2017.
 */

public class ProfileCardsAdapter extends RecyclerView.Adapter<ProfileCardsAdapter.DataObjectHolder> {

    private static String LOG_TAG = "ProfileCardsAdapter";
    private ArrayList<ProfileCardModel> mDataset;
    private static MyClickListener myClickListener;

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public ProfileCardsAdapter(ArrayList<ProfileCardModel> myDataset) {
        mDataset = myDataset;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView mProfileName;
        TextView mSettingsType;
        Switch mIsProfileSelected;

        public DataObjectHolder(View itemView) {
            super(itemView);
            mProfileName = (TextView) itemView.findViewById(R.id.profileName);
            mSettingsType = (TextView) itemView.findViewById(R.id.settingsType);
            mIsProfileSelected = (Switch) itemView.findViewById(R.id.switchIsProfileSelected);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.mProfileName.setText(mDataset.get(position).getProfileName());
        holder.mSettingsType.setText(mDataset.get(position).getSettingsType());
        holder.mIsProfileSelected.setChecked(mDataset.get(position).isProfileSelected());
    }

    public void addItem(ProfileCardModel dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
