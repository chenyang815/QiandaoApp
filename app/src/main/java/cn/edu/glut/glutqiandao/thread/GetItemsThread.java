package cn.edu.glut.glutqiandao.thread;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import java.io.IOException;
import java.util.List;

import cn.edu.glut.glutqiandao.MApplication;
import cn.edu.glut.glutqiandao.R;
import cn.edu.glut.glutqiandao.adapter.AttendanceItemAdapter;
import cn.edu.glut.glutqiandao.model.AnswerItem;
import cn.edu.glut.glutqiandao.model.AttendanceItem;
import cn.edu.glut.glutqiandao.model.CourseItem;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetItemsThread extends Thread implements Runnable {
    String link ;
    public String  responsestr;
    public List<AttendanceItem> attendanceItems;
    public List<AnswerItem> answerItems;
    public List<CourseItem> courseItems;
    int type;
    public GetItemsThread(String link,int type) {
        this.type=type;
        this.link = link;
    }

    @Override

    public void run() {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("sid", MApplication.getSid())
                .build();

        Request request = new Request.Builder().post(requestBody).url(link).build();
        try {
            Response response = client.newCall(request).execute();
            responsestr=response.body().string();


            System.out.println("respon is----"+responsestr);

            ReadContext context = JsonPath.parse(responsestr);
            if (type==0){
                String objsstr=context.read("$.attendanceItems").toString();
                Gson gson=new Gson();
                attendanceItems=gson.fromJson(objsstr,new TypeToken<List<AttendanceItem>>(){}.getType());
            }else if (type==1){
                String objsstr=context.read("$.answerItems").toString();
                Gson gson=new Gson();
                answerItems=gson.fromJson(objsstr,new TypeToken<List<AnswerItem>>(){}.getType());
            }else if (type==2){
                String objsstr=context.read("$.courseItems").toString();
                Gson gson=new Gson();
                courseItems=gson.fromJson(objsstr,new TypeToken<List<CourseItem>>(){}.getType());
            }


            //attendanceItemAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
