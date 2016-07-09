package com.example.angelshao.zhihuimitate.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;

import com.example.angelshao.zhihuimitate.R;
import com.example.angelshao.zhihuimitate.db.WebCacheDbHelper;
import com.example.angelshao.zhihuimitate.entities.Content;
import com.example.angelshao.zhihuimitate.entities.StoriesEntity;
import com.example.angelshao.zhihuimitate.utils.Constant;
import com.example.angelshao.zhihuimitate.utils.SharedPreferenceUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 最新日报阅读模块
 * 有封面大图
 */
public class LatestContentActivity extends BaseContentActivity{

    @BindView(R.id.iv_title)
    ImageView ivTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.webView)
    WebView webView;
//    @Bind(R.id.coordinatorLayout)
//    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_content);
        ButterKnife.bind(this);

        WebCacheDbHelper dbHelper = new WebCacheDbHelper(this, 1);
        boolean isLight = SharedPreferenceUtils.getBooleanFromDefault(this, Constant.IS_LIGHT, true);

        StoriesEntity entity = (StoriesEntity) getIntent().getSerializableExtra("entity");
        collapsingToolbarLayout.setTitle(entity.getTitle());
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(isLight ? R.color.primary : R.color.dark_primary));
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(isLight ? R.color.primary : R.color.dark_primary));

        //init toolbar
        initToolbar();

        //init webView
        initWebView();

        super.getData(entity, dbHelper);
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        super.initWebView(webView);
    }

    private void initToolbar() {

        setSupportActionBar(toolbar);
        //设置返回按钮
        if(null != getSupportActionBar()){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //返回按钮点击
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // 经测试在代码里直接声明透明状态栏更有效
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    @Override
    protected void parseJson(String responseString) {
        Gson gson = new Gson();
        Content content = gson.fromJson(responseString, Content.class);
        final ImageLoader imageloader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        imageloader.displayImage(content.getImage(), ivTitle, options);
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + content.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        webView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
    }
}
