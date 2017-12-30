package com.hzh.billboard.banner.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * Package: com.hzh.billboard.banner.widget
 * FileName: BillboardBannerView
 * Date: on 2017/12/29  下午4:50
 * Auther: zihe
 * Descirbe:
 * Email: hezihao@linghit.com
 */

public class BillboardBannerView extends FrameLayout {
    private final int mBackgroundColor = Color.parseColor("#7F333333");
    private final int mTipTextColor = Color.parseColor("#FFFFFF");

    private int screenWidth;
    private float maxTipTextWidth;

    private TextView textView;
    private Handler mainHandler;
    private Scroller mScroller;
    private String tipText;
    private String originText;

    public BillboardBannerView(@NonNull Context context) {
        super(context);
        init();
    }

    public BillboardBannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BillboardBannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mainHandler = new Handler(Looper.getMainLooper());
        screenWidth = getScreenWidth(getContext());
        //一开始先隐藏告示条
        this.setAlpha(0f);

//        this.setAlpha(1f);
//        mScroller = new Scroller(getContext(), new LinearInterpolator());
//        start2();
    }

//    private void start2() {
//        backgroundLayout.scrollTo(-screenWidth, 0);
//        int scrollX = backgroundLayout.getScrollX();
//        float deltaX = maxTipTextWidth;
//
//        char[] chars = originText.toCharArray();
//        mScroller.startScroll(scrollX, 0, (int) deltaX, 0, chars.length * 200);
//    }
//
//    @Override
//    public void computeScroll() {
//        super.computeScroll();
//        if (mScroller.computeScrollOffset()) {
//            backgroundLayout.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
//            postInvalidate();
//        } else {
//            start2();
//            invalidate();
//        }
//    }

    public void configText(String originText, String tipText) {
        this.originText = originText;
        this.tipText = tipText;
        //添加滚动文字
        textView = new TextView(getContext());
        textView.setTextColor(mTipTextColor);
        textView.setTextSize(sp2px(getContext(), 5f));
        textView.setText(Html.fromHtml(tipText));
        float tipTextWidth = textView.getPaint().measureText(originText);
        maxTipTextWidth = tipTextWidth;
        LayoutParams textParams = new LayoutParams((int) tipTextWidth, LayoutParams.MATCH_PARENT);
        addView(textView, textParams);
        //设置默认位置，将文字移动到屏幕外
        textView.setTranslationX(screenWidth);
        //开始动画
        startScroll();
    }

    /**
     * 开始滚动
     */
    private void startScroll() {
        AnimatorSet set = new AnimatorSet();
        set.setStartDelay(1000);
        set.playSequentially(getShowAnimator(), getScrollAnimator(), getHideAnimator());
        set.start();
    }

    private ValueAnimator getScrollAnimator() {
        //滚动动画。滚动距离，如果语句比屏幕宽度长，则使用语句长度。如果语句比屏幕宽度短，则使用屏幕宽度
        final float end = Math.max(screenWidth, maxTipTextWidth);
        final ValueAnimator rollAnimator = ValueAnimator.ofFloat(screenWidth, -end);
        rollAnimator.setInterpolator(new LinearInterpolator());
        rollAnimator.setDuration(originText.toCharArray().length * 400);
        rollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float cValue = (Float) animation.getAnimatedValue();
                textView.setTranslationX(cValue);
            }
        });
        return rollAnimator;
    }

    /**
     * 渐变展示告示条
     */
    private ValueAnimator getShowAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(400);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float cValue = (Float) animation.getAnimatedValue();
                BillboardBannerView.this.setAlpha(cValue);
            }
        });
        return animator;
    }

    /**
     * 隐藏展示告示条
     */
    private ValueAnimator getHideAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
        animator.setDuration(200);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float cValue = (Float) animation.getAnimatedValue();
                BillboardBannerView.this.setAlpha(cValue);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startScroll();
            }
        });
        return animator;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    private int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();
    }
}