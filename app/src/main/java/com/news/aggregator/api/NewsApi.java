package com.news.aggregator.api;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface NewsApi {

    @GET("sources")
    Observable<GetNewsSourcesResponse> getSources(@Query("category") String category, @Query("language") String language);

    @GET("articles")
    Observable<GetNewsArticlesResponse> getArticles(@Query("source") String source, @Query("sortBy") String sortBy);
}
