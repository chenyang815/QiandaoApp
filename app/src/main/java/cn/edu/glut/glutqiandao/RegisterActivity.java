package cn.edu.glut.glutqiandao;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    static String url1="http://202.193.80.58:81/academic/common/security/login.jsp";
    static String url2="http://202.193.80.58:81/academic/getCaptcha.do";
    EditText username,passwd,code;
    ImageView codepic;
    Button loginbt;
    static String cookie="";
    Bitmap bitmap=null;

    private static Handler handler = new Handler();
    // private static Handler handler2 = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username=(EditText)findViewById(R.id.user);
        passwd=(EditText)findViewById(R.id.passwd);
        code=(EditText)findViewById(R.id.code);
        codepic= (ImageView) findViewById(R.id.codepic);
        loginbt= (Button) findViewById(R.id.login2);
        loginbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cookie.equals("")){
                    Toast.makeText(RegisterActivity.this,"cookie is empty",Toast.LENGTH_SHORT).show();
                }
                else {

                    //Toast.makeText(Register2Activity.this,"cookie is"+cookie,Toast.LENGTH_SHORT).show();
                    new Thread(new MyThread2()).start();

                }
            }
        });
        new Thread(new MyThread()).start();//子线程开启网络连接

        Toast.makeText(RegisterActivity.this,cookie,Toast.LENGTH_LONG).show();
    }

    private String getCookie(String url){
        Connection.Response rs=null;
        try {
            rs = Jsoup.connect(url).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rs.cookie("JSESSIONID");
    }
    public  boolean sumbitForm(){
        try {
            System.out.println(username.getText().toString());
            Jsoup.connect("http://202.193.80.58:81/academic/j_acegi_security_check")
                    .data("j_username",username.getText().toString()).data("j_password",passwd.getText().toString()).data("j_captcha",code.getText().toString())
                    .data("groupId","").cookie("JSESSIONID",cookie).method(Connection.Method.POST).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static byte[] getImage(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");   //设置请求方法为GET
        conn.setReadTimeout(5*1000);    //设置请求过时时间为5秒
        conn.setRequestProperty("Cookie","JSESSIONID="+cookie);
        InputStream inputStream = conn.getInputStream();   //通过输入流获得图片数据
        byte[] data = readInputStream(inputStream);     //获得图片的二进制数据
        return data;

    }

    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();

    }
    public class MyThread extends Thread implements Runnable{
        public void run(){
            cookie=getCookie(url1);//获取cookie

            System.out.println(cookie);

            try {
                byte[] data = getImage(url2);
                bitmap= BitmapFactory.decodeByteArray(data, 0, data.length);  //生成位图

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        codepic.setImageBitmap(bitmap);   //显示图片
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class MyThread2 extends Thread implements Runnable{
        @Override
        public void run() {

            sumbitForm();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(RegisterActivity.this,ImportActivity.class);
                    intent.putExtra("cookie",cookie);
                    startActivity(intent);
                }
            });
        }

    }
}
