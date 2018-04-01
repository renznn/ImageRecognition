package com.example.imagerecognition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import Util.HttpCallback;
import Util.URL;
import model.LoginStatus;
import model.Teacher;

public class LoginActivity extends AppCompatActivity implements HttpCallback {
    private Button Login;
    private EditText UserName;
    private EditText UserPassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        x.Ext.init(this.getApplication());//注册xutils
        x.Ext.setDebug(true);
        init();
        initEvent();
    }

    void init() {
        Login = (Button) findViewById(R.id.login);
        UserName = (EditText) findViewById(R.id.username);
        UserPassWord = (EditText) findViewById(R.id.userpsd);
    }

    void initEvent() {
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = UserName.getText().toString();
                String password = UserPassWord.getText().toString();
                CheckLogin(username, password);
            }
        });
    }

    void CheckLogin(String username, String password) {
        try {
            RequestParams requestParams = new RequestParams(URL.CheckLoginUrl);
            requestParams.addBodyParameter("num", username);
            requestParams.addBodyParameter("pwd", password);
            URL.PostData(requestParams, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(String result) {
        //Toast.makeText(this,result.toString(),Toast.LENGTH_SHORT).show();
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
        String code = jsonObject.get("code").getAsString();
        System.out.println(code);
        if ("200".equals(code)) {
            JsonObject t=jsonObject.get("data").getAsJsonObject();
            Teacher teacher=gson.fromJson(t,Teacher.class);
            new LoginStatus(teacher);
            System.out.println(LoginStatus.getTeacher().getName());
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        } else if ("400".equals(code)) {
            Toast.makeText(this, "账号密码错误", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(Throwable ex) {
        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelled(Callback.CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}
