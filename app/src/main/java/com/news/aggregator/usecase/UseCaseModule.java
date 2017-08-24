package com.news.aggregator.usecase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UseCaseModule {

    @Provides
    @Singleton
    GetNewsSources provideGetNewsSources(GetNewsSourcesImpl usecase) {
        return usecase;
    }

    @Provides
    @Singleton
    GetNewsArticles provideGetNewsArticles(GetNewsArticlesImpl usecase) {
        return usecase;
    }

}
