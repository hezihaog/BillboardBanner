package com.hzh.billboard.banner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hzh.billboard.banner.widget.BillboardBannerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        final BillboardBannerView bannerView = findViewById(R.id.bannerView);
        final TextView contentView = findViewById(R.id.contentView);
        View button = findViewById(R.id.btn);

        //1、初始化一些需要的素材
        String userName = "白欣欣";
        String plateTypeName = "守护爱情许愿牌";
        final String colorStr = "#DC143C";
        //2、构建原始文本，就是不加Html颜色代码的原始字符串
        String originText = buildOriginText(userName, plateTypeName);
        //3、根据文本的宽度，重新设置TextView的宽度，因为布局中我们设置了matchParent
        measureTextWidthAndSetParams(contentView, originText);
        //4、设置嵌入Html代码的文本给TextView
        contentView.setText(Html.fromHtml(buildWithColorText(userName, plateTypeName, colorStr)));
        //5、计算滚动时间，用每个字符移动400毫秒来计算总需要的时间
        long duration = getTextScrollTime(originText.length(), 400);
        //6、设置时间，并开始滚动
        bannerView.config(duration);

        //7、设置切换按钮点击事件
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = "沈亦臻";
                String plateTypeName = "健康长寿许愿牌";
                //8、重新设置新的文字
                String newText = buildOriginText(userName, plateTypeName);
                String newColorText = buildWithColorText(userName, plateTypeName, colorStr);
                contentView.setText(Html.fromHtml(newColorText));
                measureTextWidthAndSetParams(contentView, newColorText);
                bannerView.config(getTextScrollTime(newText.length(), 400));
            }
        });
    }

    /**
     * 测量文本宽度，并且设置TextView的宽度
     */
    private void measureTextWidthAndSetParams(TextView textView, String text) {
        //计算原始文本的长度，重新设置TextView的宽
        float rawTextWidth = textView.getPaint().measureText(text);
        ViewGroup.LayoutParams params = textView.getLayoutParams();
        params.width = (int) rawTextWidth;
        textView.setLayoutParams(params);
    }

    /**
     * 计算移动文本需要的时间，按文本长度和单个字符移动时间做乘法计算出总时间
     *
     * @param textLength
     * @param singleCharScrollTime
     * @return
     */
    private long getTextScrollTime(int textLength, long singleCharScrollTime) {
        return textLength * singleCharScrollTime;
    }

    /**
     * 构建原始文本
     */
    private String buildOriginText(String userName, String plateTypeName) {
        return userName + " 于2017年12月29日 在许愿树使用 " + plateTypeName + " 许愿，愿她美梦成真！";
    }

    /**
     * 原始文本嵌入Html颜色代码
     */
    private String buildWithColorText(String userName, String plateTypeName, String colorStr) {
        //将颜色HTML代码嵌入到原始文本上
        return setColor(userName, colorStr) + " 于2017年12月29日 在许愿树使用 " + setColor(plateTypeName, colorStr) + " 许愿，愿她美梦成真！";
    }

    /**
     * 将文本嵌入到Html颜色代码中
     */
    private String setColor(String content, String colorStr) {
        return "<font color='" + colorStr + "'>" + content + "</font>";
    }
}