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

import com.news.aggregator.AppComponent;
import com.news.aggregator.R;
import com.news.aggregator.model.NewsItem;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.PostViewHolder> implements View.OnClickListener {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
    private List<NewsItem> newsItems = new ArrayList<>();
    private Context context;
    private int imageHeight;
    private int imageWidth;

    NewsRecyclerAdapter(Context context) {
        this.context = context;
        this.imageHeight = context.getResources().getDimensionPixelSize(R.dimen.image_height);
        this.imageWidth = context.getResources().getDimensionPixelSize(R.dimen.image_width);
    }

    void setNewsItems(@NonNull List<NewsItem> newsItems) {
        this.newsItems = newsItems;
        notifyDataSetChanged();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item_layout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        NewsItem newsItem = newsItems.get(position);
        holder.itemView.setTag(newsItem);
        holder.itemView.setOnClickListener(this);

        if (TextUtils.isEmpty(newsItem.getAuthor())) {
            holder.author.setVisibility(View.GONE);
        } else {
            holder.author.setText( String.format(context.getString(R.string.author), newsItem.getAuthor()));
            holder.author.setVisibility(View.VISIBLE);
        }

        if (newsItem.getPublishedAt() == null) {
            holder.date.setVisibility(View.GONE);
        } else {
            holder.date.setText(simpleDateFormat.format(newsItem.getPublishedAt()));
            holder.date.setVisibility(View.VISIBLE);
        }
        holder.text.setText(newsItem.getTitle());

        if (TextUtils.isEmpty(newsItem.getUrlToImage())) {
            holder.image.setImageResource(R.drawable.placeholder);
        } else {
            Picasso.with(context)
                    .load(Uri.parse(newsItem.getUrlToImage()))
                    .resize(imageWidth, imageHeight)
                    .error(R.drawable.placeholder)
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() instanceof NewsItem) {
            String url = ((NewsItem) view.getTag()).getUrl();
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
