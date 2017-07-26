package cn.edu.glut.glutqiandao;

import android.app.Application;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * Created by ishare on 17-7-23.
 */

public class MApplication extends Application {

@Override
public void onCreate() {
    super.onCreate();
    ZXingLibrary.initDisplayOpinion(this);
  }
}
