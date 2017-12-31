package com.hzh.billboard.banner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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
        BillboardBannerView bannerView = findViewById(R.id.bannerView);
        TextView contentView = findViewById(R.id.contentView);
        //初始化一些需要的素材
        String userName = "白欣欣";
        String plateName = "守护爱情许愿牌";
        String colorStr = "#DC143C";
        //原始文本，就是不加Html颜色代码的原始字符串
        String originText = userName + " 于2017年12月29日 在许愿树使用 " + plateName + " 许愿，愿她美梦成真！";
        //将颜色HTML代码嵌入到原始文本上
        String tipText = setColor(userName, colorStr) + " 于2017年12月29日 在许愿树使用 " + setColor(plateName, colorStr) + " 许愿，愿她美梦成真！";
        contentView.setText(Html.fromHtml(tipText));
        //计算原始文本的长度，重新设置TextView的宽
        float rawTextWidth = contentView.getPaint().measureText(originText);
        ViewGroup.LayoutParams params = contentView.getLayoutParams();
        params.width = (int) rawTextWidth;
        contentView.setLayoutParams(params);
        //计算滚动时间，用每个字符移动400毫秒来计算总需要的时间
        int duration = originText.toCharArray().length * 400;
        //设置时间，并开始滚动
        bannerView.setDuration(duration);
    }

    /**
     * 将文本嵌入到Html颜色代码中
     */
    private String setColor(String content, String colorStr) {
        return "<font color='" + colorStr + "'>" + content + "</font>";
    }
}