package cn.edu.glut.glutqiandao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {


    private EditText mStusid;
    private EditText mPasswordView;
    private Button registerbt,loginbt;
    private static boolean state;
    private static Handler handler = new Handler();
    private static String url="http://192.168.43.170:8080/qiandao/slogin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mStusid=(EditText)findViewById(R.id.stusid);
        mPasswordView = (EditText) findViewById(R.id.password);
        registerbt=(Button)findViewById(R.id.register_button);
        loginbt= (Button) findViewById(R.id.sign_in_button);

        if (getSharedPreferences("data",MODE_PRIVATE)!=null){

            //自动登录

            SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
            mStusid.setText(sp.getString("sid",""));
            mPasswordView.setText(sp.getString("spasswd",""));
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
                SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("sid",mStusid.getText().toString());
                editor.putString("spasswd",mPasswordView.getText().toString());
                editor.commit();//保存密码

                new Thread(new LoginThread()).start(); //登录
            }
        });

    }

public class LoginThread extends Thread implements Runnable{
    @Override
    public void run() {
        Connection.Response rs=null;
        try {

            rs=Jsoup.connect(url).data("sid",mStusid.getText().toString())
                    .data("spasswd",mPasswordView.getText().toString())
                    .method(Connection.Method.POST).execute();

            //Log.d("code","code is "+rs.statusCode());
            if (rs.body().contains("true")){

                   state=true;
            }else {
                state=false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

       handler.post(new Runnable() {
           @Override
           public void run() {
                if (state){
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

