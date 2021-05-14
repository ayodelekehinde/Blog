package com.cherrio.blog.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name = "item", strict = false)
public class Blog implements Parcelable {

    public static DiffUtil.ItemCallback<Blog> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Blog>() {
                @Override
                public boolean areItemsTheSame(Blog oldItem, Blog newItem) {
                    return oldItem.id.equals(newItem.id);
                }

                @Override
                public boolean areContentsTheSame(Blog oldItem, Blog newItem) {
                    return oldItem.equals(newItem);
                }
            };


    public String title;
    public String id;

    public String totalItems;
    public String link;

    public String category;

    public String pubDate;


    public String description;


    public String author;

    public String media;

    public String enclosure;


    public Blog(){}

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
    }

    public static final Creator<Blog> CREATOR = new Creator<Blog>() {
        @Override
        public Blog createFromParcel(Parcel in) {
            return new Blog(in);
        }

        @Override
        public Blog[] newArray(int size) {
            return new Blog[size];
        }
    };

    private Blog(Parcel in) {
        media = in.readString();
        pubDate = in.readString();
        title = in.readString();
        description = in.readString();
        author = in.readString();
        category = in.readString();
        link = in.readString();
        enclosure = in.readString();
        id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pubDate);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(author);
        parcel.writeString(media);
        parcel.writeString(category);
        parcel.writeString(link);
        parcel.writeString(enclosure);
        parcel.writeString(id);

    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this)
            return true;

        Blog blog = (Blog) obj;
        return blog.id.equals(this.id);
    }
}
