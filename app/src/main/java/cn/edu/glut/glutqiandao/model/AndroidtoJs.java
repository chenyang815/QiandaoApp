package cn.edu.glut.glutqiandao.model;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by ishare on 18-5-4.
 */

public class AndroidtoJs {

    Activity mContxt;

    public AndroidtoJs(Activity mContxt) {
        this.mContxt = mContxt;
    }


    @JavascriptInterface
    public void closeWebView(String msg) {
        Toast.makeText(mContxt, msg, Toast.LENGTH_LONG).show();
        mContxt.finish();
    }

}
