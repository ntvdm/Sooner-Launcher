package com.ntvdm.m3launcher;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private Gallery mGallery;
    private TextView mAppLabel;
    private List<LauncherItem> mLauncherItems;
    private LauncherAdapter mAdapter;


    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAppLabel = (TextView) findViewById(R.id.app_label);
        mGallery = (Gallery) findViewById(R.id.launcher_gallery);

        int spacingPx = dpToPx(15);
        mGallery.setSpacing(0);

        initLauncherItems();

        mAdapter = new LauncherAdapter(this, mLauncherItems);
        mGallery.setAdapter(mAdapter);

        mGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAppLabel.setText(mLauncherItems.get(position).label);

                // for arrow visibility
                ImageView leftArrow = (ImageView) findViewById(R.id.arrow_left);
                ImageView rightArrow = (ImageView) findViewById(R.id.arrow_right);

                // hide left arrow if at start
                leftArrow.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);

                // hide right arrow if at end
                rightArrow.setVisibility(position == (mLauncherItems.size() - 1) ? View.INVISIBLE : View.VISIBLE);

                // scaling animation loop
                for (int i = 0; i < parent.getChildCount(); i++) {

                    View child = parent.getChildAt(i);
                    boolean isSelected = (child == view);
                    float targetScale = isSelected ? 1.0f : 0.75f;

                    // version check here too for the smmoooth scaling method
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                        child.setPivotX(child.getWidth() / 2f);
                        child.setPivotY(child.getHeight()); // anchor to bottom
                        child.animate()
                                .scaleX(targetScale)
                                .scaleY(targetScale)
                                .setDuration(200)
                                .start();
                    } else {
                        // ScaleAnimation for older
                        Float currentScaleTag = (child.getTag() instanceof Float) ? (Float) child.getTag() : 0.75f;

                        ScaleAnimation anim = new ScaleAnimation(
                                currentScaleTag, targetScale,
                                currentScaleTag, targetScale,
                                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                                ScaleAnimation.RELATIVE_TO_SELF, 1.0f
                        );
                        anim.setDuration(200);
                        anim.setFillAfter(true);
                        child.startAnimation(anim);
                        child.setTag(targetScale);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = mLauncherItems.get(position).intent;
                if (intent != null) {
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        // fallback handling
                    }
                }
            }
        });

        // extend the screen
        View root = findViewById(R.id.main_root);
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // pass it to gallery
                return mGallery.dispatchTouchEvent(event);
            }
        });
    }

    // wallpaper picker stuff

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, "Wallpaper settings");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            // trigger system wallpaper picker
            Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
            startActivity(Intent.createChooser(intent, "Select wallpaper :)"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // menu key function (duh)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            openOptionsMenu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initLauncherItems() {
        mLauncherItems = new ArrayList<LauncherItem>();

        mLauncherItems.add(new LauncherItem("Applications", R.drawable.app_all_apps, new Intent().setClassName(this, "com.ntvdm.m3launcher.AllAppsActivity")));

        mLauncherItems.add(new LauncherItem("Contacts", R.drawable.icon_contacts, new Intent(Intent.ACTION_VIEW, android.provider.Contacts.People.CONTENT_URI)));

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"));
        mLauncherItems.add(new LauncherItem("Browser", R.drawable.icon_browser, browserIntent));

        Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0"));
        mLauncherItems.add(new LauncherItem("Maps", R.drawable.icon_maps, mapsIntent));

        mLauncherItems.add(new LauncherItem("Dev Tools", R.drawable.icon_dev, new Intent(android.provider.Settings.ACTION_SETTINGS)));
    }
}