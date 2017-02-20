package com.example.karums1.kemitor.presentation.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.karums1.kemitor.R;
import com.example.karums1.kemitor.data_access.AppModel;

import java.util.ArrayList;

/**
 * Created by karums1 on 12/26/2016.
 */

public class AppsArrayAdapter extends ArrayAdapter<AppModel> {

    private ArrayList<AppModel> objects;

    public AppsArrayAdapter(Context context, ArrayList<AppModel> objects) {
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

        final AppModel model = getItem(position);

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

//        switchIsSelected.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public void onClick(View v) {
//                if (switchIsSelected.isChecked()) {
//                    model.setSelected(true);
//                } else {
//                    model.setSelected(false);
//                }
//            }
//        });

//        switchIsSelected.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (switchIsSelected.isChecked()) {
//                    model.setSelected(true);
//                } else {
//                    model.setSelected(false);
//                }
//                return false;
//            }
//        });
        return listItemView;
    }

    public ArrayList<AppModel> getItems() {
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
