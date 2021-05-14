package com.cherrio.blog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cherrio.blog.R;
import com.cherrio.blog.models.Blog;
import com.cherrio.blog.utils.NetworkState;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BlogAdapter extends PagedListAdapter<Blog, RecyclerView.ViewHolder> {

    private Context context;
    OnItemClicked onItemClicked;
    private final int BLOG_VIEW = 0;
    private final int LOADING_VIEW = 1;
    private final int ADS_VIEW = 2;
    private NetworkState networkState;
    private OnRetryListener retryListener;
    private int space;


    public BlogAdapter(Context ctx, OnItemClicked onItemClicked, OnRetryListener retryListener,int space) {
        super(Blog.DIFF_CALLBACK);
        context = ctx;
        this.onItemClicked = onItemClicked;
        this.retryListener = retryListener;
        this.space = space;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == BLOG_VIEW){
            view = LayoutInflater.from(context).inflate(R.layout.blog_item, parent, false);
            return new BlogViewHolder(view);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.loading_item, parent, false);
            return new BlogLoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof BlogViewHolder) {
            BlogViewHolder blogViewHolder = (BlogViewHolder) holder;
            Blog blog = getItem(position);
            blogViewHolder.headline.setText(blog.title);
            blogViewHolder.date.setText(blog.pubDate);
            Picasso.get().load(blog.media).into(blogViewHolder.image);
        }else if (holder instanceof BlogLoadingViewHolder){
            BlogLoadingViewHolder loadingViewHolder = (BlogLoadingViewHolder) holder;
            loadingViewHolder.bind(networkState);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() -1){
            return LOADING_VIEW;
        }else {
            return BLOG_VIEW;
        }
    }



    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount()+1);

            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);

        }
    }

    private boolean hasExtraRow() {
        if (networkState != null && networkState.getStatus() != NetworkState.Status.SUCCESS) {
            return true;
        } else {
            return false;
        }
    }

    class BlogViewHolder extends RecyclerView.ViewHolder{
        AppCompatTextView headline, date;
        AppCompatImageView image;
        public BlogViewHolder(@NonNull final View itemView) {
            super(itemView);
            headline = itemView.findViewById(R.id.blog_headline);
            image = itemView.findViewById(R.id.blog_image);
            date = itemView.findViewById(R.id.blog_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClicked.onItemClicked((Blog)getItem(getAdapterPosition()),itemView);
                }
            });
        }
    }
    class BlogLoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar loading;
        TextView msg;
        Button retry;

        public BlogLoadingViewHolder(@NonNull final View itemView) {
            super(itemView);
            loading = itemView.findViewById(R.id.loading_bar);
            msg = itemView.findViewById(R.id.no_internet);
            retry = itemView.findViewById(R.id.retry_btn);

            retry.setOnClickListener(view -> {
                retryListener.onRetry();
            });
        }
        void bind(NetworkState state){
            if (state != null && state.getStatus() == NetworkState.Status.RUNNING){
                showLoading();
            }else {
                hideLoading();
            }
            if (state != null && state.getStatus() == NetworkState.Status.FAILED){
                showError();
            }else {
                hideError();
            }
        }
        private void showError(){
            msg.setVisibility(View.VISIBLE);
            retry.setVisibility(View.VISIBLE);
        }
        private void hideError(){
            msg.setVisibility(View.GONE);
            retry.setVisibility(View.GONE);
        }
        private void showLoading(){
            loading.setVisibility(View.VISIBLE);
        }
        private void hideLoading(){
            loading.setVisibility(View.GONE);
        }

    }



    public interface OnItemClicked{
        void onItemClicked(Blog blog, View view);
    }
    public interface OnRetryListener{
        void onRetry();
    }
}
