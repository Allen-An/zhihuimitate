package com.example.angelshao.zhihuimitate.activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.angelshao.zhihuimitate.R;
import com.example.angelshao.zhihuimitate.db.CacheDbHelper;
import com.example.angelshao.zhihuimitate.fragment.BaseFragment;
import com.example.angelshao.zhihuimitate.fragment.MainFragment;
import com.example.angelshao.zhihuimitate.fragment.MenuFragment;
import com.example.angelshao.zhihuimitate.fragment.NewsFragment;
import com.example.angelshao.zhihuimitate.fragment.OnLoadFinishedListener;
import com.example.angelshao.zhihuimitate.utils.Constant;
import com.example.angelshao.zhihuimitate.utils.SharedPreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnLoadFinishedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.srl_main)
    SwipeRefreshLayout srlMain;
    @BindView(R.id.dl_main)
    DrawerLayout dlMain;

    private boolean isLight;
    private String currentId;
    private CacheDbHelper dbHelper;
    private long firstTime;
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        isLight = SharedPreferenceUtils.getBooleanFromDefault(this, Constant.IS_LIGHT, true);
        dbHelper = new CacheDbHelper(this, 1);
        mainFragment = new MainFragment();

        initView();

        loadLatestNews();
    }

    public void loadLatestNews() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(R.id.fl_content, mainFragment, Constant.FM_LATEST)
                .commit();
        currentId = Constant.FM_LATEST;
    }

    private void initView() {
        toolbar.setBackgroundColor(getResources().getColor(isLight ? R.color.primary : R.color.dark_primary));
        setSupportActionBar(toolbar);
        setStatusBarColor(getResources().getColor(isLight ? R.color.primary_dark : R.color.dark_primary_dark));
        srlMain.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light
        );
        srlMain.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        final ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, dlMain,
                toolbar, R.string.title_activity_main, R.string.title_activity_main);
        dlMain.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void refresh() {
        Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(currentId);
        if (fragmentByTag != null) {
            //多态刷新数据
            ((BaseFragment) fragmentByTag).refreshData(this);
        }
    }

    public void setSwipeRefreshEnable(boolean enable) {
        srlMain.setEnabled(enable);
    }

    public String getCurrentId() {
        return currentId;
    }

    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }

    public void setToolBarTitle(String title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    public boolean isLight() {
        return this.isLight;
    }

    public CacheDbHelper getCacheDbHelper() {
        return dbHelper;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        menu.getItem(0)
                .setTitle(SharedPreferenceUtils.getBooleanFromDefault(this, "isLight", true) ? "夜间模式" : "日间模式");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_mode) {
            isLight = !isLight;

            item.setTitle(isLight ? "夜间模式" : "日间模式");
            toolbar.setBackgroundColor(getResources().getColor(isLight ? R.color.primary : R.color.dark_primary));
            setStatusBarColor(getResources().getColor(isLight ? R.color.primary_dark : R.color.dark_primary_dark));
            if (currentId.equals(Constant.FM_LATEST)) {
                MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(Constant
                        .FM_LATEST);
                if (mainFragment != null) {
                    mainFragment.updateTheme();
                }
            } else {
                NewsFragment newsFragment = (NewsFragment) getSupportFragmentManager().findFragmentByTag(Constant
                        .FM_NEWS);
                if (newsFragment != null) {
                    newsFragment.updateTheme();
                }
            }
            ((MenuFragment) getSupportFragmentManager().findFragmentById(R.id.menu_fragment)).updateTheme();
            SharedPreferenceUtils.putBooleanToDefault(this, "isLight", isLight);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (dlMain.isDrawerOpen(GravityCompat.START)) {
            closeMenu();
        } else {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Snackbar sb = Snackbar.make(flContent, "再按一次退出", Snackbar.LENGTH_SHORT);
                sb.getView().setBackgroundColor(getResources().getColor(isLight ? R.color.primary : R.color.dark_primary));
                sb.show();
                firstTime = secondTime;
            } else {
                finish();
            }
        }
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

    public void closeMenu() {
        dlMain.closeDrawers();
    }

    @Override
    public void finishLoad() {
        srlMain.setRefreshing(false);
    }
}
