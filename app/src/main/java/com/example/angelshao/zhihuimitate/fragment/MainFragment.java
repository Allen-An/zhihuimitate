package com.example.angelshao.zhihuimitate.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.angelshao.infiniteviewpager.InfiniteViewPager;
import com.angelshao.infiniteviewpager.indicator.CirclePageIndicator;
import com.example.angelshao.zhihuimitate.R;
import com.example.angelshao.zhihuimitate.activity.LatestContentActivity;
import com.example.angelshao.zhihuimitate.activity.MainActivity;
import com.example.angelshao.zhihuimitate.adapter.MainNewsItemAdapter;
import com.example.angelshao.zhihuimitate.adapter.ViewPagerAdapter;
import com.example.angelshao.zhihuimitate.entities.Before;
import com.example.angelshao.zhihuimitate.entities.Latest;
import com.example.angelshao.zhihuimitate.entities.StoriesEntity;
import com.example.angelshao.zhihuimitate.utils.Constant;
import com.example.angelshao.zhihuimitate.utils.HttpUtils;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by David-Ann on 2016/7/8 0008.
 */
public class MainFragment extends BaseFragment {

    private ListView lvNews;
    private InfiniteViewPager viewPager;
    private CirclePageIndicator dotIndicator;
    private Latest mLatest;

    private boolean isLoading = false;
    private String date;
    private MainNewsItemAdapter mAdapter;
    public static boolean IS_REFRESHING = false;
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //ListView
        lvNews = (ListView) rootView.findViewById(R.id.lv_news);
        //ListView Header
        View headerView = inflater.inflate(R.layout.main_news_header, lvNews, false);

        viewPager = (InfiniteViewPager) headerView.findViewById(R.id.viewPager);
        dotIndicator = (CirclePageIndicator) headerView.findViewById(R.id.dot_indicator);

        viewPager.setAutoScrollTime(4000);

        //add HeaderView for listView
        lvNews.addHeaderView(headerView);
        mAdapter = new MainNewsItemAdapter(mActivity);
        lvNews.setAdapter(mAdapter);
        updateTheme();

        lvNews.setOnItemClickListener(new OnNewsItemClickListener(mActivity, LatestContentActivity.class));
        lvNews.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (lvNews != null && lvNews.getChildCount() > 0) {
                    //只有在列表头时才能刷新
                    boolean enable = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    ((MainActivity) mActivity).setSwipeRefreshEnable(enable);

                    if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading) {
                        loadMore(Constant.BEFORE + date);
                    }
                }
            }
        });

        //解决刷新冲突
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FragmentActivity activity = getActivity();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        ((MainActivity)activity).setSwipeRefreshEnable(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        ((MainActivity)activity).setSwipeRefreshEnable(true);
                        break;
                }
                viewPager.onTouchEvent(event);

                return false;
            }
        });
        return rootView;
    }

    @Override
    protected void initData() {
        loadFirst();
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
        loadFirst();
        listener.finishLoad();
        IS_REFRESHING = true;
    }
    public void loadFirst() {
        isLoading = true;
        //检查网络
        if (HttpUtils.isNetworkConnected(mActivity)) {
            HttpUtils.get(Constant.LATESTNEWS, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values(" + Constant.LATEST_COLUMN + ",' " + responseString + "')");
                    db.close();
                    parseLatestJson(responseString);
                }
            });
        } else {
            SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + Constant.LATEST_COLUMN, null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseLatestJson(json);
            } else {
                isLoading = false;
            }
            cursor.close();
            db.close();
        }
    }

    private void loadMore(final String url) {
        isLoading = true;
        //检查网络
        if (HttpUtils.isNetworkConnected(mActivity)) {
            HttpUtils.get(url, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values(" +
                            date + ",' " + responseString + "')");
                    db.close();

                    parseBeforeJson(responseString);
                }
            });
        } else {
            SQLiteDatabase db = ((MainActivity) mActivity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + date, null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseBeforeJson(json);
            } else {
                db.delete("CacheList", "date < " + date, null);
                isLoading = false;
                Snackbar sb = Snackbar.make(lvNews, "没有更多的离线内容了~", Snackbar.LENGTH_SHORT);
                sb.getView().setBackgroundColor(getResources().getColor(
                        ((MainActivity) mActivity).isLight() ? R.color.primary : R.color.dark_primary));
                sb.show();
            }
            cursor.close();
            db.close();
        }
    }

    private void parseLatestJson(String responseString) {
        Gson gson = new Gson();
        mLatest = gson.fromJson(responseString, Latest.class);
        date = mLatest.getDate();
        viewPager.setAdapter(new ViewPagerAdapter(mLatest.getTop_stories(), mActivity));
//                指示器要设置在适配器后面，否则会报空指针
        dotIndicator.setViewPager(viewPager);
//                设置完适配器才可以开始滚动，否则会失效
        viewPager.startAutoScroll();

        List<StoriesEntity> stories = mLatest.getStories();

        IS_REFRESHING = false;

        StoriesEntity topic = new StoriesEntity();
        topic.setType(Constant.TOPIC);
        topic.setTitle("今日热闻");
        stories.add(0, topic);

        mAdapter.clearList();
        mAdapter.addList(stories);
        isLoading = false;

    }
    private void parseBeforeJson(String responseString) {
        Gson gson = new Gson();
        final Before before = gson.fromJson(responseString, Before.class);
        if (before == null) {
            isLoading = false;
            return;
        }
        date = before.getDate();
        List<StoriesEntity> stories = before.getStories();
        StoriesEntity topic = new StoriesEntity();
        topic.setType(Constant.TOPIC);
        topic.setTitle(convertDate(date));
        stories.add(0, topic);
        mAdapter.addList(stories);
        isLoading = false;

    }

    private String convertDate(String date) {
        return date.substring(0, 4) + "年" + date.substring(4, 6) + "月" + date.substring(6, 8) + "日";
    }
}
