package cn.edu.glut.glutqiandao.location;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import cn.edu.glut.glutqiandao.R;

public class LocationTestActivity extends AppCompatActivity {

    private TextView positonTextView;
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_test);
        positonTextView= (TextView) findViewById(R.id.position_text);

        mLocationClient = new AMapLocationClient(getApplicationContext());

        mLocationClient.setLocationListener(mLocationListener);

        mLocationOption=new AMapLocationClientOption();

        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        if(null != mLocationClient){
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }

    }

    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            StringBuffer locationStr=new StringBuffer();
            locationStr.append("街道信息：").append(aMapLocation.getStreet()).append("\n");
            locationStr.append("精度信息：").append(aMapLocation.getAccuracy()).append("\n");
            locationStr.append("地址：").append(aMapLocation.getAddress()).append("\n");
            positonTextView.setText(locationStr);

        }
    };
}
