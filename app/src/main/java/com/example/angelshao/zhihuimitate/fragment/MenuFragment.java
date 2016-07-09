package com.example.angelshao.zhihuimitate.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.angelshao.zhihuimitate.R;
import com.example.angelshao.zhihuimitate.activity.MainActivity;
import com.example.angelshao.zhihuimitate.entities.NewsListItem;
import com.example.angelshao.zhihuimitate.utils.Constant;
import com.example.angelshao.zhihuimitate.utils.HttpUtils;
import com.example.angelshao.zhihuimitate.utils.SharedPreferenceUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by David-Ann on 2016/7/8 0008.
 * 抽屉菜单的实现
 */
public class MenuFragment extends BaseFragment {

    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_favorites)
    TextView tvFavorites;
    @BindView(R.id.tv_download)
    TextView tvDownload;
    @BindView(R.id.tv_main)
    TextView tvMain;
    @BindView(R.id.lv_item)
    ListView lvItem;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;

    private boolean isLight;
    private int currentPosition = -1;
    private List<NewsListItem> itemList;
    private NewsTypeAdapter mAdapter;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu, container, false);
        ButterKnife.bind(this, view);
        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentPosition == position) {
                    ((MainActivity) mActivity).closeMenu();
                    return;
                }
                currentPosition = position;
                getFragmentManager().beginTransaction()
                        .replace(
                                R.id.fl_content,
                                new NewsFragment(itemList.get(position).getId(),
                                        itemList.get(position).getTitle()))
                        .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
                        .commit();
                ((MainActivity) mActivity).setCurrentId(Constant.FM_NEWS + "");
                ((MainActivity) mActivity).setToolBarTitle(itemList.get(position).getTitle());
                ((MainActivity) mActivity).closeMenu();
            }
        });
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        assert rootView != null;
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initData() {
        itemList = new ArrayList<>();
        isLight = ((MainActivity)mActivity).isLight();
        if (HttpUtils.isNetworkConnected(mActivity)) {
            HttpUtils.get(Constant.THEMES, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    String json = response.toString();
                    SharedPreferenceUtils.putStringToDefault(mActivity, Constant.THEMES, json);
                    parseJson(response);
                }
            });
        } else {
            String json = SharedPreferenceUtils.getStringFromDefault(mActivity, Constant.THEMES, "");
            if(TextUtils.isEmpty(json)){
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(json);
                parseJson(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateTheme() {
        isLight = ((MainActivity) mActivity).isLight();
        llRoot.setBackgroundColor(getResources().getColor(isLight ? R.color.primary : R.color.dark_primary));
        tvLogin.setTextColor(getResources().getColor(isLight ? R.color.light_menu_header_text : R.color.dark_menu_header_text));
        tvFavorites.setTextColor(getResources().getColor(isLight ? R.color.light_menu_header_text : R.color.dark_menu_header_text));
        tvDownload.setTextColor(getResources().getColor(isLight ? R.color.light_menu_header_text : R.color.dark_menu_header_text));
        tvMain.setBackgroundColor(getResources().getColor(isLight ? R.color.light_menu_main_bg : R.color.dark_menu_main_bg));
        lvItem.setBackgroundColor(getResources().getColor(isLight ? R.color.light_menu_list_bg : R.color.dark_menu_list_bg));
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void parseJson(JSONObject jsonObject) {
        try {
            JSONArray itemsArray = jsonObject.getJSONArray("others");
            for (int i = 0; i < itemsArray.length(); i++) {
                NewsListItem newsListItem = new NewsListItem();
                JSONObject itemObject = itemsArray.getJSONObject(i);
                newsListItem.setTitle(itemObject.getString("name"));
                newsListItem.setId(itemObject.getString("id"));
                itemList.add(newsListItem);
            }
            mAdapter = new NewsTypeAdapter();
            lvItem.setAdapter(mAdapter);
            updateTheme();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class NewsTypeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return null == itemList ? 0 : itemList.size();
        }

        @Override
        public NewsListItem getItem(int position) {
            return null == itemList ? null : itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return null == itemList ? 0 : position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_item, parent, false);
            }

            TextView tv_NewsTitle = (TextView) convertView.findViewById(R.id.tv_news_title);
            ImageView iv_Add = (ImageView) convertView.findViewById(R.id.iv_add);

            tv_NewsTitle.setText(itemList.get(position).getTitle());
            tv_NewsTitle.setTextColor(getResources().getColor(isLight ? R.color.light_menu_list_text : R.color.dark_menu_list_text));

            iv_Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return convertView;
        }
    }
    @OnClick(R.id.tv_main)
    public void clickMain() {
        currentPosition = -1;
        ((MainActivity) mActivity).loadLatestNews();
        ((MainActivity) mActivity).setToolBarTitle("首页");
        ((MainActivity) mActivity).closeMenu();
    }

}
