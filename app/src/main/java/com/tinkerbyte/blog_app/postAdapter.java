package com.tinkerbyte.blog_app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.regex.Pattern;

public class postAdapter extends RecyclerView.Adapter<postAdapter.postViewHolder> {

    private Context context;
    private List<Item>  items;

    public postAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }
    @NonNull
    @Override
    public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mLayoutInflater =  LayoutInflater.from(parent.getContext());
        View view = mLayoutInflater.inflate(R.layout.post_layout,parent,false);
        return new postViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull postViewHolder holder, int position) {
        final Item item = items.get(position);
        holder.postTitle.setText(item.getTitle());

        Document doc = Jsoup.parse(item.getContent());
        holder.postDescrition.setText(doc.text());

        Elements elements = doc.select("img");
        Glide.with(context).load(elements.get(0).attr("src")).into(holder.postImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(context,PostDetailActivity.class);
                mIntent.putExtra("URL",item.getUrl());
                context.startActivity(mIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class postViewHolder extends RecyclerView.ViewHolder{

        ImageView postImage;
        TextView postTitle;
        TextView postDescrition;

        public postViewHolder(View itemView) {
            super(itemView);
            postImage = (ImageView) itemView.findViewById(R.id.postImage);
            postTitle=(TextView)itemView.findViewById(R.id.postTitle);
            postDescrition=(TextView) itemView.findViewById(R.id.fullpostDescription);
        }
    }
}
