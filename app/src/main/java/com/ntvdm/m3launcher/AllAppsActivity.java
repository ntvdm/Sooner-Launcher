package com.ntvdm.m3launcher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import java.util.List;

public class AllAppsActivity extends Activity {

    private GridView mGrid;
    private List<ResolveInfo> mInstalledApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_apps);

        mGrid = (GridView) findViewById(R.id.apps_grid);

        // get apps via PackageManager
        PackageManager pm = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mInstalledApps = pm.queryIntentActivities(mainIntent, 0);

        AllAppsAdapter adapter = new AllAppsAdapter(this, mInstalledApps);
        mGrid.setAdapter(adapter);

        // to make launch them with ok
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResolveInfo info = mInstalledApps.get(position);

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setComponent(new ComponentName(
                        info.activityInfo.applicationInfo.packageName,
                        info.activityInfo.name));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

                startActivity(intent);
            }
        });
    }
}