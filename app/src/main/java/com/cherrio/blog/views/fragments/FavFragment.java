package com.cherrio.blog.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cherrio.blog.R;
import com.cherrio.blog.adapter.BlogAdapter;
import com.cherrio.blog.adapter.FavBlogAdapter;
import com.cherrio.blog.adapter.SwipeToDeleteCallback;
import com.cherrio.blog.models.Blog;
import com.cherrio.blog.utils.FileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.List;

public class FavFragment extends Fragment {

    private RecyclerView mListBlog;
    private FavBlogAdapter adapter;
    private LinearLayout mErrorNetwork;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadBookmark();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_fav, container, false);
        initView(v);
        initRecyclerView();

        return v;
    }

    private void loadBookmark(){
        File savedPosts = FileUtil.saveFile();
        if (savedPosts.exists()){
            List<Blog> blogList = new Gson().fromJson(FileUtil.readFile(savedPosts.getAbsolutePath()),new TypeToken<List<Blog>>(){}.getType());
            if (blogList.size() > 0) {
                adapter.addList(blogList);
            }else {
               mErrorNetwork.setVisibility(View.VISIBLE);
            }
        }else {
            mErrorNetwork.setVisibility(View.VISIBLE);
        }
    }

    private void initView(@NonNull final View itemView) {
        mListBlog = itemView.findViewById(R.id.blog_list);
        mErrorNetwork =  itemView.findViewById(R.id.network_error);
    }

    private void initRecyclerView() {
        adapter = new FavBlogAdapter(getActivity(),this, new FavFragment.BlogListener());
        mListBlog.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemTouchHelper helper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        helper.attachToRecyclerView(mListBlog);
        mListBlog.setHasFixedSize(true);
        mListBlog.setAdapter(adapter);

    }

    private class BlogListener implements FavBlogAdapter.OnItemClicked {

        @Override
        public void onItemClicked(Blog blog, View view) {
            Navigation.findNavController(view).navigate(FavFragmentDirections.actionFavFragmentToDetailsFragment(blog));
        }
    }
}
