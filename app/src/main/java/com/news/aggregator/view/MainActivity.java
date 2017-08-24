package com.news.aggregator.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.news.aggregator.NewsApp;
import com.news.aggregator.R;
import com.news.aggregator.model.NewsItem;
import com.news.aggregator.model.NewsSource;
import com.news.aggregator.usecase.GetNewsItems;
import com.news.aggregator.usecase.GetNewsSources;
import com.news.aggregator.util.Consts;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemSelectedListener,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.news_sources)
    Spinner spinner;
    @BindView(R.id.news_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.news_list)
    RecyclerView newsView;

    @Inject
    GetNewsItems getNewsItems;
    @Inject
    GetNewsSources getNewsSources;

    private ArrayAdapter<NewsSource> spinnerAdapter;
    private NewsRecyclerAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ((NewsApp) getApplication()).getAppComponent().inject(this);

        toolbar.setTitle(getString(R.string.category_general));
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_general);

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<NewsSource>());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        swipeRefreshLayout.setOnRefreshListener(this);

        newsView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsRecyclerAdapter(this);
        newsView.setAdapter(newsAdapter);

        loadSources(Consts.GENERAL);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        toolbar.setTitle(item.getTitle());

        String category;

        switch (item.getItemId()) {
            case R.id.nav_business:
                category = Consts.BUSINESS;
                break;
            case R.id.nav_entertainment:
                category = Consts.ENTERTAINMENT;
                break;
            case R.id.nav_technology:
                category = Consts.TECHNOLOGY;
                break;
            case R.id.nav_science_nature:
                category = Consts.SCIENCE_AND_NATURE;
                break;
            case R.id.nav_music:
                category = Consts.MUSIC;
                break;
            case R.id.nav_sports:
                category = Consts.SPORT;
                break;
            case R.id.nav_gaming:
                category = Consts.GAMING;
                break;
            default:
                category = Consts.GENERAL;
                break;
        }
        loadSources(category);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        loadNews((NewsSource) parent.getSelectedItem());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onRefresh() {
        loadNews((NewsSource) spinner.getSelectedItem());
    }

    private void loadSources(String category) {
        getNewsSources.call(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsSources -> {
                    spinnerAdapter.clear();
                    spinnerAdapter.addAll(newsSources);
                    if (spinner.getSelectedItemPosition() == 0) {
                        //in this case listener won't get triggered
                        loadNews((NewsSource)spinner.getSelectedItem());
                    } else {
                        spinner.setSelection(0);
                    }
                }, throwable -> clearSpinnerAndShowErrorToast());
    }

    private void loadNews(@NonNull  NewsSource newsSource) {
        getNewsItems.call(newsSource.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsItems -> {
                    swipeRefreshLayout.setRefreshing(false);
                    newsAdapter.setNewsItems(newsItems);
                    if (!newsItems.isEmpty()) {
                        newsView.smoothScrollToPosition(0);
                    }
                }, throwable -> clearListAndShowErrorToast());
    }

    private void clearSpinnerAndShowErrorToast() {
        spinnerAdapter.clear();
        Toast.makeText(MainActivity.this, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
    }

    private void clearListAndShowErrorToast() {
        swipeRefreshLayout.setRefreshing(false);
        newsAdapter.setNewsItems(new ArrayList<NewsItem>());
        Toast.makeText(MainActivity.this, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
    }

}
