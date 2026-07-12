package com.ntvdm.m3launcher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import java.util.List;

public class AllAppsActivity extends Activity {

    private GridView mGrid; //GridView over ListView kk
    private List<ResolveInfo> mInstalledApps;
    private int mSelectedItemPosition = 0; // track selected item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_apps);

        mGrid = (GridView) findViewById(R.id.apps_grid);

        PackageManager pm = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mInstalledApps = pm.queryIntentActivities(mainIntent, 0);

        AllAppsAdapter adapter = new AllAppsAdapter(this, mInstalledApps);
        mGrid.setAdapter(adapter);

        // update handler for le dpad controls (idk man i got this code from a forum)
        mGrid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedItemPosition = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchApp(position);
            }
        });

        mGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // del the one SELECTED (smart)
                mSelectedItemPosition = position;

                // bye bye app and epic system dialog
                uninstallSelectedApp();
                return true;
            }
        });
    }

    private void launchApp(int position) {
        ResolveInfo info = mInstalledApps.get(position);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(new ComponentName(
                info.activityInfo.applicationInfo.packageName,
                info.activityInfo.name));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(intent);
    }

    // options menu implemtetneitnieo

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 0, 0, "Uninstall app"); // cool menu when it just has 1 OPTION
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            uninstallSelectedApp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void uninstallSelectedApp() {
        ResolveInfo appInfo = mInstalledApps.get(mSelectedItemPosition);
        Uri packageUri = Uri.parse("package:" + appInfo.activityInfo.packageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
        startActivity(uninstallIntent);
    }

    // menu button handler, how else you gonna access it? (beam it?)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            openOptionsMenu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}