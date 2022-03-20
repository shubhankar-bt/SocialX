package com.shubhankaranku.socialx.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shubhankaranku.socialx.Models.NewsHeadlines;
import com.shubhankaranku.socialx.R;
import com.shubhankaranku.socialx.SelectListener;
import com.shubhankaranku.socialx.customViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class customAdapter extends RecyclerView.Adapter<customViewHolder> {
    private Context context;
    private List<NewsHeadlines> headlines;
    private SelectListener listener;

    public customAdapter(Context context, List<NewsHeadlines> headlines, SelectListener listener) {
        this.context = context;
        this.headlines = headlines;
        this.listener = listener;
    }

    @NonNull
    @Override
    public customViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new customViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull customViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.text_title.setText(headlines.get(position).getTitle());
        holder.text_source.setText(headlines.get(position).getSource().getName());
        holder.text_description.setText(headlines.get(position).getDescription());
        holder.text_publish_time.setText(headlines.get(position).getPublishedAt());

        if (headlines.get(position).getUrlToImage() !=null ){
            Picasso.get().load(headlines.get(position).getUrlToImage()).into(holder.img_source);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnNewsClicked(headlines.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return headlines.size();
    }


}
