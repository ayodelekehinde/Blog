package com.cherrio.blog.adapter;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PositionalDataSource;

import com.cherrio.blog.BlogApplication;
import com.cherrio.blog.models.Blog;
import com.cherrio.blog.utils.NetworkState;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.List;

import static com.cherrio.blog.views.fragments.BlogFragment.AD_UNIT_ID;
import static com.cherrio.blog.views.fragments.BlogFragment.ITEMS_PER_AD;

public class BlogDataSource extends PageKeyedDataSource<Integer,Blog> {

    public static final int PAGE_SIZE = 10;
    private static final int FIRST_PAGE = 1;

    private MutableLiveData networkState = new MutableLiveData();
    private MutableLiveData initialLoading = new MutableLiveData();

    private LoadParams<Integer> loadParams;
    private LoadCallback<Integer,Blog> callback;

    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Blog> callback) {
        Log.d("DataSource","Load Size: "+params.requestedLoadSize);

        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);

        ListDataProvider.getInstance().getList(FIRST_PAGE,PAGE_SIZE).setOnPassList(new ListDataProvider.OnPassList() {
            @Override
            public void onPassList(List<Blog> blogs) {

                callback.onResult(blogs,null,PAGE_SIZE+1);
                initialLoading.postValue(NetworkState.LOADED);
                networkState.postValue(NetworkState.LOADED);
            }

            @Override
            public void onError() {
                initialLoading.postValue(new NetworkState(NetworkState.Status.FAILED,"failed"));
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED,"failed"));
            }
        });



    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Blog> callback) {


    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Blog> callback) {
        this.callback = callback;
        this.loadParams = params;
        networkState.postValue(NetworkState.LOADING);
        ListDataProvider.getInstance().getList(params.key+1,params.requestedLoadSize).setOnPassList(new ListDataProvider.OnPassList() {
            @Override
            public void onPassList(List<Blog> blogs) {
                Log.d("DataSource","Total: "+blogs.get(0).totalItems);
                String totalItems = blogs.get(0).totalItems;

                Integer nexKey = (String.valueOf(params.key).equals(totalItems)) ? null: params.key+PAGE_SIZE;
                callback.onResult(blogs,nexKey);
                networkState.postValue(NetworkState.LOADED);

            }

            @Override
            public void onError() {
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED,"failed"));

            }
        });

    }



    public void retryPagination(){
        loadAfter(loadParams,callback);
    }

}
