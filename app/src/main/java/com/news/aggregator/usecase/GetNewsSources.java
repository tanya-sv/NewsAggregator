package com.news.aggregator.usecase;

import com.news.aggregator.api.GetNewsSourcesResponse;
import com.news.aggregator.api.NewsApi;
import com.news.aggregator.model.NewsSource;

import java.util.List;

import javax.inject.Inject;

import rx.Single;

public interface GetNewsSources {
    Single<List<NewsSource>> call(String category);
}

final class GetNewsSourcesImpl implements GetNewsSources {

    private NewsApi api;

    @Inject
    GetNewsSourcesImpl(NewsApi api) {
        this.api = api;
    }

    @Override
    public Single<List<NewsSource>> call(String category) {
        return api.getSources(category, "en").map(GetNewsSourcesResponse::getSources).toSingle();
    }
}
