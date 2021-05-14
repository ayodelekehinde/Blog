package com.cherrio.blog.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "enclosure", strict = false)
public class Enclosure {

    @Attribute(name = "url")
    public String url;

    public Enclosure() {
    }

    public Enclosure(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
