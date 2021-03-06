package cn.edu.glut.glutqiandao;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.allen.library.SuperButton;
import com.allen.library.SuperTextView;

import net.minidev.json.JSONObject;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImportActivity extends AppCompatActivity {

    static String url = "http://202.193.80.58:81/academic/student/studentinfo/studentInfoModifyIndex.do?frombase=0&wantTag=0&groupId=&moduleId=2060";
    static String url3 = "http://202.193.80.58:81/academic/student/currcourse/currcourse.jsdo?groupId=&moduleId=2000";
    static String postUrl = "http://192.168.31.135:8080/sturegister";
    static String cookie = "";
    static String userid = "";
    static String name = "";
    static String college = "";
    static String marjor = "";
    static String classes = "";
    static String imei = "";
    static int len;
    boolean state = false;
    static List<String> courseid;
    static List<String> courname;

    private SuperTextView useridTextView, nameTextView, collegeTextView, marjorTextView, classesTextView, coursenumberTextView, phoneimeiTextView;
    private EditText passwd, phone;

    private SuperButton registerbt;
    static ProgressDialog progressDialog = null;
    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        useridTextView =  findViewById(R.id.userid);
        nameTextView =  findViewById(R.id.name);
        collegeTextView =  findViewById(R.id.college);
        marjorTextView =  findViewById(R.id.major);
        classesTextView =  findViewById(R.id.classes);
        phoneimeiTextView =  findViewById(R.id.phoneimei);

        coursenumberTextView =  findViewById(R.id.course_number);

        passwd = (EditText) findViewById(R.id.app_passwd);
        phone = (EditText) findViewById(R.id.phone);
        registerbt =  findViewById(R.id.res_bt);
        TelephonyManager telephonyManager = (TelephonyManager) MApplication.getContext().getSystemService(MApplication.getContext().TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
         imei = telephonyManager.getDeviceId();

        phoneimeiTextView.setRightString(imei);

        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("正在解析教务处信息......");
        registerbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (phone.getText().toString().equals("") ){

                        Toast.makeText(ImportActivity.this,"手机号码为空，请输入",Toast.LENGTH_SHORT).show();

                    }else if (passwd.getText().toString().equals("")){

                        Toast.makeText(ImportActivity.this,"密码为空，请输入",Toast.LENGTH_SHORT).show();


                    }else if (passwd.getText().toString().length()<6){
                        Toast.makeText(ImportActivity.this,"请输入大于6位的密码",Toast.LENGTH_SHORT).show();

                    }
                    else if (phone.getText().toString().length()!=11){
                        Toast.makeText(ImportActivity.this,"手机号码位数不对，请重新输入",Toast.LENGTH_SHORT).show();
                    }

                    else {
                        new Thread(new RegisterThread()).start();
                    }



            }
        });

        Intent intent=getIntent();
        cookie=intent.getStringExtra("cookie");

        new Thread(new ParseHtml()).start();//开启解析线程
        progressDialog.show();
        //Toast.makeText(ImportActivity.this,"正在解析.......",Toast.LENGTH_LONG).show();
    }

    private boolean submitProfie() throws IOException {

        Map<String,String> postData=new HashMap<>();
        postData.put("student.id",userid);
        postData.put("student.name",name);
        postData.put("student.passwd",passwd.getText().toString());
        postData.put("student.college",college);
        postData.put("student.profession",marjor);
        postData.put("student.classes",classes);
        postData.put("student.tel",phone.getText().toString());

        String courseids="";
        String courses="";
        for (String id:
                courseid) {
            courseids=courseids+" "+id;
        }

        for (String course:
                courname){
            courses=courses+" "+course;
        }
        postData.put("ids",courseids);
        postData.put("courses",courses);


        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client=new OkHttpClient();
        RequestBody requestBody=RequestBody.create(JSON, JSONObject.toJSONString(postData));
        Request request = new Request.Builder().url(postUrl).post(requestBody).build();
        Response response=client.newCall(request).execute();
        if (response.body().toString().contains("true")){
            return true;
        }else {
            return  false;
        }

    }

    private boolean submitData(){

        boolean check=false;
        Map<String,String> postData=new HashMap<>();
        postData.put("student.id",userid);
        postData.put("student.name",name);
        postData.put("student.passwd",passwd.getText().toString());
        postData.put("student.college",college);
        postData.put("student.profession",marjor);
        postData.put("student.classes",classes);
        postData.put("student.tel",phone.getText().toString());
        postData.put("student.imei",imei);

        String courseids="";
        String courses="";
        for (String id:
             courseid) {
            courseids=courseids+" "+id;
        }

        for (String course:
                courname){
            courses=courses+" "+course;
        }
        postData.put("ids",courseids);
        postData.put("courses",courses);

        Connection.Response rs=null;
        try {
            rs=Jsoup.connect(postUrl).data(postData).method(Connection.Method.POST).execute();
            System.out.println(rs.body());
            if (rs.body().contains("true")){

                check=true;

            }
            else {
                check=false;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return check;
    }

    public class ParseHtml extends Thread implements Runnable{
        @Override
        public void run() {
            Document document= null;
            Document document2=null;
            try {
                document = Jsoup.connect(url).cookie("JSESSIONID",cookie).get();
                document2=Jsoup.connect(url3).cookie("JSESSIONID",cookie).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            userid=document.getElementsByAttributeValue("name","username").get(0).parent().text();
            name=document.getElementsByAttributeValue("name","realname").get(0).parent().text();
            college=document.getElementsByAttributeValue("name","departmentid").get(0).parent().text();
            marjor=document.getElementsByAttributeValue("name","majorid").get(0).parent().text();
            classes=document.getElementsByAttributeValue("name","classid").get(0).parent().text();

             courseid=new ArrayList<>();
             courname=new ArrayList<>();

            len=document2.getElementsByTag("table").get(3).select(".infolist_common").size();
            System.out.println("len is "+len);

            for (int i=0;i<len;i++){

                courseid.add(document2.getElementsByTag("table").get(3).select(".infolist_common").get(i).child(0).text()+
                        document2.getElementsByTag("table").get(3).select(".infolist_common").get(i).child(1).text());

                courname.add(document2.getElementsByTag("table").get(3).select(".infolist_common").get(i).child(2).text());

            }

            handler.post(new Runnable() {
                @Override
                public void run() {

                    useridTextView.setRightString(userid);
                    nameTextView.setRightString(name);
                    collegeTextView.setRightString(college);
                    marjorTextView.setRightString(marjor);
                    classesTextView.setRightString(classes);
                    coursenumberTextView.setRightString(len+"");

                    LinearLayout ll= (LinearLayout) findViewById(R.id.course_list);

                    for (int i=0;i<len;i++){
                        TextView tv=new TextView(ImportActivity.this);
                        tv.setText(courname.get(i));
                        ll.addView(tv);

                    }

                   progressDialog.dismiss();

                }
            });
        }
    }

    public class RegisterThread extends Thread implements Runnable{
        @Override
        public void run() {
                if (submitData()){

                    state=true;

            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (state){
                        Toast.makeText(ImportActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                       //保存学生信息
                        SharedPreferences sp=getSharedPreferences("profile",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putString("sid",userid);
                        editor.putString("sname",name);
                        editor.putString("scollege",college);
                        editor.putString("sprofession",marjor);
                        editor.putString("sclass",classes);
                        editor.putString("sphone",phone.getText().toString());
                        editor.commit();

                        Intent intent =new Intent(ImportActivity.this,LoginActivity.class);
                        startActivity(intent); //注册成功，跳转到登录
                    }else {
                        Toast.makeText(ImportActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
