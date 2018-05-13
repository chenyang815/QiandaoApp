package cn.edu.glut.glutqiandao;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import cn.edu.glut.glutqiandao.questionnaire.AnswerQuestionnaireActivity;
import cn.edu.glut.glutqiandao.quiz.AnswerQuizActivity;

public class MainActivity extends AppCompatActivity implements QianDaoFragment.OnFragmentInteractionListener,
        QueryFragment.OnFragmentInteractionListener,ProfileFragment.OnFragmentInteractionListener,
        ViewPager.OnPageChangeListener {


    public static final int REQUEST_CODE = 111;
    private QianDaoFragment qianDaoFragment;
    private QueryFragment queryFragment;
    private ProfileFragment profileFragment;

    private ViewPager viewPager;
    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            viewPager.setCurrentItem(item.getOrder());

            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        qianDaoFragment=new QianDaoFragment();
        queryFragment=new QueryFragment();
        profileFragment=new ProfileFragment();


        viewPager= (ViewPager) findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return qianDaoFragment;
                    case 1:
                        return queryFragment;
                    case 2:
                        return profileFragment;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });




    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //Toast.makeText(this,"test", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        navigation.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


    if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    //Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();//显示解析
                    JsonParser parser=new JsonParser();
                    JsonElement element=parser.parse(result);
                    JsonObject jsonObj = element.getAsJsonObject();
                    if (jsonObj.get("type")!=null){


                    String type=jsonObj.get("type").getAsString();
                    //0为考勤,1为答题,2为问卷
                    if (type.equals("0")){
                        Intent intent = new Intent(MainActivity.this, SignActivity.class);
                        intent.putExtra("data", result);
                        startActivity(intent);

                    }else if (type.equals("1")){
                        Intent intent = new Intent(MainActivity.this, AnswerQuizActivity.class);
                        intent.putExtra("data", result);
                        startActivity(intent);

                    }

                    else if(type.equals("2")){

                        Intent intent = new Intent(MainActivity.this, AnswerQuestionnaireActivity.class);
                        intent.putExtra("data", result);
                        startActivity(intent);
                    }
                    }
                    else {
                        //跳转显示内容的Activity
                    }





                } else if
                        (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
