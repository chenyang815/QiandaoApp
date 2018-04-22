package cn.edu.glut.glutqiandao.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import cn.edu.glut.glutqiandao.R;
import cn.edu.glut.glutqiandao.model.Code;
import cn.edu.glut.glutqiandao.model.QuizCode;

public class AnswerQuizActivity extends AppCompatActivity {

    private TextView titleTextview,courseTextview;
    private TextView answerBt;
    private QuizCode quizCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_quiz);

        titleTextview = (TextView) findViewById(R.id.title);
        courseTextview= (TextView) findViewById(R.id.course);
        answerBt= (TextView) findViewById(R.id.answerbt);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        Gson gson = new Gson();
        quizCode = gson.fromJson(data, QuizCode.class);

        titleTextview.setText(quizCode.getTitle());
        courseTextview.setText(quizCode.getCname());

    }

}
