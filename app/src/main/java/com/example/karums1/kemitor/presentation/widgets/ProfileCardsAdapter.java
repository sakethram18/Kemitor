package com.example.karums1.kemitor.presentation.widgets;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.karums1.kemitor.KemitorApplication;
import com.example.karums1.kemitor.R;
import com.example.karums1.kemitor.data_access.ProfileModel;

import java.util.ArrayList;

/**
 * Created by karums1 on 3/5/2017.
 */

public class ProfileCardsAdapter extends RecyclerView.Adapter<ProfileCardsAdapter.DataObjectHolder> {

    private static String LOG_TAG = "ProfileCardsAdapter";
    private ArrayList<ProfileModel> mDataset;
    private static MyClickListener myClickListener;

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public ProfileCardsAdapter(ArrayList<ProfileModel> myDataset) {
        mDataset = myDataset;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        TextView mProfileName;
        TextView mSettingsType;
        TextView mBlockLevel;
        TextView mBlockDescription;
        Switch mIsProfileSelected;
        SeekBar mSeekbar;

        public DataObjectHolder(View itemView) {
            super(itemView);
            mProfileName = (TextView) itemView.findViewById(R.id.profileName);
            mSettingsType = (TextView) itemView.findViewById(R.id.settingsType);
            mBlockLevel = (TextView) itemView.findViewById(R.id.block_level_label);
            mBlockDescription = (TextView) itemView.findViewById(R.id.block_level_description);
            mIsProfileSelected = (Switch) itemView.findViewById(R.id.switchIsProfileSelected);
            mSeekbar = (SeekBar) itemView.findViewById(R.id.seekBarFocusLevel);

            itemView.setOnClickListener(this);
            mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Resources res = KemitorApplication.getAppContext().getResources();
                    String[] strictnessLevel = res.getStringArray(R.array.strictness_level);
                    String[] strictnessDescription = res.getStringArray(R.array
                            .strictness_level_description);
                    mBlockLevel.setText(strictnessLevel[progress]);
                    mBlockDescription.setText(strictnessDescription[progress]);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
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
        holder.mSettingsType.setText(mDataset.get(position).isProfileLevelSetting() ?
                "true":"false");
        holder.mIsProfileSelected.setChecked(mDataset.get(position).isEnabled());
        holder.mSeekbar.setProgress(0);
    }

    public void addItem(ProfileModel dataObj, int index) {
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
