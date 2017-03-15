package com.example.karums1.kemitor.presentation.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.os.Handler;

import com.example.karums1.kemitor.KemitorApplication;
import com.example.karums1.kemitor.R;
import com.example.karums1.kemitor.data_access.BlockLevel;
import com.example.karums1.kemitor.data_access.KemitorDataResolver;
import com.example.karums1.kemitor.data_access.ProfileModel;

import java.util.ArrayList;

/**
 * Created by karums1 on 3/5/2017.
 */

public class ProfileCardsAdapter extends RecyclerView.Adapter<ProfileCardsAdapter.DataObjectHolder> {

    private static String LOG_TAG = "ProfileCardsAdapter";
    private ArrayList<ProfileModel> mDataset;
    private static MyClickListener myClickListener;
    private Context mContext;

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public ProfileCardsAdapter(Context context, ArrayList<ProfileModel> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        TextView mProfileName;
        CheckBox mSettingsType;
        TextView mBlockLevel;
        TextView mBlockDescription;
        Switch mIsProfileSelected;
        SeekBar mSeekbar;

        public DataObjectHolder(View itemView) {
            super(itemView);
            mProfileName = (TextView) itemView.findViewById(R.id.profileName);
            mSettingsType = (CheckBox) itemView.findViewById(R.id.checkboxIsProfileLevelSettings);
            mBlockLevel = (TextView) itemView.findViewById(R.id.block_level_label);
            mBlockDescription = (TextView) itemView.findViewById(R.id.block_level_description);
            mIsProfileSelected = (Switch) itemView.findViewById(R.id.switchIsProfileSelected);
            mSeekbar = (SeekBar) itemView.findViewById(R.id.seekBarFocusLevel);

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
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        holder.mProfileName.setText(mDataset.get(position).getProfileName());
        holder.mSettingsType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDataset.get(position).setIsProfileLevelSetting(isChecked);
                synchronizeWithDatabase(position);
            }
        });
        holder.mIsProfileSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDataset.get(position).setIsEnabled(isChecked);
                synchronizeWithDatabase(position);
            }
        });
        holder.mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Resources res = KemitorApplication.getAppContext().getResources();
                String[] strictnessLevel = res.getStringArray(R.array.strictness_level);
                String[] strictnessDescription = res.getStringArray(R.array
                        .strictness_level_description);
                holder.mBlockLevel.setText(strictnessLevel[progress]);
                holder.mBlockDescription.setText(strictnessDescription[progress]);
                if (fromUser) {
                    mDataset.get(position).setProfileBlockLevel(BlockLevel.getBlockLevelFromValue(progress));
                    synchronizeWithDatabase(position);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        holder.mSettingsType.setChecked(mDataset.get(position).isProfileLevelSetting());
        holder.mIsProfileSelected.setChecked(mDataset.get(position).isEnabled());
        holder.mSeekbar.setProgress(mDataset.get(position).getProfileBlockLevel().getLevel());
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

    private void synchronizeWithDatabase(final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                KemitorDataResolver resolver = new KemitorDataResolver(mContext);
                resolver.updateProfileModel(mDataset.get(position));
            }
        }).start();
    }
}
