package cn.edu.glut.glutqiandao.questionnaire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;

import cn.edu.glut.glutqiandao.MApplication;
import cn.edu.glut.glutqiandao.R;
import cn.edu.glut.glutqiandao.model.AndroidtoJs;
import cn.edu.glut.glutqiandao.model.QuizCode;
import cn.edu.glut.glutqiandao.model.QunaCode;

public class AnswerQuestionnaireActivity extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_questionnaire);

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        Gson gson = new Gson();
        QunaCode qunaCode = gson.fromJson(data, QunaCode.class);

        webView= (WebView) findViewById(R.id.main_webview);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });

        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new AndroidtoJs(AnswerQuestionnaireActivity.this),"close");
        webView.loadUrl(qunaCode.getLink()+"&sid="+ MApplication.getSid());

    }
}
