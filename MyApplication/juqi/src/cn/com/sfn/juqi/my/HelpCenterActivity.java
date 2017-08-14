package cn.com.sfn.juqi.my;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.juqi.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.sfn.juqi.util.Config;

/**
 * 帮助中心
 * Created by wuxiaojun on 17-8-14.
 */

public class HelpCenterActivity extends Activity {

    @BindView(R.id.id_webview)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_help_center);
        ButterKnife.bind(this);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(Config.URL_BASE + "protocol/helpCenter");
    }


    @OnClick({R.id.id_back})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.id_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
        webView = null;
    }
}
