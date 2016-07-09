package com.example.angelshao.zhihuimitate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.angelshao.zhihuimitate.R;
import com.example.angelshao.zhihuimitate.activity.MainActivity;
import com.example.angelshao.zhihuimitate.entities.StoriesEntity;
import com.example.angelshao.zhihuimitate.utils.Constant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by David-Ann on 2016/7/8 0008.
 */
public class MainNewsItemAdapter extends BaseAdapter {

    public static final int TYPE_TOPIC = 0;
    public static final int TYPE_STORIES = 1;

    private List<StoriesEntity> stories;
    private Context context;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private boolean isLight = true;

    public MainNewsItemAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.stories = new ArrayList<>();
        this.mImageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {

        int type = getItem(position).getType();
        if (type == Constant.TOPIC) {
            return TYPE_TOPIC;
        } else {
            return TYPE_STORIES;
        }
    }

    @Override
    public int getCount() {
        return stories == null ? 0 : stories.size();
    }

    @Override
    public StoriesEntity getItem(int position) {
        return stories == null ? null : stories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return stories == null ? 0 : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (getItemViewType(position) == TYPE_TOPIC) {
            TopicViewHolder viewHolder;
            if (convertView != null) {
                viewHolder = (TopicViewHolder) convertView.getTag();
            } else {
                convertView = mInflater.inflate(R.layout.main_news_item_date, parent, false);
                viewHolder = new TopicViewHolder(convertView);
                convertView.setTag(viewHolder);
            }

            TextView tvTopic = viewHolder.tvTopic;
            tvTopic.setText(getItem(position).getTitle());
            tvTopic.setTextColor(context.getResources().getColor(isLight ? R.color.primary_text : R.color
                    .dark_primary_text));
        } else {
            ViewHolder viewHolder;
            if (convertView != null) {
                viewHolder = (ViewHolder) convertView.getTag();
            } else {
                convertView = mInflater.inflate(R.layout.main_news_item, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            //设置标题
            StoriesEntity storiesEntity = stories.get(position);
            TextView tvTitle = viewHolder.tvTitle;
            tvTitle.setText(storiesEntity.getTitle());
            tvTitle.setTextColor(context.getResources().getColor(isLight ? R.color.primary_text : R.color
                    .dark_primary_text));
            viewHolder.rlRoot.setBackgroundResource(isLight ? R.drawable.card_light : R.drawable.card_dark);
            if (storiesEntity.getImages() != null) {
                //设置图片
                mImageLoader.displayImage(storiesEntity.getImages().get(0), viewHolder.ivTitle, options);
                viewHolder.ivTitle.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ivTitle.setVisibility(View.GONE);
            }

        }
        return convertView;
    }

    public void clearList() {
        this.stories.clear();
    }

    public void addList(List<StoriesEntity> items) {
        if (this.stories == null) {
            this.stories = new ArrayList<>();
        }
        this.stories.addAll(items);
        this.notifyDataSetChanged();
    }

    public void updateTheme() {
        isLight = ((MainActivity) context).isLight();
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.iv_title)
        ImageView ivTitle;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.rl_root)
        RelativeLayout rlRoot;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class TopicViewHolder {
        @BindView(R.id.tv_topic)
        TextView tvTopic;

        TopicViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
