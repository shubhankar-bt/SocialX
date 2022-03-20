package com.shubhankaranku.socialx;

import com.shubhankaranku.socialx.Models.NewsHeadlines;

import java.util.List;

public interface OnFetchDataListener<NewsApiResponse> {
    void onFetchData(List<NewsHeadlines> list, String message);
    void onError(String message);

}
