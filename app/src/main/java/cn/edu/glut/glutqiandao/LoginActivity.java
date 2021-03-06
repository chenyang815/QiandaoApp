package cn.edu.glut.glutqiandao;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.allen.library.SuperButton;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

import cn.edu.glut.glutqiandao.location.LocationTestActivity;
import cn.edu.glut.glutqiandao.model.Student;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    private CheckBox rememberpasswd;
    private CheckBox autologin;
    private EditText mStusid;
    private EditText mPasswordView;
    private SuperButton registerbt,loginbt;
    private static boolean state;
    private static Handler handler = new Handler();
    private static String url="http://tomcat.ishare20.cn:8080/slogin";
    private static String url2="http://tomcat.ishare20.cn:8080/getStudentInfoAction";
    static ProgressDialog progressDialog=null;

    private boolean iSremeberpasswd;
    private boolean iSautologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mStusid=(EditText)findViewById(R.id.stusid);
        mPasswordView = (EditText) findViewById(R.id.password);
        registerbt=findViewById(R.id.register_button);
        loginbt=findViewById(R.id.sign_in_button);
        autologin=findViewById(R.id.autologin);
        rememberpasswd= (CheckBox) findViewById(R.id.rememberpasswd);

        rememberpasswd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    iSremeberpasswd=true;
                }else {
                    iSremeberpasswd=false;
                }
            }
        });

        autologin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    iSautologin=true;
                }else {
                    iSautologin=false;
                }
            }
        });

        if (getSharedPreferences("data",MODE_PRIVATE)!=null){



            SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
            //如果上次记住密码
            if(sp.getString("isremeberpasswd","false").equals("true")){
                rememberpasswd.setChecked(true);
                mStusid.setText(sp.getString("sid",""));
                mPasswordView.setText(sp.getString("spasswd",""));
                MApplication.setSid(sp.getString("sid",""));


            }
            Intent intent=getIntent();
            if (intent.getStringExtra("exit")==null){
                //如果上次选自动登录
                if (sp.getString("isautologin","false").equals("true")){
                    autologin.setChecked(true);
                    new Thread(new LoginThread()).start();
                }

            }




        }


        registerbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });



        loginbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (iSremeberpasswd){
                    SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString("sid",mStusid.getText().toString());
                    editor.putString("spasswd",mPasswordView.getText().toString());
                    editor.putString("isremeberpasswd","true");
                    if(iSautologin){
                        editor.putString("isautologin","true");
                    }
                    MApplication.setSid(mStusid.getText().toString());

                    editor.commit();//保存密码
                }


                new Thread(new LoginThread()).start(); //登录


            }
        });

    }


    //点击返回桌面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

public class LoginThread extends Thread implements Runnable{

    private boolean login() throws IOException {

        OkHttpClient client = new OkHttpClient();

       RequestBody requestBody =new FormBody.Builder()
               .add("student.id",mStusid.getText().toString())
               .add("student.passwd",mPasswordView.getText().toString())
               .build();

        Request request=new Request.Builder().post(requestBody).url(url).build();
        Response response=client.newCall(request).execute();
        String rs=response.body().string();
        if (rs.contains("true")){

            return true;
        }
        else {
            return false;
        }


    }

    //保存学生资料
    private void setProfile(String sid) throws IOException{
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody =new FormBody.Builder()
                .add("sid",mStusid.getText().toString())
                .build();

        Request request=new Request.Builder().post(requestBody).url(url2).build();
        Response response=client.newCall(request).execute();
        String rs=response.body().string();
        ReadContext context = JsonPath.parse(rs);
        String objstr=context.read("$.studentInfo").toString();
        Gson gson=new Gson();
        Student student=gson.fromJson(objstr,Student.class);
        SharedPreferences sp=getSharedPreferences("profile",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("id",student.getId());
        editor.putString("name",student.getName());
        editor.putString("college",student.getCollege());
        editor.putString("classes",student.getClasses());
        editor.putString("tel",student.getTel());
        editor.commit();
    }

    @Override
    public void run() {

        try {
            if (login()){
                state=true;
                setProfile(mStusid.getText().toString());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }



           handler.post(new Runnable() {
               @Override
               public void run() {
                    if (state){
                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                    }
               }
           });

    }


}


}

