package com.news.aggregator;

import com.news.aggregator.usecase.GetNewsItems;
import com.news.aggregator.usecase.GetNewsSources;
import com.news.aggregator.usecase.UseCaseModule;
import com.news.aggregator.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { AppModule.class, UseCaseModule.class })
public interface AppComponent {

    void inject(NewsApp app);

    void inject(MainActivity mainActivity);

    GetNewsSources exposeGetNewsSources();

    GetNewsItems exposeGetNewsItems();

}
