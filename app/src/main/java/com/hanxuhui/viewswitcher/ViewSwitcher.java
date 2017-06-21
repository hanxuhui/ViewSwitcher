package com.hanxuhui.viewswitcher;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by hanxuhui on 2016/7/31.
 */
public class ViewSwitcher extends FrameLayout {

    private static final int ANIM_DURATION = 500;
    private static final String NINE_OLD_TRANSLATION_Y = "translationY";
    private static final int MARGIN_TOP = 45;
    private Context mContext;
    private boolean isAnimaling;

    public ViewSwitcher(Context context) {
        this(context, null);
    }

    public ViewSwitcher(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewSwitcher(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        Log.d("--------------------", "------------onMeasure------------");
        int childCount = this.getChildCount();
        int totalHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
            int ch = child.getMeasuredHeight();
            //计算控件总高度
            if(i == 0) {
                totalHeight += ch;
            } else {
                totalHeight += dip2px(mContext, MARGIN_TOP);
            }
            //设置子View的位置
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            int topPx = dip2px(mContext, MARGIN_TOP * i);
            lp.setMargins(0, topPx, 0, 0);
//            Log.d("++++++++"+i+"++++++++++", "-----------------topPx:" + topPx);
            child.setLayoutParams(lp);
            //设置子view的点击监听
            child.setOnClickListener(getClickListener(child, i));
        }
        super.onMeasure(widthMeasureSpec, totalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        Log.d("--------------------", "------------onLayout------------");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Log.d("--------------------", "------------onDraw------------");
        super.onDraw(canvas);
    }

    private View.OnClickListener getClickListener(final View viewChild, final int index) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAnimaling) {
                    return;
                }
                int last = getChildCount() - 1;
                if (index != last) {
                    isAnimaling = true;
                    //点击最后一个view不进行卡片切换
                    if (index == 0) {
                        onClickFirstCard();
                    } else if (index < last) {
                        onClickOtherCard(index, viewChild);
                    }
                }
            }

            public void onClickFirstCard() {
                Log.d("---------------", "-------------onClickFirstCard-------------");
                // run through all the cards
                AnimatorSet animatorSet = new AnimatorSet();
                int childCount = getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = getChildAt(i);
                    ObjectAnimator anim = null;
                    if (i == 0) {
                        // the first goes all the way down
                        float downFactor = dip2px(mContext, -1 * MARGIN_TOP * (childCount - 1));
                        Log.d("+++++++++++------------"+i, "pos:"+child.getY() + "--upFactor--"+downFactor);
                        anim = ObjectAnimator.ofFloat(child, NINE_OLD_TRANSLATION_Y, downFactor, 0);
                        anim.addListener(getAnimationListener(index, viewChild, childCount));
//                    }else if(i == 1) {
//                        // the rest go up by one card
//                        float upFactor = dip2px(mContext,  MARGIN_TOP * 0);
//                        Log.d("+++++++++++------------"+i, "pos:"+child.getY() + "--upFactor--"+upFactor);
//                        anim = ObjectAnimator.ofFloat(child, NINE_OLD_TRANSLATION_Y, child.getY(), 0);
                    }else {
                        // the rest go up by one card
                        float upFactor = dip2px(mContext,  MARGIN_TOP * ((i-1)));
                        Log.d("+++++++++++------------"+i, "pos:"+child.getY() + "--upFactor--"+upFactor);
                        anim = ObjectAnimator.ofFloat(child, NINE_OLD_TRANSLATION_Y, child.getY() - upFactor , child.getY() - upFactor - dip2px(mContext, MARGIN_TOP));
                    }
                    if (anim != null) {
//                        anim.start();
                        anim.setDuration(ANIM_DURATION);
                        animatorSet.play(anim);
                    }
                }
                animatorSet.start();
            }

            public void onClickOtherCard(final int index, View viewChild) {
                Log.d("---------------", "-------------onClickOtherCard-------------");
                // if clicked card is in middle
                AnimatorSet animatorSet = new AnimatorSet();
                int childCount = getChildCount();
                for (int i = index; i < childCount; i++) {
                    View child = getChildAt(i);
                    // run through the cards from the clicked position
                    // and on until the end
                    ObjectAnimator anim = null;
                    if (i == index) {
                        // the selected card goes all the way down
                        float downFactor = dip2px(mContext,  -1 * MARGIN_TOP * (childCount - i - 1));
                        anim = ObjectAnimator.ofFloat(child, NINE_OLD_TRANSLATION_Y, downFactor, 0);
                        anim.addListener(getAnimationListener(i, viewChild, childCount));
                    } else {
                        // the rest go up by one
                        float upFactor = dip2px(mContext, MARGIN_TOP * (i-1));
                        anim = ObjectAnimator.ofFloat(child, NINE_OLD_TRANSLATION_Y, child.getY() - upFactor , child.getY() - upFactor - dip2px(mContext, MARGIN_TOP));
                    }
                    if (anim != null) {
//                        anim.setInterpolator(new LinearInterpolator());
//                        anim.start();
                        anim.setDuration(ANIM_DURATION);
                        animatorSet.play(anim);
                    }
                }
                animatorSet.start();
            }
        };
    }

    private Animator.AnimatorListener getAnimationListener(final int index, final View clickedCard, final int last) {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.d("--------------", "-----------------onAnimationStart-----------------");
//                if (index == 0) {
//                    View newFirstCard = getChildAt(1);
//                    handleFirstCard(newFirstCard);
//                } else {
//                    clickedCard.setBackgroundResource(R.drawable.item_card_background_shadow);
//                }
                removeView(clickedCard);
                addView(clickedCard);
            }

            private void handleFirstCard(View newFirstCard) { }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.d("--------------", "-----------------onAnimationRepeat-----------------");
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d("--------------", "-----------------onAnimationEnd-----------------");
//                removeView(clickedCard);
//                addView(clickedCard);
                isAnimaling = false;
                //将所有卡片置为可点击
//                int childCount = getChildCount();
//                for (int i = 0; i < childCount; i++) {
//                    View child = getChildAt(i);
//                    child.setClickable(true);
//                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub
                Log.d("--------------", "-----------------onAnimationCancel-----------------");
            }
        };
    }

    public static int dip2px(Context context, float dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

}
