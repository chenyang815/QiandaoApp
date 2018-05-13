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

    public static String getSectionText(int section){

        if (section==1){
            return "1、2节";
        }
        else if (section==3){
            return "3、4节";
        }else if (section==5)

        {
            return "5、6节";
        }else if (section==7){
            return "7、8节";
        }else
        {
            return "9、10节";
        }

    }
}
