package com.ntvdm.m3launcher;

import android.content.Intent;

public class LauncherItem {
    public String label;
    public int iconResId;
    public Intent intent;

    public LauncherItem(String label, int iconResId, Intent intent) {
        this.label = label;
        this.iconResId = iconResId;
        this.intent = intent;
    }
}