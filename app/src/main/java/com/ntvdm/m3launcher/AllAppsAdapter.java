package com.ntvdm.m3launcher;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class AllAppsAdapter extends BaseAdapter {
    private Context mContext;
    private List<ResolveInfo> mApps;

    public AllAppsAdapter(Context context, List<ResolveInfo> apps) {
        mContext = context;
        mApps = apps;
    }

    public int getCount() {
        return mApps.size();
    }

    public Object getItem(int position) {
        return mApps.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_item, parent, false);
        } else {
            view = convertView;
        }

        ResolveInfo info = mApps.get(position);

        ImageView icon = (ImageView) view.findViewById(R.id.item_icon);
        TextView label = (TextView) view.findViewById(R.id.item_label);

        icon.setImageDrawable(info.loadIcon(mContext.getPackageManager()));
        label.setText(info.loadLabel(mContext.getPackageManager()));

        return view;
    }
}