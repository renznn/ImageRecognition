package com.example.imagerecognition;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import Util.HttpCallback;
import Util.URL;
import model.LoginStatus;
import model.Teacher;

public class UserActivity extends AppCompatActivity implements HttpCallback {

    private Button ChangeInfo;
    private Button PrePanel;

    private EditText UserName;
    private EditText UserAge;
    private EditText UserClass;
    private EditText UserGrade;
    private EditText UserNum;
    private EditText UserSubject;
    private EditText UserPwd;

    private RadioGroup Sex;
    private RadioButton SexMale;
    private RadioButton SexFemale;

    private boolean IsChangeStatus = false;

    private Teacher teacherCache;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        init();
        initAction();
    }

    void init() {
        ChangeInfo = (Button) findViewById(R.id.change_info);
        PrePanel = (Button) findViewById(R.id.pre_panel);

        UserName = (EditText) findViewById(R.id.user_name);
        UserAge = (EditText) findViewById(R.id.user_age);
        UserClass = (EditText) findViewById(R.id.user_class);
        UserGrade = (EditText) findViewById(R.id.user_grade);
        UserNum = (EditText) findViewById(R.id.user_num);
        UserSubject = (EditText) findViewById(R.id.user_subject);
        UserPwd = (EditText) findViewById(R.id.user_pwd);

        Sex = (RadioGroup) findViewById(R.id.user_sex);

        SexMale = (RadioButton) findViewById(R.id.sex_male);
        SexFemale = (RadioButton) findViewById(R.id.sex_female);
        teacherCache=LoginStatus.getTeacher();
        setInfo();
        OutChangeStatus();
    }

    void initAction() {
        ChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeStatus();
            }
        });
        PrePanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Finish();
            }
        });
    }

    void ChangeStatus() {
        if (!IsChangeStatus) {
            InChangeStatus();
        } else {
            UpdateInfo();
            OutChangeStatus();
        }
    }

    void Finish() {
        if (IsChangeStatus) {
            setInfo();
            OutChangeStatus();
        } else {
            this.finish();
        }
    }

    void InChangeStatus() {
        UserName.setEnabled(true);
        UserAge.setEnabled(true);
        UserClass.setEnabled(true);
        UserGrade.setEnabled(true);
        UserNum.setEnabled(true);
        UserSubject.setEnabled(true);
        UserPwd.setEnabled(true);

        UserName.setFocusableInTouchMode(true);
        UserName.setFocusable(true);
        UserName.setCursorVisible(true);
        UserName.requestFocus();

        UserAge.setFocusableInTouchMode(true);
        UserAge.setFocusable(true);
        UserAge.setCursorVisible(true);
        UserAge.requestFocus();

        UserClass.setFocusableInTouchMode(true);
        UserClass.setFocusable(true);
        UserClass.setCursorVisible(true);
        UserClass.requestFocus();

        UserGrade.setFocusableInTouchMode(true);
        UserGrade.setFocusable(true);
        UserGrade.setCursorVisible(true);
        UserGrade.requestFocus();

        UserNum.setFocusableInTouchMode(true);
        UserNum.setFocusable(true);
        UserNum.setCursorVisible(true);
        UserNum.requestFocus();

        UserSubject.setFocusableInTouchMode(true);
        UserSubject.setFocusable(true);
        UserSubject.setCursorVisible(true);
        UserSubject.requestFocus();

        UserPwd.setFocusableInTouchMode(true);
        UserPwd.setFocusable(true);
        UserPwd.setCursorVisible(true);
        UserPwd.requestFocus();

        SexMale.setEnabled(true);
        SexFemale.setEnabled(true);

        ChangeInfo.setText("保存");
        PrePanel.setText("取消");
        IsChangeStatus = true;
    }

    void OutChangeStatus() {
        //UserName.setEnabled(false);
        //UserAge.setEnabled(false);
        //UserClass.setEnabled(false);
        //UserGrade.setEnabled(false);
        //UserNum.setEnabled(false);
        //UserSubject.setEnabled(false);
        //UserPwd.setEnabled(false);

        UserName.setFocusable(false);
        UserName.setFocusableInTouchMode(false);

        UserAge.setFocusable(false);
        UserAge.setFocusableInTouchMode(false);

        UserClass.setFocusable(false);
        UserClass.setFocusableInTouchMode(false);

        UserGrade.setFocusable(false);
        UserGrade.setFocusableInTouchMode(false);

        UserNum.setFocusable(false);
        UserNum.setFocusableInTouchMode(false);

        UserSubject.setFocusable(false);
        UserSubject.setFocusableInTouchMode(false);

        UserPwd.setFocusable(false);
        UserPwd.setFocusableInTouchMode(false);

        SexMale.setEnabled(false);
        SexFemale.setEnabled(false);


        ChangeInfo.setText("修改信息");
        PrePanel.setText("返回");
        IsChangeStatus = false;
    }

    public void setInfo(){
        Teacher teacher = LoginStatus.getTeacher();
        UserName.setText(teacher.getName());
        UserAge.setText(teacher.getAge() + "");
        UserClass.setText(teacher.getClasses());
        UserGrade.setText(teacher.getGrade());
        UserSubject.setText(teacher.getSubject());
        UserPwd.setText(teacher.getPwd());
        if (teacher.isSex()) {
            SexFemale.setChecked(true);
        } else {
            SexMale.setChecked(true);
        }
    }

    public void UpdateInfo(){
        Teacher teacher = LoginStatus.getTeacher();
        teacher.setId(teacher.getId());
        teacher.setName(UserName.getText().toString());
        teacher.setAge(Integer.parseInt( UserAge.getText().toString()));
        teacher.setClasses(UserClass.getText().toString());
        teacher.setGrade(UserGrade.getText().toString());
        teacher.setSubject(UserSubject.getText().toString());
        teacher.setNum(UserNum.getText().toString());
        teacher.setPwd(UserPwd.getText().toString());
        if(SexFemale.isChecked()){
            teacher.setSex(true);
        }else if(SexMale.isChecked()){
            teacher.setSex(false);
        }
        LoginStatus.setTeacher(teacher);

        RequestParams requestParams=new RequestParams(URL.UpdateTeacherInfoUrl);
        requestParams.addBodyParameter("id",teacher.getId()+"");
        requestParams.addBodyParameter("name",teacher.getName());
        requestParams.addBodyParameter("sex",teacher.isSex()+"");
        requestParams.addBodyParameter("age",teacher.getAge()+"");
        requestParams.addBodyParameter("classes",teacher.getClasses()+"");
        requestParams.addBodyParameter("grade",teacher.getGrade()+"");
        requestParams.addBodyParameter("num",teacher.getNum()+"");
        requestParams.addBodyParameter("subject",teacher.getSubject()+"");
        requestParams.addBodyParameter("pwd",teacher.getPwd()+"");
        URL.PostData(requestParams,this);
    }

    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
        String code = jsonObject.get("code").getAsString();
        System.out.println(code);
        switch (code){
            case "200":
                Toast.makeText(this,"修改成功", Toast.LENGTH_SHORT).show();
                teacherCache=LoginStatus.getTeacher();
                break;
            case "400":
                Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
                LoginStatus.setTeacher(teacherCache);
                break;
        }
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
