package com.shubhankaranku.socialx;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;

public class customViewHolder extends RecyclerView.ViewHolder {
    public TextView text_title, text_source, text_description,text_publish_time;
    public ImageView img_source;
    public CardView cardView;
    public customViewHolder(@NonNull View itemView) {
        super(itemView);

        text_title = itemView.findViewById(R.id.titleNews);
        text_source = itemView.findViewById(R.id.source);
        text_description = itemView.findViewById(R.id.description);
        img_source = itemView.findViewById(R.id.NewsImage);
        cardView = itemView.findViewById(R.id.itemCard);
        text_publish_time = itemView.findViewById(R.id.timePublish);

    }
}
