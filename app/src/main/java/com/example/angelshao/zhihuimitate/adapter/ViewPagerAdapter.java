package com.example.angelshao.zhihuimitate.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.angelshao.infiniteviewpager.InfinitePagerAdapter;
import com.example.angelshao.zhihuimitate.R;
import com.example.angelshao.zhihuimitate.activity.LatestContentActivity;
import com.example.angelshao.zhihuimitate.entities.Latest;
import com.example.angelshao.zhihuimitate.entities.StoriesEntity;
import com.example.angelshao.zhihuimitate.utils.Constant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 *
 * Created by yan on 2015/10/22.
 */
public class ViewPagerAdapter extends InfinitePagerAdapter {

//    private List<HeaderInfo> mImageList;
    private List<Latest.TopStoriesEntity> mTopStories;
    private Context context;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private Activity mActivity;


    public ViewPagerAdapter(List<Latest.TopStoriesEntity> mTopStories, Activity mActivity) {
        this.mTopStories = mTopStories;
        this.context = mActivity;
        this.mActivity = mActivity;
        mImageLoader = ImageLoader.getInstance();
        mOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    @Override
    public int getItemCount() {
        return mTopStories == null ? 0 : mTopStories.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup container) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.infinite_viewpager_item, container, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Latest.TopStoriesEntity topStoriesEntity = mTopStories.get(position);

        //设置图片
        final ImageView ivTitle = viewHolder.ivTitle;
        mImageLoader.displayImage(topStoriesEntity.getImage(), ivTitle, mOptions);
//        Bitmap resizedBitmap = ImageCacheUtil.getResizedBitmap(context.getResources(), headerInfo.getIcon(), 480, false);
//        viewHolder.ivTitle.setImageBitmap(resizedBitmap);

        //设置标题
        viewHolder.tvTitle.setText(topStoriesEntity.getTitle());

        ivTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reveal效果开始扩散的位置
                int[] startingLocation = new int[2];
                ivTitle.getLocationOnScreen(startingLocation);
                startingLocation[0] += ivTitle.getWidth() / 2;
                startingLocation[1] += ivTitle.getHeight() / 2;

                StoriesEntity entity = new StoriesEntity();
                entity.setId(topStoriesEntity.getId());
                entity.setTitle(topStoriesEntity.getTitle());
                Intent intent = new Intent(context, LatestContentActivity.class);
                intent.putExtra(Constant.START_LOCATION, startingLocation);
                intent.putExtra(Constant.ENTITY, entity);

                context.startActivity(intent);
                // 取消activity之间的跳转效果
                // 使用目标Activity的Reveal效果
                mActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_title)
        ImageView ivTitle;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private void openArticle(int position){

    }
}
