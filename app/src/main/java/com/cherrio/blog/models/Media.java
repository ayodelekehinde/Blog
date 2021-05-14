package com.cherrio.blog.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "media",strict = false)
public class Media {

    @Attribute(name = "url",required = false)
    public String image;

    public Media() {
    }

    public Media(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

}
