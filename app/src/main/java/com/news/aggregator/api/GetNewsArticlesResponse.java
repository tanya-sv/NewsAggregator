package com.news.aggregator.api;

import com.news.aggregator.model.NewsArticle;

import java.util.List;

public class GetNewsArticlesResponse {

    private List<NewsArticle> articles;

    public GetNewsArticlesResponse(List<NewsArticle> articles) {
        this.articles = articles;
    }

    public List<NewsArticle> getArticles() {
        return articles;
    }
}
