package com.cherrio.blog;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.cherrio.blog.models.Blog;
import com.cherrio.blog.retrofit.RetrofitClient;
import com.cherrio.blog.retrofit.XMLParser;
import com.cherrio.blog.utils.FileUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.StringReader;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationWorker extends Worker {

    private NotificationManager manager;
    private Context context;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    @NonNull
    @Override
    public Result doWork() {
        File notif = FileUtil.notifFile();

        try {

            Call<ResponseBody> call = RetrofitClient.getInstance().getApi().getBlogPosts(false, 1, 1);
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                Blog blog = parseXml(response.body().string());
                Log.d(NotificationWorker.class.getSimpleName(), "The blog: " + blog.title);

                    String id = FileUtil.readFile(notif.getAbsolutePath());
                    if (!id.equals(blog.id)) {
                        showNotification(blog);
                        FileUtil.writeFile(notif.getAbsolutePath(), blog.id);
                    }
                return Result.success();
            } else {
                return Result.retry();
            }
        }catch (Throwable e){
            return Result.failure();
        }

    }
    private void showNotification(Blog blog){
        String channelId = "viral_channel";
        String channelName = "viral_name";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channelId)
                .setContentTitle(blog.title)
                .setContentText(Html.fromHtml(blog.description))
                .setSmallIcon(R.mipmap.ic_launcher_round);

        manager.notify(1, builder.build());
    }

    private Blog parseXml(String xml) throws Exception {
        Blog blog = new Blog();
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
                        blog.setDescription(description);
                    }

                }  else if (xmlPullParser.getName().equalsIgnoreCase("pubDate")) {
                    Date pubDate = new Date(xmlPullParser.nextText());
                    blog.setPubDate(pubDate.toString());
                }

            } else if (eventType == XmlPullParser.END_TAG && xmlPullParser.getName().equalsIgnoreCase("item")) {
                insideItem = false;

            }
            eventType = xmlPullParser.next();
        }


        return blog;

    }
}
