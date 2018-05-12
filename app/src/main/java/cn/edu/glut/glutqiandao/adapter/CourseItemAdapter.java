package cn.edu.glut.glutqiandao.adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.allen.library.SuperTextView;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;

import cn.edu.glut.glutqiandao.R;
import cn.edu.glut.glutqiandao.activity.DisplayActivity;
import cn.edu.glut.glutqiandao.model.CourseItem;

public class CourseItemAdapter extends ArrayAdapter<CourseItem> {
    private int resid;
    private Context context;
    private FragmentManager fragmentManager;
    public CourseItemAdapter(FragmentManager fragmentManager, @NonNull Context context, int resource, @NonNull List<CourseItem> objects) {
        super(context, resource, objects);
        resid=resource;
        this.context=context;
        this.fragmentManager=fragmentManager;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final CourseItem courseItem=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resid,parent,false);
        SuperTextView superTextView=view.findViewById(R.id.cname);
        superTextView.setLeftString(courseItem.getCname());
        superTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DisplayActivity.class);
                intent.putExtra("cid",courseItem.getCid());
                context.startActivity(intent);
            }
        });
        return view;
    }



}
