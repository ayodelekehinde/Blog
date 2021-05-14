package com.cherrio.blog.adapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.cherrio.blog.models.Blog;

public class BlogDataFactory extends DataSource.Factory<Integer, Blog> {

    private MutableLiveData<PageKeyedDataSource<Integer, Blog>> itemLiveDataSource = new MutableLiveData<>();
    private MutableLiveData<BlogDataSource> mutableLiveData;

    public BlogDataFactory() {
        mutableLiveData = new MutableLiveData<BlogDataSource>();
    }

    @NonNull
    @Override
    public DataSource create() {
        BlogDataSource itemDataSource = new BlogDataSource();
        mutableLiveData.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<BlogDataSource> getItemLiveDataSource() {
        return mutableLiveData;
    }

}
