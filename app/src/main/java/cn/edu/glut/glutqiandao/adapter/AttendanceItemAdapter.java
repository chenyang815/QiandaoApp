package cn.edu.glut.glutqiandao.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cn.edu.glut.glutqiandao.R;
import cn.edu.glut.glutqiandao.model.AttendanceItem;

public class AttendanceItemAdapter extends ArrayAdapter<AttendanceItem> {

    private int resid;
    public AttendanceItemAdapter(@NonNull Context context, int resource, @NonNull List<AttendanceItem> objects) {
        super(context, resource, objects);
        resid=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AttendanceItem attendanceItem=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resid,parent,false);
        TextView cnametx=view.findViewById(R.id.cnametx);
        TextView sectiontx=view.findViewById(R.id.sectiontx);
        TextView statustx=view.findViewById(R.id.statustx);
        cnametx.setText(attendanceItem.getCname());
        sectiontx.setText(attendanceItem.getSection());
        statustx.setText(attendanceItem.getStatus());
        return view;
    }
}
