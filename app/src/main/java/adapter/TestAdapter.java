package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.imagerecognition.R;

import java.util.List;

import model.Test;

/**
 * Created by Administrator on 2018/3/20.
 */

public class TestAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Test> mDatas;

    public TestAdapter(LayoutInflater mInflater, List<Test> mDatas) {
        this.mInflater = mInflater;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return this.mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=mInflater.inflate(R.layout.list,null);
        Test student = this.mDatas.get(position);
        //获得自定义布局中每一个控件的对象
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView subject = (TextView) view.findViewById(R.id.subject);
        TextView score = (TextView) view.findViewById(R.id.score);
        TextView paper_type=(TextView) view.findViewById(R.id.list_paper_type);
        //将数据一一添加到自定义的布局中。
        name.setText(student.getStudent_name());
        subject.setText(student.getSubject());
        score.setText(student.getScore()+"");
        paper_type.setText(student.getPaper_num());

        return view ;
    }
}
