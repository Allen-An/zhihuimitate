package com.example.angelshao.zhihuimitate.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.example.angelshao.zhihuimitate.R;
import com.example.angelshao.zhihuimitate.utils.Constant;
import com.example.angelshao.zhihuimitate.utils.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by David-Ann on 2016/6/5 0005.
 */
public class StartPage extends Activity {

    private ImageView iv_start;
    public final String TAG = "startpage_activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置窗体全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.start_page);
        iv_start = (ImageView) findViewById(R.id.iv_start_page);

        initImage();

    }

    /**
     * 检查/data/data/下是否存在start.jpg，如果存在，直接设置到iv_start，
     * 然后后台再异步下载一个最新的start.jpg,替换已有的文件，等待下次使用
     * 如果不存在，则证明系统第一次启动该APP，从Resource中获取一个默认的start.jpg文件，
     * 设置给起始页面
     */
    private void initImage() {


        File dir = getFilesDir();
        final File imgFile = new File(dir, "start.jpg");
        if (imgFile.exists()) {
            iv_start.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
        } else {
            iv_start.setImageResource(R.mipmap.start);
        }

        final ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(3000);

        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                //动画结束后，联网获取最新的启动图像
                getNewestStartPageImage(imgFile);

                //进入App
                startMainActivity();


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv_start.setAnimation(scaleAnim);
    }

    private void getNewestStartPageImage(final File imgFile) {
        if (HttpUtils.isNetworkConnected(StartPage.this)) {
            HttpUtils.get(Constant.START, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(bytes));
                        String url = jsonObject.getString("img");
                        HttpUtils.getImage(url, new BinaryHttpResponseHandler() {
                            @Override
                            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                saveImage(imgFile, bytes);
                            }

                            @Override
                            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                Log.i(TAG, "onFailure: " + throwable.toString());
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes,
                                      Throwable throwable) {
                    Log.i(TAG, "onFailure: " + throwable.toString());

                }
            });
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(StartPage.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
        finish();
    }

    private void saveImage(File imgFile, byte[] bytes) {
        if (imgFile.exists()) {
            imgFile.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(imgFile);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
