package com.cherrio.blog.retrofit;

import com.cherrio.blog.models.Blog;
import com.cherrio.blog.models.Rss;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CallsApi {

    /*
    The @Query ?
    the @Path /

     */
    @GET("rss.xml")
    Call<ResponseBody> getBlogPosts(@Query("redirect")boolean isrRedirect,
                                    @Query("start-index") int start,
                                    @Query("max-results") int max);
}
