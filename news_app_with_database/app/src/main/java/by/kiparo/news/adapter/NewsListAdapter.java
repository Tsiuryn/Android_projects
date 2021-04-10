package by.kiparo.news.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.imagepipeline.request.ImageRequest;

import java.util.List;

import by.kiparo.news.R;
import by.kiparo.news.database.News;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsListHolder> {

    private List<News> newsItemList;
    private OnClickListener onClickListener;

    public NewsListAdapter(List<News> newsItemList, OnClickListener onClickListener) {
        this.newsItemList = newsItemList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public NewsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsListHolder(
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(R.layout.list_item_news, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NewsListHolder holder, int position) {

        final News news = newsItemList.get(position);

        String thumbnailURL = news.getImageUrl();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onItemClick(news);
            }
        });
        holder.newsTitle.setText(news.getTitle());
        DraweeController draweeController = Fresco.newDraweeControllerBuilder().setImageRequest(ImageRequest.fromUri
                (Uri.parse(thumbnailURL))).setOldController(holder.imageView.getController()).build();
        holder.imageView.setController(draweeController);
    }

    @Override
    public int getItemCount() {
        return newsItemList.size();
    }

    public void updateAdapter (List<News> newList){
        this.newsItemList = newList;
        notifyDataSetChanged();
    }

    static class NewsListHolder extends RecyclerView.ViewHolder {
        TextView newsTitle;
        DraweeView imageView;

        NewsListHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.news_title);
            imageView = itemView.findViewById(R.id.news_item_image);
        }
    }

    public interface OnClickListener {
        void onItemClick(News newsEntity);
    }
}
