package com.news.aggregator.usecase;

import com.news.aggregator.api.NewsApi;
import com.news.aggregator.model.NewsItem;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Single;

public interface GetNewsItems {
    Single<List<NewsItem>> call(String source);
}

final class GetNewsItemsImpl implements GetNewsItems {

    private NewsApi api;

    @Inject
    public GetNewsItemsImpl(NewsApi api) {
        this.api = api;
    }

    @Override
    public Single<List<NewsItem>> call(String source) {
        return api.getArticles("875419f0c3534b6b96a734e965e53911", source, "top")
                .map(response -> {
                    List<NewsItem> articles = response.getArticles();
                    //sort from newest to oldest
                    Collections.sort(articles, (o1, o2) -> {
                        if (o1.getPublishedAt() != null && o2.getPublishedAt() != null) {
                            return o2.getPublishedAt().compareTo(o1.getPublishedAt());
                        } else
                            return 0;
                    });
                    return articles;
                }).toSingle();
    }
}
