package cn.edu.glut.glutqiandao.util;

import android.content.SharedPreferences;

import cn.edu.glut.glutqiandao.MApplication;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ishare on 18-3-5.
 */

public class Util  {
    public static String getSid(){
        SharedPreferences sp= MApplication.getContext().getSharedPreferences("data",MODE_PRIVATE);
        return sp.getString("sid","");
    }
}
