package com.cherrio.blog.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.cherrio.blog.models.Blog;
import com.cherrio.blog.repos.SavedPostsRepo;

public class DetailFragmentVM extends AndroidViewModel {

    private MutableLiveData<Blog> blogMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSaved = new MutableLiveData<>();


    public DetailFragmentVM(@NonNull Application application, Blog blog) {
        super(application);
        isSaved.postValue(SavedPostsRepo.getInstance().getIsSaved(blog));
        blogMutableLiveData.postValue(blog);
    }
    public MutableLiveData<Blog> getBlog(){
        return blogMutableLiveData;
    }
    public MutableLiveData<Boolean> getIsSaved(){
       return isSaved;
    }
    public void saveBlog(Blog blog){
        SavedPostsRepo.getInstance().saveBlog(blog);
    }
    public void removeBlog(Blog blog){
        SavedPostsRepo.getInstance().removeBlog(blog);
    }


}
