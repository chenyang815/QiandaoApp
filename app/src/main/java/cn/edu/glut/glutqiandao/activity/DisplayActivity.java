package cn.edu.glut.glutqiandao.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.allen.library.SuperButton;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.glut.glutqiandao.R;


//条件查询考勤或答题数据
public class DisplayActivity extends AppCompatActivity {

    @BindView(R.id.select) Spinner selectSpinner;
    @BindView(R.id.timebutton) SuperButton timeBtn;
    @BindView(R.id.items_list_view) ListView itemsListView;
    @BindView(R.id.itemframe) PtrClassicFrameLayout ptrClassicFrameLayout;
    @BindView(R.id.querybtn) SuperButton queryBtn;
    private ArrayAdapter<String> arr_adapter;
    List<String> datalist;
    Handler handler = new Handler();
    int page = 0;
    //选中需要查询的
    int type=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        ButterKnife.bind(this);


        datalist=new ArrayList<>();
        datalist.add("签到");
        datalist.add("答题");
        arr_adapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,datalist);
        //selectSpinner.setSelection(0);
        selectSpinner.setAdapter(arr_adapter);

        selectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出日历控件
                Calendar ca = Calendar.getInstance();
                int mYear = ca.get(Calendar.YEAR);
                int  mMonth = ca.get(Calendar.MONTH);
                int  mDay = ca.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=DatePickerDialog.newInstance(monDateSetListener,mYear,mMonth,mDay);
                datePickerDialog.setStartTitle("开始日期");
                datePickerDialog.setEndTitle("结束日期");
                datePickerDialog.show(getFragmentManager(),"Datepickerdialog");

            }
        });

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        ptrClassicFrameLayout.refreshComplete();

                        if (!ptrClassicFrameLayout.isLoadMoreEnable()) {
                            ptrClassicFrameLayout.setLoadMoreEnable(true);
                        }
                    }
                }, 1500);
            }
        });

//上划加载更多
        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        //mData.add(new String("  ListView item  - add " + page));//mAdapter.notifyDataSetChanged();
                        ptrClassicFrameLayout.loadMoreComplete(true);
                        page++;
                        //Toast.makeText(ListViewActivity.this, "load more complete", Toast.LENGTH_SHORT)
                              //  .show();

                        if (page == 1) {
                            //set load more disable
//                            ptrClassicFrameLayout.setLoadMoreEnable(false);
                        }
                    }
                }, 1000);
            }
        });
    }

//日期选择接口
    DatePickerDialog.OnDateSetListener monDateSetListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {

        }
    } ;

}
