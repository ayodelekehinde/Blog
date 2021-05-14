package com.cherrio.blog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cherrio.blog.R;
import com.cherrio.blog.models.Blog;
import com.cherrio.blog.utils.FileUtil;
import com.cherrio.blog.utils.NetworkState;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class FavBlogAdapter extends RecyclerView.Adapter<FavBlogAdapter.BlogViewHolder> {

    private Context context;
    OnItemClicked onItemClicked;
    private List<Blog> blogList = new ArrayList<>();
    private Fragment fragment;



    public FavBlogAdapter(Context ctx, Fragment fragment, OnItemClicked onItemClicked) {
        context = ctx;
        this.onItemClicked = onItemClicked;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.blog_item, parent, false);
            return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
            Blog blog = blogList.get(position);
            holder.headline.setText(blog.title);
            holder.date.setText(blog.pubDate);
            Picasso.get().load(blog.media).into(holder.image);

    }

    public void addList(List<Blog> blogs){
        this.blogList = blogs;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public void deleteItem(int pos){
        Blog blogDelete = blogList.get(pos);
        int position = pos;
        blogList.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar(position,blogDelete);
    }
    private void showUndoSnackbar(int pos, Blog blog){
        View view = fragment.getView().findViewById(R.id.cod);
        Snackbar snackbar = Snackbar.make(view,"Want to undo delete?",Snackbar.LENGTH_LONG);
        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onShown(Snackbar sb) {
            }

            @Override
            public void onDismissed(Snackbar transientBottomBar, @DismissEvent int event) {
                if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT){
                    delete(blog);
                }
            }
        });
        snackbar.setAction("Undo", view1 -> {
           undoDelete(pos,blog);

        });
        snackbar.show();


    }
    private void delete(Blog blog){
        File postsFile = FileUtil.saveFile();
        if (postsFile.exists()){
            List<Blog> blogList = new Gson().fromJson(FileUtil.readFile(postsFile.getAbsolutePath()), new TypeToken<List<Blog>>() {
            }.getType());
            for(ListIterator<Blog> iterator = blogList.listIterator(); iterator.hasNext();){
                Blog miBlog = iterator.next();
                if (blog.id.equals(miBlog.id)){
                    iterator.remove();
                    FileUtil.writeFile(postsFile.getAbsolutePath(),new Gson().toJson(blogList));

                }
            }
        }
    }
    private void undoDelete(int pos, Blog blog){
        blogList.add(pos,blog);
        notifyItemInserted(pos);
    }


    class BlogViewHolder extends RecyclerView.ViewHolder{
        AppCompatTextView headline, date;
        AppCompatImageView image;
        public BlogViewHolder(@NonNull final View itemView) {
            super(itemView);
            headline = itemView.findViewById(R.id.blog_headline);
            image = itemView.findViewById(R.id.blog_image);
            date = itemView.findViewById(R.id.blog_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClicked.onItemClicked(blogList.get(getAdapterPosition()),itemView);
                }
            });

        }
    }


    public interface OnItemClicked{
        void onItemClicked(Blog blog, View view);
    }

}
