package com.example.angelshao.zhihuimitate.fragment;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.angelshao.zhihuimitate.R;
import com.example.angelshao.zhihuimitate.activity.MainActivity;
import com.example.angelshao.zhihuimitate.activity.NewsContentActivity;
import com.example.angelshao.zhihuimitate.adapter.MainNewsItemAdapter;
import com.example.angelshao.zhihuimitate.entities.News;
import com.example.angelshao.zhihuimitate.entities.StoriesEntity;
import com.example.angelshao.zhihuimitate.utils.Constant;
import com.example.angelshao.zhihuimitate.utils.HttpUtils;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class NewsFragment extends BaseFragment {

    private ListView lvNews;
    private ImageView ivTitle;
    private TextView tvTitle;
    private ImageLoader mImageLoader;
    private MainNewsItemAdapter mAdapter;
    private String urlId;
    private String title;

    public NewsFragment(String id, String title) {
        this.urlId = id;
        this.title = title;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        mImageLoader = ImageLoader.getInstance();
        lvNews = (ListView) view.findViewById(R.id.lv_news);
        View header = LayoutInflater.from(mActivity).inflate(
                R.layout.infinite_viewpager_item, lvNews, false);
        ivTitle = (ImageView) header.findViewById(R.id.iv_title);
        tvTitle = (TextView) header.findViewById(R.id.tv_title);
        lvNews.addHeaderView(header);

        mAdapter = new MainNewsItemAdapter(mActivity);
        lvNews.setAdapter(mAdapter);
        updateTheme();
        lvNews.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (lvNews != null && lvNews.getChildCount() > 0) {
                    boolean enable = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    ((MainActivity) mActivity).setSwipeRefreshEnable(enable);
                }
            }
        });

        lvNews.setOnItemClickListener(new OnNewsItemClickListener(mActivity, NewsContentActivity.class));

        return view;
    }

    @Override
    public void updateTheme() {
        boolean isLight = ((MainActivity) mActivity).isLight();
        lvNews.setBackgroundColor(getResources().getColor(isLight ? R.color.light_menu_list_bg : R.color.dark_menu_list_bg));
        if (mAdapter != null) {
            mAdapter.updateTheme();
        }
    }

    @Override
    public void refreshData(final OnLoadFinishedListener listener) {
        initData();
        listener.finishLoad();

    }

    @Override
    protected void initData() {
        if (HttpUtils.isNetworkConnected(mActivity)) {
            HttpUtils.get(Constant.THEMENEWS + urlId, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      String responseString, Throwable throwable) {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values(" +
                            (Constant.BASE_COLUMN + Integer.parseInt(urlId)) + ",' " + responseString + "')");

                    db.close();
                    parseJson(responseString);
                }
            });
        } else {
            SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " +
                    (Constant.BASE_COLUMN + Integer.parseInt(urlId)), null);

            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseJson(json);
            }

            cursor.close();
            db.close();
        }
    }

    private void parseJson(String responseString) {
        Gson gson = new Gson();
        News news = gson.fromJson(responseString, News.class);

        tvTitle.setText(news.getDescription());
        DisplayImageOptions options = new DisplayImageOptions
                .Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        mImageLoader.displayImage(news.getImage(), ivTitle, options);
        List<StoriesEntity> stories = news.getStories();
        mAdapter.clearList();
        mAdapter.addList(stories);
    }
}
