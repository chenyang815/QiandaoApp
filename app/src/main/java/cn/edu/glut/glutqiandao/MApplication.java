package cn.edu.glut.glutqiandao;

import android.app.Application;
import android.content.Context;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * Created by ishare on 17-7-23.
 */

public class MApplication extends Application {
private static Context context;
@Override
public void onCreate() {
    super.onCreate();
    context = getApplicationContext();
    ZXingLibrary.initDisplayOpinion(this);
  }

  public static Context getContext(){
      return context;
  }
}
