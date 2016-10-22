package com.androidex.mybrowser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    //private TextView tv_title;
    private ImageView find, reload;
    private MyEditText et_title;
    private ProgressBar progressBar;
    private ProgressWebView webview;
    private ImageButton brak_btn;
    private TextView tv_error;
    final String TAG = "标识";

    //private String url = "http://dict.youdao.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏设置，隐藏窗口所有装饰
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);//清除FLAG
        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN, android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sendBroadcast(new Intent("com.android.action.hide_navigationbar"));
        setContentView(R.layout.activity_main);
        // 控件实例化
        brak_btn = (ImageButton) findViewById(R.id.brak_btn);
        find = (ImageView) findViewById(R.id.find);
        tv_error = (TextView) findViewById(R.id.tv_error);
        reload = (ImageView) findViewById(R.id.reload);
        et_title = (MyEditText) findViewById(R.id.et_title);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webview = (ProgressWebView) findViewById(R.id.webview);
        // 绑定监听
        find.setOnClickListener(this);
        brak_btn.setOnClickListener(this);
        reload.setOnClickListener(this);
        et_title.setOnClickListener(this);

        // 支持JavaScript
        webview.getSettings().setJavaScriptEnabled(true);

        // 提取上一次输入的url地址
        SharedPreferences settings = this.getSharedPreferences("PREFS_CONF", Context.MODE_PRIVATE);
        SharedPreferences.Editor edtor = settings.edit();
        String url = settings.getString(TAG, "");//取得保存的值
        if (url == "") {
            webViewCallBack();
            webview.loadUrl("http://www.baidu.com/");
        } else {
            webViewCallBack();
            webview.loadUrl("http://www.baidu.com/");
            webview.loadUrl(url);
        }

        //网页加载失败
       /* webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                webview.setVisibility(View.GONE);
                tv_error.setVisibility(View.VISIBLE);
            }
        });*/
    }
    //设置webview的回掉函数,获得Url
    private void webViewCallBack() {
        webview.setOnWebCallBack(new OnWebCallBack() {
            //获取标题
            @Override
            public void getTitle(String title) {
                //tv_title.setText( title ) ;
            }

            //获取当前web的URL地址
            @Override
            public void getUrl(String url) {
                et_title.setText(url);
                //et_title.setFocusable(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find://收索
                String url = "http://" + et_title.getText().toString().trim();
                webViewCallBack();
                webview.loadUrl(url);
                break;
            case R.id.reload://刷新
                webview.reload();
                break;
            case R.id.et_title:
                if (et_title.getText().toString() != "") {
                    //et_title.setFocusable(true);
                    et_title.setText(et_title.getText().toString());
                    et_title.selectAll();
                    //设置光标在文字后面
                    Editable b = et_title.getText();
                    et_title.setSelection(b.length());
                }
                break;
            case R.id.brak_btn://返回
                if (webview.canGoBack()) {
                    webview.goBack();
                } else {
                    //finish();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("是否退出APP");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendBroadcast(new Intent("com.android.action.display_navigationbar"));
                            finish();
                        }
                    });
                    builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences settings = this.getSharedPreferences("PREFS_CONF",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edtor = settings.edit();
        edtor.putString(TAG, et_title.getText().toString().trim());
        edtor.commit();
    }
}