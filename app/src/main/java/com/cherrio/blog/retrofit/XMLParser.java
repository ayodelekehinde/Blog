

package com.cherrio.blog.retrofit;

import android.util.Log;

import com.cherrio.blog.models.Blog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class XMLParser  {

    private List<Blog> articles;
    private Blog blog;

    public XMLParser() {
        articles = new ArrayList<>();
        blog = new Blog();
    }


    public List<Blog> parseXML(String xml) throws Exception {

        int listCount = 0;
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

        factory.setNamespaceAware(false);
        XmlPullParser xmlPullParser = factory.newPullParser();

        xmlPullParser.setInput(new StringReader(xml));
        boolean insideItem = false;
        int eventType = xmlPullParser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {

            if (eventType == XmlPullParser.START_TAG) {
                if (xmlPullParser.getName().equalsIgnoreCase("openSearch:totalResults")){
                    String sTotal = xmlPullParser.nextText();
                    blog.setTotalItems(sTotal);
                }else if (xmlPullParser.getName().equalsIgnoreCase("item")) {
                    insideItem = true;

                }else if (xmlPullParser.getName().equalsIgnoreCase("guid")) {
                    if (insideItem) {
                        String id = xmlPullParser.nextText();
                        String newId = id.substring(id.lastIndexOf("-")+1);
                        blog.setId(newId);
                        Log.d(XMLParser.class.getSimpleName(), "Post ID: "+newId);
                    }

                } else if (xmlPullParser.getName().equalsIgnoreCase("title")) {
                    if (insideItem) {
                        String title = xmlPullParser.nextText();
                        blog.setTitle(title);
                        Log.d("Retrofit","Item Title: "+title);

                    }

                } else if (xmlPullParser.getName().equalsIgnoreCase("link")) {
                    if (insideItem) {
                        String link = xmlPullParser.nextText();
                        blog.setLink(link);
                    }

                } else if (xmlPullParser.getName().equalsIgnoreCase("dc:creator")) {
                    if (insideItem) {
                        blog.setAuthor("ViralTrend");
                    }

                } else if (xmlPullParser.getName().equalsIgnoreCase("category")) {
                    if (insideItem) {
                        String category = xmlPullParser.nextText();
                        blog.setCategory(category);
                    }

                } else if (xmlPullParser.getName().equalsIgnoreCase("media:thumbnail")) {
                    if (insideItem) {
                        String img = xmlPullParser.getAttributeValue(null, "url").replace("s72-c","s400-c");
                        blog.setMedia(img);
                    }

                } else if (xmlPullParser.getName().equalsIgnoreCase("description")) {
                    if (insideItem) {
                        String description = xmlPullParser.nextText();
                        if (blog.media == null) {
                            blog.setMedia(getImageUrl(description));
                        }
                        blog.setDescription(description);
                    }

                }  else if (xmlPullParser.getName().equalsIgnoreCase("pubDate")) {
                    Date pubDate = new Date(xmlPullParser.nextText());
                    blog.setPubDate(pubDate.toString());
                }

            } else if (eventType == XmlPullParser.END_TAG && xmlPullParser.getName().equalsIgnoreCase("item")) {
                insideItem = false;
                articles.add(blog);
                listCount++;
                blog = new Blog();

            }
            eventType = xmlPullParser.next();
        }
        Log.d("Retrofit","List: "+listCount);


        return articles;
    }


    /**
     * Finds the first img tag and get the src as featured image
     *
     * @param input The content in which to search for the tag
     * @return The url, if there is one
     */
    private String getImageUrl(String input) {

        String url = null;
        Pattern patternImg = Pattern.compile("(<img .*?>)");
        Matcher matcherImg = patternImg.matcher(input);
        if (matcherImg.find()) {
            String imgTag = matcherImg.group(1);
            Pattern patternLink = Pattern.compile("src\\s*=\\s*\"(.+?)\"");
            Matcher matcherLink = patternLink.matcher(imgTag);
            if (matcherLink.find()) {
                url = matcherLink.group(1);
            }
        }
        return url;
    }
}