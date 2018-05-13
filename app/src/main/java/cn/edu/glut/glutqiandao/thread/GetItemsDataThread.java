package cn.edu.glut.glutqiandao.thread;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import java.io.IOException;
import java.util.List;

import cn.edu.glut.glutqiandao.MApplication;
import cn.edu.glut.glutqiandao.model.AnswerItem;
import cn.edu.glut.glutqiandao.model.AttendanceItem;
import cn.edu.glut.glutqiandao.model.CourseItem;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetItemsDataThread extends Thread implements Runnable {
    public String cid;
    public String datesection;
    public int page;
    public int limit;
    public int type;
    public List<?> itemdatas;
    public String link;
    public int totalAnswerItem;
    public int totalAttendanceItem;

    public GetItemsDataThread(String cid, String datesection, int page, int limit, int type,String link) {
        this.cid = cid;
        this.datesection = datesection;
        this.page = page;
        this.limit = limit;
        this.type = type;
        this.link=link;
    }

    @Override
    public void run() {
        OkHttpClient client = new OkHttpClient();
        if (datesection==null){
            datesection="";
        }
        RequestBody requestBody = new FormBody.Builder()
                .add("sid", MApplication.getSid())
                .add("cid",cid)
                .add("datesection",datesection)
                .add("type",type+"")
                .add("page",page+"")
                .add("limit",limit+"")
                .build();

        Request request = new Request.Builder().post(requestBody).url(link).build();

        try {
            Response response = client.newCall(request).execute();
            String responsestr=response.body().string();


            System.out.println("this respon is----"+responsestr);

            ReadContext context = JsonPath.parse(responsestr);
            if (type==0){
                String objsstr="";
                if (context.read("$.attendanceItems")!=null){
                     objsstr=context.read("$.attendanceItems").toString();
                }


                totalAttendanceItem=Integer.parseInt(context.read("$.totalAttendanceItem").toString());
                if (objsstr!=null){
                    Gson gson=new Gson();
                    itemdatas=gson.fromJson(objsstr,new TypeToken<List<AttendanceItem>>(){}.getType());
                }

            }else if (type==1) {
                String objsstr="";
                if (context.read("$.answerItems")!=null){
                    objsstr=context.read("$.answerItems").toString();
                }

                totalAnswerItem=Integer.parseInt(context.read("$.totalAnswerItem").toString());
                if (objsstr!=null){
                    Gson gson = new Gson();
                    itemdatas = gson.fromJson(objsstr, new TypeToken<List<AnswerItem>>() {
                    }.getType());
                }

            }


            //attendanceItemAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
