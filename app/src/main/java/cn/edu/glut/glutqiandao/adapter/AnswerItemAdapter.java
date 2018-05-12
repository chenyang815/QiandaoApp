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
import cn.edu.glut.glutqiandao.model.AnswerItem;

public class AnswerItemAdapter extends ArrayAdapter<AnswerItem> {

    private int resid;
    public AnswerItemAdapter(@NonNull Context context, int resource, @NonNull List<AnswerItem> objects) {
        super(context, resource, objects);
        resid=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AnswerItem answerItem=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resid,parent,false);
        TextView cnametx=view.findViewById(R.id.cnametext);
        TextView title=view.findViewById(R.id.title);
        TextView grade=view.findViewById(R.id.grade);
        cnametx.setText(answerItem.getCname());
        title.setText(answerItem.getTitle());
        grade.setText(answerItem.getGrade());
        return view;
    }
}
