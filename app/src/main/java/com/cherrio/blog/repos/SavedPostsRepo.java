package com.cherrio.blog.repos;

import androidx.lifecycle.MutableLiveData;

import com.cherrio.blog.models.Blog;
import com.cherrio.blog.utils.FileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class SavedPostsRepo {

    private static SavedPostsRepo instance;

    private boolean isSaved = false;

    public static SavedPostsRepo getInstance(){
        if (instance == null)
            instance = new SavedPostsRepo();
        return instance;
    }

    public boolean getIsSaved(Blog blog){
        return check(blog);
    }

    private boolean check(Blog newBlog){
        boolean isSaved = false;
        File file = new File(FileUtil.getExternalStorageDir().concat("/ViralTrend/posts.json"));
        if (file.exists()) {
            List<Blog> blogList = new Gson().fromJson(FileUtil.readFile(file.getAbsolutePath()), new TypeToken<List<Blog>>() {
            }.getType());
            for (Blog blog : blogList) {
                if (newBlog.id.equals(blog.id)) {
                    isSaved = true;

                } else {
                    isSaved = false;
                }
            }
        }
        return isSaved;
    }

    public void saveBlog(Blog blog){
        File postsFile = FileUtil.saveFile();
        if (postsFile.exists()){
            List<Blog> blogList = new Gson().fromJson(FileUtil.readFile(postsFile.getAbsolutePath()), new TypeToken<List<Blog>>() {
            }.getType());
            blogList.add(blog);
            String newJson = new Gson().toJson(blogList);
            FileUtil.writeFile(postsFile.getAbsolutePath(),newJson);
        }else {
            List<Blog> newBlogList = new ArrayList<>();
            newBlogList.add(blog);
            String json = new Gson().toJson(newBlogList);
            FileUtil.writeFile(postsFile.getAbsolutePath(),json);
        }
    }

    public void removeBlog(Blog blog){
        File postsFile = FileUtil.saveFile();
        if (postsFile.exists()){
            List<Blog> blogList = new Gson().fromJson(FileUtil.readFile(postsFile.getAbsolutePath()), new TypeToken<List<Blog>>() {
            }.getType());
            for(ListIterator<Blog> iterator = blogList.listIterator(); iterator.hasNext();){
                Blog miBlog = iterator.next();
                if (blog.id.equals(miBlog.id)){
                    iterator.remove();
                    FileUtil.writeFile(postsFile.getAbsolutePath(),new Gson().toJson(blogList));

                }
            }
        }

    }
}
