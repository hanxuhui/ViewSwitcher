package com.hanxuhui.viewswitcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by hanxuhui on 2016/7/25.
 */
public class FeedbackCard {

    private View.OnClickListener mListener;
    private Context mContext;
    private String des;
    private int bgColor;

    public FeedbackCard(Context context, String des) {
        this.mContext = context;
        this.des = des;
    }

    public FeedbackCard(Context context, String des, int bgColor) {
        this.mContext = context;
        this.des = des;
    }

    public View getView() {
        View view = LayoutInflater.from(mContext).inflate(getCardLayout(), null);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_content.setText(des);
//        tv_content.setBackgroundColor(bgColor);
        return view;
    }

    public View.OnClickListener getClickListener() {
        return mListener;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        mListener = listener;
    }

    protected int getCardLayout() {
        return R.layout.view_viewswitcher_layout;
    }

}
