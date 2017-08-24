package com.news.aggregator.api;

import com.news.aggregator.model.NewsSource;

import java.util.List;

public class GetNewsSourcesResponse {

    private List<NewsSource> sources;

    public GetNewsSourcesResponse(List<NewsSource> sources) {
        this.sources = sources;
    }

    public List<NewsSource> getSources() {
        return sources;
    }
}
