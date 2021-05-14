package com.cherrio.blog.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.List;

@Root(name = "rss",strict = false)
public class Rss {

    @Element(name = "channel")
    public Channel channel;

    public Rss() {
    }

    public Rss(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }



}
