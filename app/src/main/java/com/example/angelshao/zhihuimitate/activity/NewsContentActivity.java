package com.example.angelshao.zhihuimitate.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import com.example.angelshao.zhihuimitate.R;
import com.example.angelshao.zhihuimitate.db.WebCacheDbHelper;
import com.example.angelshao.zhihuimitate.entities.Content;
import com.example.angelshao.zhihuimitate.entities.StoriesEntity;
import com.example.angelshao.zhihuimitate.utils.Constant;
import com.example.angelshao.zhihuimitate.utils.SharedPreferenceUtils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 各类主题日报的阅读模块
 * 主要区别是没有封面大图
 */
public class NewsContentActivity extends BaseContentActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webView)
    WebView webView;

    private boolean isLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        ButterKnife.bind(this);

        WebCacheDbHelper dbHelper = new WebCacheDbHelper(this, 1);
        isLight = SharedPreferenceUtils.getBooleanFromDefault(this, Constant.IS_LIGHT, true);
        StoriesEntity entity = (StoriesEntity) getIntent().getSerializableExtra("entity");

        //init toolbar
        initToolbar();

        //init webView
        initWebView();

        super.getData(entity, dbHelper);

    }

    @Override
    protected void parseJson(String responseString) {
        Gson gson = new Gson();
        Content content = gson.fromJson(responseString, Content.class);
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + content.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        webView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        super.initWebView(webView);
    }

    private void initToolbar() {

        toolbar.setTitle("享受阅读的乐趣");
        toolbar.setBackgroundColor(getResources().getColor(isLight ? R.color.primary : R.color.dark_primary));
        setSupportActionBar(toolbar);
        setStatusBarColor(getResources().getColor(isLight ? R.color.primary_dark : R.color.dark_primary_dark));

        //设置返回按钮
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //返回按钮点击
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @TargetApi(21)
    private void setStatusBarColor(int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // If both system bars are black, we can remove these from our layout,
            // removing or shrinking the SurfaceFlinger overlay required for our views.
            Window window = this.getWindow();
            if (statusBarColor == Color.BLACK && window.getNavigationBarColor() == Color.BLACK) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.setStatusBarColor(statusBarColor);
        }
    }
}
