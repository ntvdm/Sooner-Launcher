package com.ntvdm.m3launcher;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import java.util.List;

public class LauncherAdapter extends BaseAdapter {
    private Context mContext;
    private List<LauncherItem> mItems;

    public LauncherAdapter(Context context, List<LauncherItem> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public int getCount() { return mItems.size(); }
    @Override
    public Object getItem(int position) { return mItems.get(position); }
    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            // Ensure the container is tall enough to accommodate the scale-up (60px)
            // and wide enough for the icons (55px)
            imageView.setLayoutParams(new Gallery.LayoutParams(51, 45));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else {
            imageView = (ImageView) convertView;
        }

        // Determine the target scale based on the selection
        int selectedPosition = ((Gallery) parent).getSelectedItemPosition();
        float targetScale = (position == selectedPosition) ? 1.0f : 0.75f;

        // scalin
        ScaleAnimation snapAnim = new ScaleAnimation(
                targetScale, targetScale, // ??
                targetScale, targetScale,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 1.0f
        );

        snapAnim.setDuration(0);
        snapAnim.setFillAfter(true);
        imageView.startAnimation(snapAnim);

        imageView.setTag(targetScale);

        imageView.setImageResource(mItems.get(position).iconResId);
        return imageView;
    }
}