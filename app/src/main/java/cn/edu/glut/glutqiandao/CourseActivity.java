package cn.edu.glut.glutqiandao;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import cn.edu.glut.glutqiandao.model.Code;
import cn.edu.glut.glutqiandao.util.Util;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class CourseActivity extends AppCompatActivity {

    TextView teacher_textView, course_textView, date_textView,
            class_textView, section_textView,address_textView;
    Button b1,getLocationBt;
    private static Handler handler = new Handler();
    private Code code;
    private String codejson;
    private String address;
    private String coordinate;

    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);


        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        /*String teacher= JsonPath.read(data,"$.teacher");
        String course= JsonPath.read(data,"$.course");
        String link= JsonPath.read(data,"$.link");*/

        Gson gson = new Gson();

         code = gson.fromJson(data, Code.class);

        teacher_textView = (TextView) findViewById(R.id.textView1);
        course_textView = (TextView) findViewById(R.id.textView2);
        date_textView= (TextView) findViewById(R.id.sign_date);
        class_textView = (TextView) findViewById(R.id.classroom);
        address_textView = (TextView) findViewById(R.id.address);
        section_textView = (TextView) findViewById(R.id.section);
        b1 = (Button) findViewById(R.id.buttonqq);

        code.setSid(Util.getSid());

       codejson=gson.toJson(code);

        System.out.println(code.getSection());

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new SignThread()).start();

            }
        });
        getLocationBt = (Button) findViewById(R.id.get_location);

        getLocationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLocation();
            }
        });

        teacher_textView.setText(code.getTname());
        course_textView.setText(code.getCname());
        date_textView.setText(code.getSigndate());
        class_textView.setText(code.getClassroom());
        section_textView.setText(code.getSection()+"节");

        mLocationClient = new AMapLocationClient(getApplicationContext());

        mLocationClient.setLocationListener(mLocationListener);

        mLocationOption=new AMapLocationClientOption();

        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);





        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(CourseActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(CourseActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(CourseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(CourseActivity.this, permissions, 1);
        } else {
            if(null != mLocationClient){
                mLocationClient.setLocationOption(mLocationOption);
                //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
                mLocationClient.stopLocation();
                mLocationClient.startLocation();
            }
        }

    }

    private void requestLocation() {
        mLocationClient.startLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }


    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            StringBuffer locationStr=new StringBuffer();

            //locationStr.append("精度信息：").append(aMapLocation.getAccuracy()).append("\n");
            locationStr.append(aMapLocation.getAddress());

            System.out.println(locationStr);
            address_textView.setText(locationStr);
            address=locationStr.toString();
            coordinate=aMapLocation.getLongitude()+" "+aMapLocation.getLatitude();
            //positonTextView.setText(locationStr);

        }
    };



   public class SignThread extends Thread implements Runnable{
        @Override
        public void run() {

            OkHttpClient client=new OkHttpClient();
            RequestBody requestBody =new FormBody.Builder()
                    .add("codejson",codejson)
                    .add("address",address)
                    .add("coordinate",coordinate)
                    .build();

            Request request=new Request.Builder().post(requestBody).url(code.getLink()).build();
            try {
                Response response=client.newCall(request).execute();
                System.out.println(response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}


