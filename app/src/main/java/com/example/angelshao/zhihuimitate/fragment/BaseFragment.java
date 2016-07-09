package com.example.angelshao.zhihuimitate.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by David-Ann on 2016/7/8 0008.
 */
public abstract class BaseFragment extends Fragment {

    protected Activity mActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        mActivity = getActivity();
        return initView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    protected abstract void initData();
    public void refreshData(final OnLoadFinishedListener listener){}

    /**
     * 更新主题
     * 注意：需要在设置完适配器再调用
     */
    public abstract void updateTheme();

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity = null;
    }
}
