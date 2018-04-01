package com.example.imagerecognition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.List;

import Util.HttpCallback;
import adapter.ResultAdapter;
import adapter.TestAdapter;
import model.ResultItem;
import model.Test;

public class ResultActivity extends AppCompatActivity implements HttpCallback {

    private TextView result_paper_type;
    private TextView result_name;
    private TextView result_num;

    private ListView result_list;

    private Button result_return;

    Test test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        GetIntent();
        init();
        initEvent();
        setData();
    }

    void GetIntent() {
        Intent i = getIntent();
        test = (Test) i.getSerializableExtra("test");
    }

    void init() {
        result_paper_type = (TextView) findViewById(R.id.result_paper_type);
        result_name = (TextView) findViewById(R.id.result_name);
        result_num = (TextView) findViewById(R.id.result_num);

        result_list = (ListView) findViewById(R.id.result_list);

        result_return = (Button) findViewById(R.id.result_return);

    }

    void initEvent() {
        result_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageFinish();
            }
        });
    }

    void PageFinish() {
        this.finish();
    }

    void setData() {
        result_paper_type.setText(test.getPaper_num());
        result_name.setText(test.getStudent_name());
        result_num.setText(test.getStudent_num());

        String result = test.getCheck_result();
        System.out.print(result);
        if (result == "") {

        } else {
            JsonArray array = new JsonParser().parse(result).getAsJsonArray();
            List list = ArratToList(array);
            LayoutInflater inflater = getLayoutInflater();
            ResultAdapter adapter = new ResultAdapter(inflater, list);
            result_list.setAdapter(adapter);
        }
    }

    List ArratToList(JsonArray jsonArray) {
        List<ResultItem> list = new ArrayList<>();
        Gson gson = new Gson();
        for (JsonElement i : jsonArray) {
            //Toast.makeText(this, i.getAsString(), Toast.LENGTH_SHORT).show();
            list.add(gson.fromJson(i, ResultItem.class));
        }
        return list;
    }

    @Override
    public void onSuccess(String result) {

    }

    @Override
    public void onError(Throwable ex) {

    }

    @Override
    public void onCancelled(Callback.CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}
