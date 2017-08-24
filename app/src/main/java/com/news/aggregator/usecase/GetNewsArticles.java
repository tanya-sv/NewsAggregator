package com.news.aggregator.usecase;

import android.support.annotation.NonNull;

import com.news.aggregator.api.NewsApi;
import com.news.aggregator.model.NewsArticle;
import com.news.aggregator.model.NewsSource;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Single;

public interface GetNewsArticles {
    Single<List<NewsArticle>> call(NewsSource source);
}

final class GetNewsArticlesImpl implements GetNewsArticles {

    private NewsApi api;

    @Inject
    GetNewsArticlesImpl(NewsApi api) {
        this.api = api;
    }

    @Override
    public Single<List<NewsArticle>> call(@NonNull NewsSource source) {
        String sortBy = "top";
        if (!source.getSortBysAvailable().contains(sortBy) && source.getSortBysAvailable().size() > 0) {
            sortBy = source.getSortBysAvailable().get(0);
        }
        return api.getArticles(source.getId(), sortBy)
                .map(response -> {
                    List<NewsArticle> articles = response.getArticles();
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
