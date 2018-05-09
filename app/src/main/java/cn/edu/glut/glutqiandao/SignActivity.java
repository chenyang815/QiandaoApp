package cn.edu.glut.glutqiandao;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.allen.library.SuperButton;
import com.allen.library.SuperTextView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;


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



public class SignActivity extends AppCompatActivity {

    SuperTextView teacher_textView, course_textView, date_textView,
            class_textView, section_textView,address_textView,coordinate_TextView,isfingeridfiy_TextView;
    SuperButton b1,getLocationBt;
    private static Handler handler = new Handler();
    private Code code;
    private String codejson;
    private String address;
    private String coordinate;
    private String imei;

    private FingerprintIdentify mFingerprintIdentify;
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
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

        teacher_textView = (SuperTextView) findViewById(R.id.tname);
        course_textView = (SuperTextView) findViewById(R.id.cname);
        date_textView= (SuperTextView) findViewById(R.id.date);
        class_textView = (SuperTextView) findViewById(R.id.classroom);
        address_textView = (SuperTextView) findViewById(R.id.address);
        section_textView = (SuperTextView) findViewById(R.id.section);
        coordinate_TextView = (SuperTextView) findViewById(R.id.coordinate);
        isfingeridfiy_TextView = (SuperTextView) findViewById(R.id.isfingeridfiy);

        b1 = (SuperButton) findViewById(R.id.buttonqq);

        code.setSid(Util.getSid());

       codejson=gson.toJson(code);

       // System.out.println(code.getSection());
        mFingerprintIdentify = new FingerprintIdentify(getApplicationContext(), new BaseFingerprint.FingerprintIdentifyExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {
               // append("\nException：" + exception.getLocalizedMessage());
            }
        });

        //是否支持指纹
        if (mFingerprintIdentify.isFingerprintEnable()){
            isfingeridfiy_TextView.setCenterString("支持");
        }else {
            isfingeridfiy_TextView.setCenterString("不支持");
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("坐标是"+coordinate);
                if (!coordinate.equals("0.0-0.0")){
                     if (mFingerprintIdentify.isFingerprintEnable()) {
                         Toast.makeText(SignActivity.this, "开始指纹验证", Toast.LENGTH_SHORT).show();
                         View view = getLayoutInflater().inflate(R.layout.identifylayout, null);
                         final AlertDialog dialog = new AlertDialog.Builder(SignActivity.this)
                                 .setTitle("指纹验证")
                                 .setView(view)
                                 .create();
                         dialog.show();
                         mFingerprintIdentify.startIdentify(3, new BaseFingerprint.FingerprintIdentifyListener() {
                             @Override
                             public void onSucceed() {
                                 Toast.makeText(SignActivity.this, "指纹验证成功", Toast.LENGTH_SHORT).show();
                                 dialog.dismiss();
                                 new Thread(new SignThread()).start();
                             }

                             @Override
                             public void onNotMatch(int availableTimes) {
                                 Toast.makeText(SignActivity.this, "指纹不匹配,还剩" + availableTimes + "次", Toast.LENGTH_SHORT).show();
                             }

                             @Override
                             public void onFailed(boolean isDeviceLocked) {
                                 Toast.makeText(SignActivity.this, "指纹匹配失败,无法签到", Toast.LENGTH_SHORT).show();
                             }

                             @Override
                             public void onStartFailedByDeviceLocked() {

                             }
                         });
                     }
                     else {
                         new Thread(new SignThread()).start();
                     }
                }

                else {
                    Toast.makeText(SignActivity.this, "坐标数据为空,定位失败,请重新定位", Toast.LENGTH_LONG).show();

                }

                //new Thread(new SignThread()).start();

            }
        });
        getLocationBt = (SuperButton) findViewById(R.id.get_location);

        getLocationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignActivity.this,"正在重新定位......",Toast.LENGTH_SHORT).show();
                requestLocation();
            }
        });

        teacher_textView.setCenterString(code.getTname());
        course_textView.setCenterString(code.getCname());
        date_textView.setCenterString(code.getSigndate());
        class_textView.setCenterString(code.getClassroom());
        section_textView.setCenterString(code.getSection()+"节");

        mLocationClient = new AMapLocationClient(getApplicationContext());

        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);





        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(SignActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(SignActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(SignActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(SignActivity.this, permissions, 1);
        } else {
            if(null != mLocationClient){
                mLocationClient.setLocationOption(mLocationOption);
                //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
                mLocationClient.stopLocation();
                mLocationClient.startLocation();
                Toast.makeText(SignActivity.this,"正在定位......",Toast.LENGTH_SHORT).show();

            }
        }

    }

    private void requestLocation() {

        mLocationClient.stopLocation();
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

            if (aMapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。

                String locationStr=aMapLocation.getAddress();

                //locationStr.append("精度信息：").append(aMapLocation.getAccuracy()).append("\n");
                System.out.println("正在定位......."+locationStr);
                System.out.println(locationStr);
                address_textView.setCenterString(locationStr);
                address=locationStr;
                coordinate=aMapLocation.getLongitude()+"-"+aMapLocation.getLatitude();
                String showcoordinate="经度："+aMapLocation.getLongitude()+" 纬度："+aMapLocation.getLatitude();
                coordinate_TextView.setCenterString(showcoordinate);
                Toast.makeText(SignActivity.this,"定位成功",Toast.LENGTH_SHORT).show();

            }else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }

            //positonTextView.setText(locationStr);

        }
    };

    private void resetOption() {
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
    }

   public class SignThread extends Thread implements Runnable{
       String msg="";
        @Override
        public void run() {

            OkHttpClient client=new OkHttpClient();
            RequestBody requestBody =new FormBody.Builder()
                    .add("codejson",codejson)
                    .add("address",address)
                    .add("coordinate",coordinate)
                    .add("imei",imei)
                    .build();

            Request request=new Request.Builder().post(requestBody).url(code.getLink()).build();
            try {
                Response response=client.newCall(request).execute();
                String responsestr=response.body().string();
                System.out.println(responsestr);
                ReadContext context = JsonPath.parse(responsestr);
                msg =context.read("$.msg");

            } catch (IOException e) {
                e.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SignActivity.this,msg,Toast.LENGTH_LONG).show();
                }
            });

        }
    }

}


