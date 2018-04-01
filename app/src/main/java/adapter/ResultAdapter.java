package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.imagerecognition.R;

import java.util.List;

import model.ResultItem;
import model.Test;

/**
 * Created by Administrator on 2018/3/20.
 */

public class ResultAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<ResultItem> mDatas;

    public ResultAdapter(LayoutInflater mInflater, List<ResultItem> mDatas) {
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
        View view=mInflater.inflate(R.layout.result_list,null);
        ResultItem item = this.mDatas.get(position);
        //获得自定义布局中每一个控件的对象
        TextView result_item_item = (TextView) view.findViewById(R.id.result_item_item);
        TextView result_item_isTrue = (TextView) view.findViewById(R.id.result_item_isTrue);
        //将数据一一添加到自定义的布局中。
        result_item_item.setText(item.getItem());
        result_item_isTrue.setText(item.isTrue()?"√":"×");
        return view ;
    }
}
