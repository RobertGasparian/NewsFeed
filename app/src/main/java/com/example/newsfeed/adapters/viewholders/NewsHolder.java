package com.example.newsfeed.adapters.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsfeed.R;
import com.example.newsfeed.data.models.News;

public class NewsHolder extends RecyclerView.ViewHolder {

    private TextView titleTv, dateTv;
    private ImageView imageIv;

    public NewsHolder(@NonNull View itemView) {
        super(itemView);
        titleTv = itemView.findViewById(R.id.web_title_tv);
        dateTv = itemView.findViewById(R.id.date_tv);
        imageIv = itemView.findViewById(R.id.image_iv);
    }

    public void bind(News news) {
        titleTv.setText(news.getWebTitle());
        dateTv.setText(news.getFormattedDate());
        Glide.with(itemView.getContext())
                .load(news.getFields().getThumbnail())
                .into(imageIv);

        ViewCompat.setTransitionName(imageIv, news.getId());
    }

    public ImageView getImageForAnimation() {
        return imageIv;
    }
}
