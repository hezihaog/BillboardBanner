package com.hzh.billboard.banner.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

/**
 * Package: com.hzh.billboard.banner.widget
 * FileName: BillboardBannerView
 * Date: on 2017/12/29  下午4:50
 * Auther: zihe
 * Descirbe:
 * Email: hezihao@linghit.com
 */

public class BillboardBannerView extends LinearLayout {
    /**
     * 屏幕宽度
     */
    private int screenWidth;
    /**
     * 最大滚动的距离，就是内容View的宽度，一开始会先将内容View设置位移到屏幕宽度，后续会从屏幕宽度为起点，到该最大值
     */
    private float maxScrollViewWidth;
    /**
     * 渐变显示、滚动、渐变消失动画集合
     */
    private AnimatorSet animatorSet;
    /**
     * 内容View，实例是一个TextView
     */
    private View mContentView;
    /**
     * 滚动时间，默认是10秒，示例中是TextView，时间应该是拿到文字的总字符数，乘以每个字符移动的距离，计算出总时间
     */
    private int mDuration = 1000 * 10;

    //private Scroller mScroller;

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
        //开启绘制自身，这样才会回调onDraw函数
        setWillNotDraw(false);
        //设置横向排列，这里因为我们只允许一个子View，所以横向竖线都是一样的，如果不设置默认也是横向
        setOrientation(LinearLayout.HORIZONTAL);
        //获取屏幕宽度，一开始先将contentView移动到屏幕外层
        screenWidth = getScreenWidth(getContext());

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


    public void setDuration(int duration) {
        this.mDuration = duration;
        if (animatorSet != null && animatorSet.isRunning()) {
            animatorSet.cancel();
        }
        //重新开始动画
        startScroll();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        //只能有一个子View
        if (childCount > 1) {
            throw new RuntimeException("只能有一个子View");
        }
        //获取滚动内容View
        mContentView = getChildAt(0);
        int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        mContentView.measure(w, h);
        //获取需要滚动的View的宽
        maxScrollViewWidth = mContentView.getMeasuredWidth();
    }

    /**
     * 开始滚动
     */
    private void startScroll() {
        animatorSet = new AnimatorSet();
        animatorSet.setStartDelay(1000);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                //一开始先隐藏告示条
                if (getAlpha() != 0) {
                    BillboardBannerView.this.setAlpha(0f);
                    //设置默认位置，将内容View移动到屏幕外
                    mContentView.setTranslationX(screenWidth);
                }
            }
        });
        animatorSet.playSequentially(getShowAnimator(), getScrollAnimator(), getHideAnimator());
        animatorSet.start();
    }

    private ValueAnimator getScrollAnimator() {
        //滚动动画。滚动距离，如果语句比屏幕宽度长，则使用语句长度。如果语句比屏幕宽度短，则使用屏幕宽度
        final float end = Math.max(screenWidth, maxScrollViewWidth);
        final ValueAnimator rollAnimator = ValueAnimator.ofFloat(screenWidth, -end);
        rollAnimator.setInterpolator(new LinearInterpolator());
        rollAnimator.setDuration(mDuration);
        rollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float cValue = (Float) animation.getAnimatedValue();
                mContentView.setTranslationX(cValue);
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
                //结束后，重新滚动
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