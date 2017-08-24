package com.news.aggregator;

import android.app.Application;

import com.news.aggregator.usecase.UseCaseModule;

public class NewsApp extends Application {
    
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .useCaseModule(new UseCaseModule())
                .build();
        appComponent.inject(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
