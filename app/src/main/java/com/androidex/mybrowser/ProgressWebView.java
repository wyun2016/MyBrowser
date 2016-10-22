package com.androidex.mybrowser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by ASUS on 2016/10/21.
 */

public class ProgressWebView extends WebView {
    private Context context;
    private ProgressBar progressbar;
    private TextView tv_error;
    private ProgressWebView webview;
    private OnWebCallBack onWebCallBack;   //回调

    public ProgressWebView(Context context) {
        this(context, null);
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webTextViewStyle);
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;

        init();

        setWebViewClient(new MyWebViewClient());
        setWebChromeClient(new WebChromeClient());
    }

    /**
     * 设置ProgressBar
     */
    void init() {
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 20, 0, 0));
        addView(progressbar);
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }


        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (onWebCallBack != null) {  //获取标题
                onWebCallBack.getTitle(title);
            }
        }

    }

    /**
     * 不重写的话，会跳到手机浏览器中
     *
     * @author admin
     */
    public class MyWebViewClient extends WebViewClient {
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // Handle the
            goBack();
            //tv_error.setVisibility(VISIBLE);
            //webview.setVisibility(GONE);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (onWebCallBack != null) { //获得WebView的地址
                onWebCallBack.getUrl(url);
            }
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * 设置WebView的回掉器
     *
     * @param onWebCallBack
     */
    void setOnWebCallBack(OnWebCallBack onWebCallBack) {
        this.onWebCallBack = onWebCallBack;
    }
}

interface OnWebCallBack {
    /**
     * 获取标题
     *
     * @param title
     */
    void getTitle(String title);

    /**
     * 获得WebView的地址
     *
     * @param url
     */
    void getUrl(String url);

}

