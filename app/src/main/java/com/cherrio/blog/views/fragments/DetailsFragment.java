package com.cherrio.blog.views.fragments;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cherrio.blog.MainActivity;
import com.cherrio.blog.R;
import com.cherrio.blog.models.Blog;
import com.cherrio.blog.utils.FileUtil;
import com.cherrio.blog.utils.ToastMessage;
import com.cherrio.blog.viewmodel.DetailFragmentVM;
import com.cherrio.blog.viewmodel.DetailsFragmentVMFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.File;
import java.util.List;

public class DetailsFragment extends Fragment {


    private AppCompatTextView mHeadlineBlog;
    private AppCompatImageView mImageBlog;
    private AppCompatTextView mDateBlog;
    private AppCompatTextView mCategoryBlog;
    private HtmlTextView mContentBlog;
    private boolean isSaved = false;
    private DetailFragmentVM detailFragmentVM;
    Blog blog;
    MenuItem save;
    MenuItem saved;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_details, container, false);
        blog = DetailsFragmentArgs.fromBundle(requireArguments()).getDetailFragmentArgs();
        initView(v);
        Application application = getActivity().getApplication();
        checkIsSaved(blog);
        DetailsFragmentVMFactory factory = new DetailsFragmentVMFactory(application, blog);
        detailFragmentVM = new ViewModelProvider(this, factory).get(DetailFragmentVM.class);



        detailFragmentVM.getBlog().observe(getViewLifecycleOwner(), blog -> {
            mHeadlineBlog.setText(blog.title);
            mCategoryBlog.setText(blog.category);
            mDateBlog.setText(blog.pubDate);
            mContentBlog.setHtml(blog.description, new HtmlHttpImageGetter(mContentBlog));
            Picasso.get().load(blog.media).into(mImageBlog);
        });


        return v;

    }

    private void checkIsSaved(Blog b) {
        File savedPosts = FileUtil.saveFile();
        if (savedPosts.exists()) {
            List<Blog> blogList = new Gson().fromJson(FileUtil.readFile(savedPosts.getAbsolutePath()), new TypeToken<List<Blog>>() {
            }.getType());
            for (Blog miBlog : blogList) {
                if (b.id.equals(miBlog.id)) {
                    isSaved = true;
                    getActivity().invalidateOptionsMenu();
                }
            }
        } else {
            isSaved = false;
            getActivity().invalidateOptionsMenu();
        }
    }

    private void initView(@NonNull final View itemView) {
        mHeadlineBlog = (AppCompatTextView) itemView.findViewById(R.id.blog_headline);
        mImageBlog = (AppCompatImageView) itemView.findViewById(R.id.blog_image);
        mDateBlog = (AppCompatTextView) itemView.findViewById(R.id.blog_date);
        mCategoryBlog = (AppCompatTextView) itemView.findViewById(R.id.blog_category);
        mContentBlog = itemView.findViewById(R.id.blog_content);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(blog.title);
    }


    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.details_menu, menu);
        save = menu.findItem(R.id.action_save);
        saved = menu.findItem(R.id.action_saved);
        if (isSaved) {
            saved.setVisible(true);
        } else {
            save.setVisible(true);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveBlog();
                break;
            case R.id.action_saved:
                removeBlog();
                break;
            case R.id.action_share:
                share();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveBlog() {
        save.setVisible(false);
        saved.setVisible(true);
        ToastMessage.show(getActivity(), "Added to Favourites");
        detailFragmentVM.saveBlog(blog);
    }

    private void removeBlog() {
        saved.setVisible(false);
        save.setVisible(true);
        ToastMessage.show(getActivity(), "Removed from Favourites");
        detailFragmentVM.removeBlog(blog);

    }

    private void share() {

    }


    @Override
    public void onDestroyView() {
        blog = null;
        detailFragmentVM.getIsSaved().removeObservers(getViewLifecycleOwner());
        super.onDestroyView();
    }
}
