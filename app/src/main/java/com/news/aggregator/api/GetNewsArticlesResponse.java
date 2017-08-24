package com.news.aggregator.api;

import com.news.aggregator.model.NewsItem;

import java.util.List;

public class GetNewsArticlesResponse {

    private List<NewsItem> articles;

    public GetNewsArticlesResponse(List<NewsItem> articles) {
        this.articles = articles;
    }

    public List<NewsItem> getArticles() {
        return articles;
    }
}
