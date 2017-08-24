package com.news.aggregator.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.news.aggregator.R;
import com.news.aggregator.model.NewsArticle;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.PostViewHolder> implements View.OnClickListener {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
    private List<NewsArticle> newsArticles = new ArrayList<>();
    private Context context;
    private int imageHeight;
    private int imageWidth;

    NewsRecyclerAdapter(Context context) {
        this.context = context;
        this.imageHeight = context.getResources().getDimensionPixelSize(R.dimen.image_height);
        this.imageWidth = context.getResources().getDimensionPixelSize(R.dimen.image_width);
    }

    void setNewsArticles(@NonNull List<NewsArticle> newsArticles) {
        this.newsArticles = newsArticles;
        notifyDataSetChanged();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item_layout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        NewsArticle newsArticle = newsArticles.get(position);
        holder.itemView.setTag(newsArticle);
        holder.itemView.setOnClickListener(this);

        if (TextUtils.isEmpty(newsArticle.getAuthor())) {
            holder.author.setVisibility(View.GONE);
        } else {
            holder.author.setText( String.format(context.getString(R.string.author), newsArticle.getAuthor()));
            holder.author.setVisibility(View.VISIBLE);
        }

        if (newsArticle.getPublishedAt() == null) {
            holder.date.setVisibility(View.GONE);
        } else {
            holder.date.setText(simpleDateFormat.format(newsArticle.getPublishedAt()));
            holder.date.setVisibility(View.VISIBLE);
        }
        holder.text.setText(newsArticle.getTitle());

        if (TextUtils.isEmpty(newsArticle.getUrlToImage())) {
            holder.image.setImageResource(R.drawable.placeholder);
        } else {
            Picasso.with(context)
                    .load(Uri.parse(newsArticle.getUrlToImage()))
                    .resize(imageWidth, imageHeight)
                    .error(R.drawable.placeholder)
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return newsArticles.size();
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() instanceof NewsArticle) {
            String url = ((NewsArticle) view.getTag()).getUrl();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
        }
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_item_image)
        ImageView image;
        @BindView(R.id.news_item_author)
        TextView author;
        @BindView(R.id.news_item_date)
        TextView date;
        @BindView(R.id.news_item_text)
        TextView text;

        PostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
