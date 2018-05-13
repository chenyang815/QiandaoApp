package cn.edu.glut.glutqiandao.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperButton;
import com.allen.library.SuperTextView;
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
import cn.edu.glut.glutqiandao.adapter.AnswerItemAdapter;
import cn.edu.glut.glutqiandao.adapter.AttendanceItemAdapter;
import cn.edu.glut.glutqiandao.adapter.CourseItemAdapter;
import cn.edu.glut.glutqiandao.model.AnswerItem;
import cn.edu.glut.glutqiandao.model.AttendanceItem;
import cn.edu.glut.glutqiandao.thread.GetItemsDataThread;


//条件查询考勤或答题数据
public class DisplayActivity extends AppCompatActivity {

    @BindView(R.id.select) Spinner selectSpinner;
    @BindView(R.id.timebutton) SuperButton timeBtn;
    @BindView(R.id.items_list_view) ListView itemsListView;
    @BindView(R.id.itemframe) PtrClassicFrameLayout ptrClassicFrameLayout;
    @BindView(R.id.querybtn) SuperButton queryBtn;
    @BindView(R.id.timesection) SuperTextView timesectionTexView;
    @BindView(R.id.center_textview) TextView center_textview;
    @BindView(R.id.right_textview) TextView right_textview;

    private ArrayAdapter<String> arr_adapter;
    private List<String> datalist;
    private String cid;
    Handler handler = new Handler();
    int page = 1;
    //选中需要查询的,默认查询签到
    int type=0;
    private String datesection;
    //每次展示数量
    private int limit=20;
    static String link="http://tomcat.ishare20.cn:8080/getItemsData";

    private List<AttendanceItem> attendanceItems;
    private List<AnswerItem> answerItems;
    private AttendanceItemAdapter attendanceItemAdapter;
    private AnswerItemAdapter answerItemAdapter;

    public int totalAnswerItem;
    public int totalAttendanceItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        ButterKnife.bind(this);

        final Intent intent=getIntent();
        cid=intent.getStringExtra("cid");

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

//初始化，显示第一页数据

        Toast.makeText(DisplayActivity.this,"上划加载更多数据",Toast.LENGTH_SHORT).show();

        attendanceItems= (List<AttendanceItem>) getdatas(cid,datesection,1,limit,type,link);

        attendanceItemAdapter=new AttendanceItemAdapter(this,R.layout.attendance_item,attendanceItems);
        itemsListView.setAdapter(attendanceItemAdapter);


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

                        page++;
                        List<?> itemdatas=getdatas(cid,datesection,page,limit,type,link);
                        System.out.println("page is:"+page);

                        int pagesize;
                        if (type==0){
                            if (itemdatas!=null){
                                List<AttendanceItem> oldlist=new ArrayList<>();
                                oldlist.addAll(attendanceItems);
                                attendanceItems.clear();
                                attendanceItems.addAll(oldlist);
                                attendanceItems.addAll((List<AttendanceItem>)itemdatas);
                                System.out.println(attendanceItems.size());
                                attendanceItemAdapter.notifyDataSetChanged();
                            }


                            if (totalAttendanceItem%limit==0){
                                pagesize=totalAttendanceItem/limit;
                            }else {
                                pagesize=totalAttendanceItem/limit+1;
                            }

                            if (page==pagesize) {
                                //set load more disable
                                ptrClassicFrameLayout.setLoadMoreEnable(false);
                                ptrClassicFrameLayout.loadMoreComplete(true);
                            }


                        }else {

                            if (itemdatas!=null){
                                List<AnswerItem> oldlist=new ArrayList<>();
                                oldlist.addAll(answerItems);
                                answerItems.clear();
                                answerItems.addAll(oldlist);
                                answerItems.addAll((List<AnswerItem>)itemdatas);
                                System.out.println(answerItems.size());
                                answerItemAdapter.notifyDataSetChanged();
                            }


                            if (totalAnswerItem%limit==0){
                                pagesize=totalAnswerItem/limit;
                            }else {
                                pagesize=totalAnswerItem/limit+1;
                            }
                            if (page==pagesize) {
                                //set load more disable

                                ptrClassicFrameLayout.setLoadMoreEnable(false);
                                ptrClassicFrameLayout.loadMoreComplete(true);

                            }
                        }


                        ptrClassicFrameLayout.loadMoreComplete(true);

                    }
                }, 1000);
            }
        });


        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (type==0){
                    center_textview.setText("节数");
                    right_textview.setText("状态");

                }
                else {
                    center_textview.setText("题目");
                    right_textview.setText("分数");

                }

                page=1;
                if (!ptrClassicFrameLayout.isLoadMoreEnable()) {
                    ptrClassicFrameLayout.setLoadMoreEnable(true);
                }

                if (type==1){
                    if (answerItems!=null){
                        answerItems.clear();
                        answerItems.addAll((List<AnswerItem>) getdatas(cid,datesection,1,limit,type,link));
                    }else {
                        answerItems=(List<AnswerItem>) getdatas(cid,datesection,1,limit,type,link);
                    }


                    System.out.println("totalAnswerItem is"+totalAnswerItem);
                    //System.out.println("ptrClassicFrameLayout.isLoadMoreEnable()"+ptrClassicFrameLayout.isLoadMoreEnable());


                    if (answerItemAdapter==null){
                        answerItemAdapter=new AnswerItemAdapter(DisplayActivity.this,R.layout.answer_item,answerItems);
                        itemsListView.setAdapter(answerItemAdapter);
                    }else {

                        itemsListView.setAdapter(answerItemAdapter);
                        answerItemAdapter.notifyDataSetChanged();
                    }



                }
                else {
                    List<AttendanceItem> attendanceItems1= (List<AttendanceItem>) getdatas(cid,datesection,1,limit,type,link);

                    attendanceItems.clear();
                    attendanceItems.addAll(attendanceItems1);
                    itemsListView.setAdapter(attendanceItemAdapter);
                    attendanceItemAdapter.notifyDataSetChanged();

                }
                ptrClassicFrameLayout.loadMoreComplete(true);

            }
        });
    }

//日期选择接口
    DatePickerDialog.OnDateSetListener monDateSetListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
            monthOfYear+=1;
            monthOfYearEnd+=1;
            datesection=year+"-"+monthOfYear+"-"+dayOfMonth+"~"+yearEnd+"-"+monthOfYearEnd+"-"+dayOfMonthEnd;
            timesectionTexView.setCenterString(datesection);
        }
    } ;

    private List<?> getdatas(String cid, String datesection, int page, int limit, int type,String link){
        GetItemsDataThread getItemsDataThread=new GetItemsDataThread(cid,datesection,page,limit,type,link);
        getItemsDataThread.start();

        try {
            getItemsDataThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (type==0){
            totalAttendanceItem=getItemsDataThread.totalAttendanceItem;

        }else {
            totalAnswerItem=getItemsDataThread.totalAnswerItem;
        }

        return getItemsDataThread.itemdatas;
    }

}
