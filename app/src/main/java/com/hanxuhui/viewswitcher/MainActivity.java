package com.hanxuhui.viewswitcher;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ViewSwitcher vs_view_switcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        vs_view_switcher = (ViewSwitcher) findViewById(R.id.vs_view_switcher);
        ShareCard card1 = new ShareCard(this, "1", Color.parseColor("#ffffff"));
        FeedbackCard card2 = new FeedbackCard(this, "2", Color.parseColor("#00ffff"));
        ShareCard card3 = new ShareCard(this, "3", Color.parseColor("#ffffff"));
        FeedbackCard card4 = new FeedbackCard(this, "4", Color.parseColor("#00ffff"));
        ShareCard card5 = new ShareCard(this, "5", Color.parseColor("#ffffff"));
        vs_view_switcher.addView(card1.getView());
        vs_view_switcher.addView(card2.getView());
        vs_view_switcher.addView(card3.getView());
        vs_view_switcher.addView(card4.getView());
        vs_view_switcher.addView(card5.getView());
//        vs_view_switcher.addView(card6.getView());
//        vs_view_switcher.postInvalidate();
    }
}
