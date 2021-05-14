package com.cherrio.blog.viewmodel;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.cherrio.blog.adapter.BlogDataFactory;
import com.cherrio.blog.adapter.BlogDataSource;
import com.cherrio.blog.models.Blog;
import com.cherrio.blog.utils.NetworkState;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeVM extends ViewModel {

    LiveData<PagedList<Blog>> itemPagedList;

    private Executor executor;
    LiveData<NetworkState> networkState;
    BlogDataFactory blogDataFactory;



    public HomeVM() {
        blogDataFactory = new BlogDataFactory();
        init();
    }
    private void init(){
        executor = Executors.newFixedThreadPool(5);

        networkState = Transformations.switchMap(blogDataFactory.getItemLiveDataSource(),
                dataSource -> dataSource.getNetworkState());

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(BlogDataSource.PAGE_SIZE)
                        .build();

        itemPagedList = (new LivePagedListBuilder(blogDataFactory, config))
                .setFetchExecutor(executor)
                .build();
    }
    public LiveData<PagedList<Blog>> getItemPagedList(){
        return itemPagedList;
    }

    public void retry(){
        blogDataFactory.getItemLiveDataSource().getValue().retryPagination();
    }
    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }
    public void refresh(){
        blogDataFactory.getItemLiveDataSource().getValue().invalidate();
    }


}
