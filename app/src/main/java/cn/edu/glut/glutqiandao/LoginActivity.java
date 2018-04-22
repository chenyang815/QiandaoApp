package cn.edu.glut.glutqiandao;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

import cn.edu.glut.glutqiandao.location.LocationTestActivity;
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
    private EditText mStusid;
    private EditText mPasswordView;
    private Button registerbt,loginbt;
    private static boolean state;
    private static Handler handler = new Handler();
    private static String url="http://192.168.0.106:8080/slogin";
    static ProgressDialog progressDialog=null;
    private Button locatestBt;
    private boolean isremeberpasswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mStusid=(EditText)findViewById(R.id.stusid);
        mPasswordView = (EditText) findViewById(R.id.password);
        registerbt=(Button)findViewById(R.id.register_button);
        loginbt= (Button) findViewById(R.id.sign_in_button);
        locatestBt= (Button) findViewById(R.id.locationtest);
        rememberpasswd= (CheckBox) findViewById(R.id.rememberpasswd);

        rememberpasswd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    isremeberpasswd=true;
                }else {
                    isremeberpasswd=false;
                }
            }
        });

        if (getSharedPreferences("data",MODE_PRIVATE)!=null){

            //自动登录

            SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
            mStusid.setText(sp.getString("sid",""));
            mPasswordView.setText(sp.getString("spasswd",""));

            new Thread(new LoginThread()).start();

        }


        registerbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        locatestBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(LoginActivity.this, LocationTestActivity.class);
                startActivity(intent);
            }
        });


        loginbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isremeberpasswd){
                    SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString("sid",mStusid.getText().toString());
                    editor.putString("spasswd",mPasswordView.getText().toString());
                    editor.commit();//保存密码
                }


                new Thread(new LoginThread()).start(); //登录


            }
        });

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
       // System.out.println(response.body().string());
        String rs=response.body().string();
        if (rs.contains("true")){

            return true;
        }
        else {
            return false;
        }


    }

    @Override
    public void run() {

        try {
            if (login()){
                state=true;
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

