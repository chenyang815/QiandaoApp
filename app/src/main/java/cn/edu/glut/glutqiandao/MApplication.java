package cn.edu.glut.glutqiandao;

import android.app.Application;
import android.content.Context;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * Created by ishare on 17-7-23.
 */

public class MApplication extends Application {
    private static Context context;
    private static String sid;
    private static String aid;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        ZXingLibrary.initDisplayOpinion(this);

    }

    public static String getAid() {
        return aid;
    }

    public static void setAid(String aid) {
        MApplication.aid = aid;
    }


    public static Context getContext() {
        return context;
    }

    public static String getSid() {
        return sid;
    }

    public static void setSid(String sid) {
        MApplication.sid = sid;
    }
}
