package com.news.aggregator.model;

import java.util.Arrays;
import java.util.List;

public class NewsSource {

    private String id;
    private String name;
    private String description;
    private String url;
    private String category;
    private String language;
    private String country;
    private List<String> sortBysAvailable;

    public NewsSource(String id,
                      String name,
                      String description,
                      String url,
                      String category,
                      String language,
                      String country,
                      String[] sortBysAvailable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.category = category;
        this.language = language;
        this.country = country;
        this.sortBysAvailable = Arrays.asList(sortBysAvailable);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getCategory() {
        return category;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    public List<String> getSortBysAvailable() {
        return sortBysAvailable;
    }

    @Override
    public String toString() {
        return name;
    }
}
