package com.example.angelshao.zhihuimitate.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.angelshao.zhihuimitate.R;
import com.example.angelshao.zhihuimitate.adapter.MainNewsItemAdapter;
import com.example.angelshao.zhihuimitate.entities.StoriesEntity;
import com.example.angelshao.zhihuimitate.utils.Constant;
import com.example.angelshao.zhihuimitate.utils.SharedPreferenceUtils;

/**
 * Created by David-Ann on 2016/7/8 0008.
 */
public class OnNewsItemClickListener implements AdapterView.OnItemClickListener {

    private Activity mActivity;
    private Class<?> contentClass; //不同项点击进入的界面不同

    public OnNewsItemClickListener(Activity mActivity, Class<?> contentClass) {
        this.mActivity = mActivity;
        this.contentClass = contentClass;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //不响应 headerView 点击事件
        int itemViewType = parent.getAdapter().getItemViewType(position);
        if(itemViewType != MainNewsItemAdapter.TYPE_STORIES){
            return ;
        }
        StoriesEntity entity = (StoriesEntity) parent.getAdapter().getItem(position);
        Intent intent = new Intent(mActivity, contentClass);
        intent.putExtra(Constant.ENTITY, entity);

        String readSequence = SharedPreferenceUtils.getStringFromDefault(mActivity, Constant.READ, "");
        String[] splits = readSequence.split(",");
        StringBuffer sb = new StringBuffer();
        if (splits.length >= 200) {
            for (int i = 100; i < splits.length; i++) {
                sb.append(splits[i]).append(",");
            }
            readSequence = sb.toString();
        }

        if (!readSequence.contains(entity.getId() + "")) {
            readSequence = readSequence + entity.getId() + ",";
        }
        SharedPreferenceUtils.putStringToDefault(mActivity, Constant.READ, readSequence);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setTextColor(mActivity.getResources().getColor(R.color.secondary_text));

        mActivity.startActivity(intent);
        // 取消activity之间的跳转效果
        // 使用目标Activity的Reveal效果
        mActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
