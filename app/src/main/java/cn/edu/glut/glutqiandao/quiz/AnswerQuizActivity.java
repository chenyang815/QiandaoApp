package cn.edu.glut.glutqiandao.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import cn.edu.glut.glutqiandao.MApplication;
import cn.edu.glut.glutqiandao.R;
import cn.edu.glut.glutqiandao.model.Code;
import cn.edu.glut.glutqiandao.model.QuizCode;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AnswerQuizActivity extends AppCompatActivity {

    private TextView titleTextview,courseTextview;
    private TextView answerBt;
    private QuizCode quizCode;
    private String datastr;
    private boolean isSameAnswer;
    private static Handler handler = new Handler();
    static String querylink="http://192.168.31.135:8080/queryaid";
    String qid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_quiz);

        titleTextview = (TextView) findViewById(R.id.title);
        courseTextview= (TextView) findViewById(R.id.course);
        answerBt= (TextView) findViewById(R.id.answerbt);


        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        Gson gson = new Gson();
        quizCode = gson.fromJson(data, QuizCode.class);

        titleTextview.setText(quizCode.getTitle());
        courseTextview.setText(quizCode.getCname());

        answerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp=getSharedPreferences("answer",MODE_PRIVATE);
                System.out.println(quizCode.getQid());

                    if (sp.getString("qid","").equals(quizCode.getQid()+"")){
                        //System.out.println("if is"+sp.getString("qid","")+"and quizcode is"+quizCode.getQid());
                        Toast.makeText(AnswerQuizActivity.this,"你已经回答了该题目，请勿重复提交",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //System.out.println("else is "+sp.getString("qid",""));
                        new Thread(new AnswerThread()).start();
                    }



            }
        });

    }


    public class AnswerThread extends Thread implements Runnable{

        @Override
        public void run() {
            OkHttpClient client=new OkHttpClient();
            RequestBody requestBody =new FormBody.Builder()
                    .add("sid", MApplication.getSid())
                    .add("qid",quizCode.getQid()+"")
                    .build();

            Request request=new Request.Builder().post(requestBody).url(quizCode.getLink()).build();
            try {
                Response response=client.newCall(request).execute();
                datastr=response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    JsonParser parser=new JsonParser();
                    JsonElement element=parser.parse(datastr);
                    JsonObject jsonObj = element.getAsJsonObject();
                    String str=jsonObj.get("msg").getAsString();
                    //存答题的id
                    //MApplication.setAid();
                    SharedPreferences sp=getSharedPreferences("answer",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString("qid",quizCode.getQid()+"");
                    editor.commit();

                    Toast.makeText(AnswerQuizActivity.this,str,Toast.LENGTH_SHORT).show();

                }
            });
        }
    }


 /*   public class QueryAidThread extends Thread implements Runnable{
        @Override
        public void run() {
            OkHttpClient client=new OkHttpClient();
            RequestBody requestBody =new FormBody.Builder()
                    .add("aid",qid)
                    .build();

            Request request=new Request.Builder().post(requestBody).url(querylink).build();
            try {
                Response response=client.newCall(request).execute();
                String str=response.body().string();
                JsonParser parser=new JsonParser();
                JsonElement element=parser.parse(str);
                JsonObject jsonObj = element.getAsJsonObject();
                String flag=jsonObj.get("flag").getAsString();
                if (flag.equals("true")){
                    isSameAnswer=true;
                }else {
                    isSameAnswer=false;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (isSameAnswer){
                    }
                    else {
                        new Thread(new AnswerThread()).start();
                    }
                }
            });

        }


    }*/

}
