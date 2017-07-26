package cn.edu.glut.glutqiandao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.jayway.jsonpath.JsonPath;

public class CourseActivity extends AppCompatActivity {

    TextView t1,t2,t3;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Intent intent=getIntent();
        String data=intent.getStringExtra("data");
        String teacher= JsonPath.read(data,"$.teacher");
        String course= JsonPath.read(data,"$.course");
        String link= JsonPath.read(data,"$.link");
        t1=(TextView)findViewById(R.id.textView1);
        t2=(TextView)findViewById(R.id.textView2);
        t3=(TextView)findViewById(R.id.textView3);
        b1=(Button)findViewById(R.id.buttonqq);
        t1.setText(teacher);
        t2.setText(course);
        t3.setText(link);
    }
}
