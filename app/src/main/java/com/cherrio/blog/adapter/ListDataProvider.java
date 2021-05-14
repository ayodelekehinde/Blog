package com.cherrio.blog.adapter;

import android.util.Log;

import com.cherrio.blog.models.Blog;
import com.cherrio.blog.retrofit.RetrofitClient;
import com.cherrio.blog.retrofit.XMLParser;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListDataProvider {
    private List<Blog> articles;
    private Blog blog;

    static ListDataProvider instance;
    private OnPassList onPassList;

    public static ListDataProvider getInstance(){
        if (instance == null)
            instance = new ListDataProvider();
        return instance;
    }


    public ListDataProvider getList(int page, int pageSize){
        Log.d("Retrofit","Page: "+page);
        Log.d("Retrofit","PageSize: "+pageSize);

        XMLParser parser = new XMLParser();
       RetrofitClient.getInstance()
               .getApi().getBlogPosts(false,page,pageSize).enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               if (response.isSuccessful()){
                   try {
                       onPassList.onPassList(parser.parseXML(response.body().string()));
                   } catch (Exception e) {
                       onPassList.onError();
                       e.printStackTrace();
                   }
               }else {
                   onPassList.onError();
               }
           }

           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {
               onPassList.onError();
           }
       });
       return this;
    }

    public ListDataProvider setOnPassList(OnPassList onPassList){
        this.onPassList = onPassList;
        return this;
    }

    public interface OnPassList{
        void onPassList(List<Blog> blogs);
        void onError();
    }

}
