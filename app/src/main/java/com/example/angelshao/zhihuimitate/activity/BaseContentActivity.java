package com.example.angelshao.zhihuimitate.activity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.angelshao.zhihuimitate.db.WebCacheDbHelper;
import com.example.angelshao.zhihuimitate.entities.StoriesEntity;
import com.example.angelshao.zhihuimitate.utils.Constant;
import com.example.angelshao.zhihuimitate.utils.HttpUtils;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * 新闻内容基类
 * 新闻内容获取数据方式相同，解析形式不同
 * 故作为抽象类使子类去实现 parseJson 解析方法
 */
public abstract class BaseContentActivity extends AppCompatActivity {


    protected void getData(final StoriesEntity entity, final WebCacheDbHelper dbHelper) {
        if (HttpUtils.isNetworkConnected(this)) {
            HttpUtils.get(Constant.CONTENT + entity.getId(), new TextHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    responseString = responseString.replaceAll("'", "''");
                    db.execSQL("replace into Cache(newsId,json) values(" + entity.getId() + ",'" + responseString + "')");
                    db.close();
                    parseJson(responseString);
                }
            });
        } else {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from Cache where newsId = " + entity.getId(), null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseJson(json);
            }
            cursor.close();
            db.close();
        }
    }

    /**
     * 不同新闻类型有不同解析方式
     * 自己实现
     */
    protected abstract void parseJson(String responseString);

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
        //淡出
        overridePendingTransition(0, android.R.anim.fade_out);
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void initWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        settings.setDomStorageEnabled(true);
        // 开启database storage API功能
        settings.setDatabaseEnabled(true);
        // 开启Application Cache功能
        settings.setAppCacheEnabled(true);
    }
}
