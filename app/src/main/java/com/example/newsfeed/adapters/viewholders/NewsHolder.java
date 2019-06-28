package com.example.newsfeed.adapters.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsfeed.R;
import com.example.newsfeed.data.models.News;

public class NewsHolder extends RecyclerView.ViewHolder {

    private TextView titleTv, dateTv;
    private ImageView imageIv;
    private RelativeLayout placeHolderLayout;
    private ConstraintLayout normalLayout;

    public NewsHolder(@NonNull View itemView) {
        super(itemView);
        titleTv = itemView.findViewById(R.id.web_title_tv);
        dateTv = itemView.findViewById(R.id.date_tv);
        imageIv = itemView.findViewById(R.id.image_iv);
        placeHolderLayout = itemView.findViewById(R.id.placeholder_layout);
        normalLayout = itemView.findViewById(R.id.normal_layout);
    }

    public void bind(News news, boolean isPlaceholder) {
        if (isPlaceholder) {
            normalLayout.setVisibility(View.INVISIBLE);
            placeHolderLayout.setVisibility(View.VISIBLE);
        } else {
            normalLayout.setVisibility(View.VISIBLE);
            placeHolderLayout.setVisibility(View.INVISIBLE);
            titleTv.setText(news.getWebTitle());
            dateTv.setText(news.getFormattedDate());
            Glide.with(itemView.getContext())
                    .load(news.getFields() != null ? news.getFields().getThumbnail() : null)
                    .into(imageIv);

            ViewCompat.setTransitionName(imageIv, news.getId());
        }

    }

    public ImageView getImageForAnimation() {
        return imageIv;
    }
}
