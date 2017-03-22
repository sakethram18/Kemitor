package com.apps.karums.kemitor.presentation.widgets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.Switch;
import android.widget.TextView;

import com.apps.karums.kemitor.R;
import com.apps.karums.kemitor.data_access.IAppModel;

import java.util.ArrayList;

/**
 * Created by karums on 12/26/2016.
 */

public class AppsArrayAdapter extends ArrayAdapter<IAppModel> {

    private ArrayList<IAppModel> objects;

    public AppsArrayAdapter(Context context, ArrayList<IAppModel> objects) {
        super(context, 0, objects);
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.apps_list, parent, false);
        }

        final IAppModel model = getItem(position);

        ImageView appIcon = (ImageView) listItemView.findViewById(R.id.appIcon);
        appIcon.setImageDrawable(model.getAppIcon());

        TextView appName = (TextView) listItemView.findViewById(R.id.appName);
        appName.setText(model.getAppName());

        TextView packageName = (TextView) listItemView.findViewById(R.id.packageName);
        packageName.setText(model.getPackageName());

        final Switch switchIsSelected = (Switch) listItemView.findViewById(R.id.switchIsSelected);
        switchIsSelected.setChecked(model.isSelected());
        switchIsSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.setSelected(isChecked);
            }
        });


//        if (model.getIsLauncherApp()) {
////            AbsListView.LayoutParams param = (AbsListView.LayoutParams)listItemView.getLayoutParams();
////            listItemView.setVisibility(View.GONE);
////            param.width = 0;
////            param.height = 0;
////            listItemView.setLayoutParams(param);
//            listItemView = new Space(getContext());
//        }
        return listItemView;
    }

    public ArrayList<IAppModel> getItems() {
        return objects;
    }

    //TODO: Remove the following 2 methods to enable view recycling. Might have to deal with
    // switch listener issues
    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
