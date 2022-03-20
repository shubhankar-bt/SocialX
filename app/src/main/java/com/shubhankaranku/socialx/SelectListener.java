package com.shubhankaranku.socialx;

import android.view.MenuItem;

import com.shubhankaranku.socialx.Models.NewsHeadlines;

public interface SelectListener {
    void OnNewsClicked(NewsHeadlines headlines);

    void onMenuItemClick(MenuItem item);
}
