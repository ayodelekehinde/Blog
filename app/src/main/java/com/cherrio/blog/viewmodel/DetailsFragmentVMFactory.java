package com.cherrio.blog.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cherrio.blog.models.Blog;

public class DetailsFragmentVMFactory implements ViewModelProvider.Factory {
    private Application application;
    private Blog blog;

    public DetailsFragmentVMFactory(Application application, Blog blog) {
        this.application = application;
        this.blog = blog;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DetailFragmentVM.class)){
            return (T) new DetailFragmentVM(application,blog);
        }
        throw new IllegalArgumentException("Cannot create Instance for this class");
    }
}
