package com.cherrio.blog.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "channel",strict = false)
public class Channel {



    @ElementList(name = "items",inline = true)
    private List<Blog> blogs;

    public Channel() {
    }

    public Channel(List<Blog> blogs) {
        this.blogs = blogs;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }
}
