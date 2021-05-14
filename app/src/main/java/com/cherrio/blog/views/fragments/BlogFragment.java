package com.cherrio.blog.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cherrio.blog.MainActivity;
import com.cherrio.blog.R;
import com.cherrio.blog.adapter.BlogAdapter;
import com.cherrio.blog.models.Blog;
import com.cherrio.blog.utils.NetworkState;
import com.cherrio.blog.utils.NetworkUtil;
import com.cherrio.blog.viewmodel.HomeVM;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class BlogFragment extends Fragment implements View.OnClickListener, BlogAdapter.OnRetryListener {


    private RecyclerView mListBlog;
    private BlogAdapter adapter;
    private HomeVM homeVM;
    private ShimmerFrameLayout mShimmer;
    private TextView mInternetNo;
    private Button mBtnRetry;
    private LinearLayout mErrorNetwork;
    private NavController controller;

    public static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110";
    public static final int ITEMS_PER_AD = 5;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initNetwork();
        controller = Navigation.findNavController(view);
        homeVM = new ViewModelProvider(requireActivity()).get(HomeVM.class);
        homeVM.getItemPagedList().observe(getViewLifecycleOwner(), new Observer<PagedList<Blog>>() {
            @Override
            public void onChanged(PagedList<Blog> blogs) {
                adapter.submitList(blogs);


            }
        });
        homeVM.getNetworkState().observe(getViewLifecycleOwner(), networkState -> {
            adapter.setNetworkState(networkState);
            if (networkState.getStatus() == NetworkState.Status.SUCCESS) {
                hideLoading();
                hideNoNetwork();
                showRecyclerView();
            } else if (networkState.getStatus() == NetworkState.Status.FAILED) {
                if (mListBlog.getVisibility() != View.VISIBLE) {
                    hideLoading();
                    showNoNetwork();
                }

            } else if (networkState.getStatus() == NetworkState.Status.RUNNING) {
                if (mListBlog.getVisibility() != View.VISIBLE) {
                    hideNoNetwork();
                    showLoading();
                }
            }

        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        initView(v);

        initRecyclerView();



        return v;
    }

    private void initView(@NonNull final View itemView) {
        mListBlog = itemView.findViewById(R.id.blog_list);
        mShimmer = itemView.findViewById(R.id.shimmer);
        mInternetNo = itemView.findViewById(R.id.no_internet);
        mBtnRetry = itemView.findViewById(R.id.retry_btn);
        mBtnRetry.setOnClickListener(this);
        mErrorNetwork = itemView.findViewById(R.id.network_error);
    }

    private void initRecyclerView() {
        adapter = new BlogAdapter(getActivity(), new BlogListener(), this,ITEMS_PER_AD);
        mListBlog.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListBlog.setHasFixedSize(true);
        mListBlog.setAdapter(adapter);

    }

    private void hideLoading() {
        mShimmer.stopShimmer();
        mShimmer.setVisibility(View.GONE);
    }

    private void showLoading() {
        mShimmer.startShimmer();
        mShimmer.setVisibility(View.VISIBLE);
    }

    private void showNoNetwork() {
        mErrorNetwork.setVisibility(View.VISIBLE);
    }

    private void hideNoNetwork() {
        mErrorNetwork.setVisibility(View.GONE);
    }

    private void showRecyclerView() {
        mListBlog.setVisibility(View.VISIBLE);
    }

    private void hideRecyclerView() {
        mListBlog.setVisibility(View.GONE);
    }

    private void initNetwork() {
        if (!checkNetwork()) {
            showNoNetwork();
            hideLoading();
        }
    }

    private boolean checkNetwork() {
        return new NetworkUtil(getActivity())
                .isConnected();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retry_btn:
                homeVM.refresh();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favourites:
                openFavourites();
                break;
            case R.id.menu_about:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openFavourites() {
        controller.navigate(R.id.action_blogFragment_to_favFragment);
    }

    @Override
    public void onRetry() {
        homeVM.retry();
    }



    private class BlogListener implements BlogAdapter.OnItemClicked {

        @Override
        public void onItemClicked(Blog blog, View view) {
            Navigation.findNavController(view).navigate(BlogFragmentDirections.actionBlogsFragmentToDetailsFragment(blog));
        }
    }
}
