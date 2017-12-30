package com.hzh.billboard.banner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.hzh.billboard.banner.widget.BillboardBannerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        BillboardBannerView bannerView = findViewById(R.id.bannerView);

        String userName = "白欣欣";
        String plateName = "守护爱情许愿牌";
        String colorStr = "#DC143C";

        String originText = userName + " 于2017年12月29日 在许愿树使用 " + plateName + " 许愿，愿她美梦成真！";
        //拼接颜色和语句
        String tipText = setColor(userName, colorStr) + " 于2017年12月29日 在许愿树使用 " + setColor(plateName, colorStr) + " 许愿，愿她美梦成真！";
        bannerView.configText(originText, tipText);
    }

    private String setColor(String content, String colorStr) {
        return "<font color='" + colorStr + "'>" + content + "</font>";
    }
}