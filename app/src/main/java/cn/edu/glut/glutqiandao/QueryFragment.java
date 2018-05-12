package cn.edu.glut.glutqiandao;



import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ListView;

import com.allen.library.SuperTextView;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;

import java.util.Calendar;
import java.util.List;

import cn.edu.glut.glutqiandao.adapter.AnswerItemAdapter;
import cn.edu.glut.glutqiandao.adapter.AttendanceItemAdapter;
import cn.edu.glut.glutqiandao.adapter.CourseItemAdapter;
import cn.edu.glut.glutqiandao.model.AnswerItem;
import cn.edu.glut.glutqiandao.model.AttendanceItem;
import cn.edu.glut.glutqiandao.model.CourseItem;
import cn.edu.glut.glutqiandao.thread.GetItemsThread;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QueryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QueryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QueryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    static String link1="http://192.168.31.135:8080/getTodayAttendaceItems";
    static String link2="http://192.168.31.135:8080/getTodayAnswerItems";
    static String link3="http://192.168.31.135:8080/getCourseItemsByStu";
    PtrClassicFrameLayout ptrClassicFrameLayout;
    private OnFragmentInteractionListener mListener;
    private AttendanceItemAdapter attendanceItemAdapter;
    private AnswerItemAdapter answerItemAdapter;
    private CourseItemAdapter courseItemAdapter;

    private ListView attendacelistview;
    private ListView answerlistview;
    private ListView courselistview;
    private List<AttendanceItem> attendanceItems;
    private List<AnswerItem> answerItems;
    private List<CourseItem> courseItems;
    Handler handler=new Handler();
    int mYear, mMonth, mDay;
    private SuperTextView todayatendacetx;
    final int DATE_DIALOG_ID=1;
    public QueryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QueryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QueryFragment newInstance(String param1, String param2) {
        QueryFragment fragment = new QueryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_query, container, false);
        ptrClassicFrameLayout=view.findViewById(R.id.test_list_view_frame);
        attendacelistview=view.findViewById(R.id.attdancelistview);
        answerlistview=view.findViewById(R.id.answerlistview);
        courselistview=view.findViewById(R.id.couselistview);
        try {

            attendanceItems= (List<AttendanceItem>)getItemList(link1,0);
            answerItems=(List<AnswerItem>) getItemList(link2,1);
            courseItems= (List<CourseItem>) getItemList(link3,2);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        attendanceItemAdapter=new AttendanceItemAdapter(getActivity(),R.layout.attendance_item,attendanceItems);
        answerItemAdapter=new AnswerItemAdapter(getActivity(),R.layout.answer_item,answerItems);
        FragmentManager fragmentManager= getActivity().getFragmentManager();
        courseItemAdapter=new CourseItemAdapter(fragmentManager,getActivity(),R.layout.couse_item,courseItems);

        attendacelistview.setAdapter(attendanceItemAdapter);
        answerlistview.setAdapter(answerItemAdapter);
        courselistview.setAdapter(courseItemAdapter);


        courselistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {


            }
        });


        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);



        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        attendanceItems.clear();
                        answerItems.clear();
                        courseItems.clear();
                        try {
                            //type 0为考勤数据，1为答题数据
                            attendanceItems.addAll((List<AttendanceItem>)getItemList(link1,0));
                            answerItems.addAll((List<AnswerItem>)getItemList(link2,1));
                            courseItems.addAll((List<CourseItem>)getItemList(link3,2));

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        attendanceItemAdapter.notifyDataSetChanged();
                        answerItemAdapter.notifyDataSetChanged();
                        courseItemAdapter.notifyDataSetChanged();
                        ptrClassicFrameLayout.refreshComplete();

                        if (!ptrClassicFrameLayout.isLoadMoreEnable()) {
                            ptrClassicFrameLayout.setLoadMoreEnable(false);
                        }
                    }
                }, 1500);
            }
        });
        return view;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private  List<?> getItemList(String link,int type) throws InterruptedException {
        GetItemsThread getItemsThread=new GetItemsThread(link,type);
        getItemsThread.start();
        getItemsThread.join();
        if (type==0){
            return getItemsThread.attendanceItems;
        }
        else if (type==1){
            return getItemsThread.answerItems;
        }else  {
            return getItemsThread.courseItems;
        }

    }
}


