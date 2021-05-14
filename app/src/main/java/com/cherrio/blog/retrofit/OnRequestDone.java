package com.cherrio.blog.retrofit;

import com.cherrio.blog.models.Blog;

import java.util.List;

public interface OnRequestDone {
    List<Blog> onDone(List<Blog> blogList);
}
