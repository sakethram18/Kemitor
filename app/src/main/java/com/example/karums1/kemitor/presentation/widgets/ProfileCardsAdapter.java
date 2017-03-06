package com.example.karums1.kemitor.presentation.widgets;

import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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
            implements View.OnClickListener, View.OnLongClickListener {
        TextView mProfileName;
        TextView mSettingsType;
        TextView mProfileName2;
        TextView mSettingsType2;
        Switch mIsProfileSelected;

        public DataObjectHolder(View itemView) {
            super(itemView);
            mProfileName = (TextView) itemView.findViewById(R.id.profileName);
            mSettingsType = (TextView) itemView.findViewById(R.id.settingsType);
            mProfileName2 = (TextView) itemView.findViewById(R.id.profileName2);
            mSettingsType2 = (TextView) itemView.findViewById(R.id.settingsType2);
            mIsProfileSelected = (Switch) itemView.findViewById(R.id.switchIsProfileSelected);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            myClickListener.onItemLongClickListener(getAdapterPosition(), v);
            return true;
        }
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
        void onItemLongClickListener(int position, View v);
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row, parent, false);

        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {
        holder.mProfileName.setText(mDataset.get(position).getProfileName());
        holder.mSettingsType.setText(mDataset.get(position).getSettingsType());
        holder.mIsProfileSelected.setChecked(mDataset.get(position).isProfileSelected());
        holder.mIsProfileSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDataset.get(holder.getAdapterPosition()).setIsProfileSelected(isChecked);
                if (isChecked) {
                    holder.mProfileName2.setVisibility(View.VISIBLE);
                    holder.mSettingsType2.setVisibility(View.VISIBLE);
                } else {
                    holder.mProfileName2.setVisibility(View.GONE);
                    holder.mSettingsType2.setVisibility(View.GONE);
                }
            }
        });
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
