package com.example.imagerecognition;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import Util.HttpCallback;
import Util.URL;
import adapter.TestAdapter;
import model.LoginStatus;
import model.Student;
import model.Test;

import static Util.URL.GetGradeAndClassListUrl;


public class MainActivity extends AppCompatActivity implements HttpCallback {
    private Button TakePhoto;
    private Button Scanner;
    private ImageView photo;
    private Button Album;
    private Button User;
    private static final int IMAGE_CODE = 1;
    private Spinner TypeSpinner;
    private Spinner ItemSpinner;
    private Spinner PaperTypeSpinner;
    private ListView HistoryList;

    private String[] type = {"班级"};
    private String[] item = {"1班", "2班"};

    private EditText DialogName;
    private EditText DialogNum;
    private EditText DialogGrade;
    private EditText DialogClass;
    private EditText DialogPaper;

    private RelativeLayout avi_bg;

    private AVLoadingIndicatorView avi;

    JsonObject SpinnerData;

    int ChooseItem = -1;
    int ChooseType = -1;
    int ChoosePaper = 0;
    boolean IsFirstLoad = true;

    List<Test> testList = null;

    int Score = 80;
    Student studentData = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //x.view().inject(this);
        x.Ext.init(this.getApplication());
        x.Ext.setDebug(true);
        init();
        initEvent();
    }

    /*初始化*/
    void init() {
        TakePhoto = (Button) findViewById(R.id.take);
        Scanner = (Button) findViewById(R.id.scanner);
        //photo = (ImageView) findViewById(R.id.photo);
        Album = (Button) findViewById(R.id.album);
        User = (Button) findViewById(R.id.User);

        TypeSpinner = (Spinner) findViewById(R.id.TypeSpinner);
        ItemSpinner = (Spinner) findViewById(R.id.ItemSpinner);
        PaperTypeSpinner = (Spinner) findViewById(R.id.paper_type);

        HistoryList = (ListView) findViewById(R.id.HistoryList);


        avi_bg = (RelativeLayout) findViewById(R.id.avi_bg);

        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);

        GetGradeAndClassList();


    }

    //请求分类类型
    void GetGradeAndClassList() {
        RequestParams requestParams = new RequestParams(URL.GetGradeAndClassListUrl);
        URL.PostData(requestParams, this);
    }

    //设置第一个下拉框选项
    void setTypeSpinner() {
        JsonArray array = SpinnerData.get("grade").getAsJsonArray();
        List list = ArratToList(array);

        //适配器
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        //设置样式
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TypeSpinner.setAdapter(typeAdapter);
        TypeSpinner.setSelection(ChooseType);
    }

    //设置第二个下拉框选项
    void setItemSpinner() {
        JsonArray array = SpinnerData.get("classes").getAsJsonArray().get(ChooseType).getAsJsonArray();
        List list = ArratToList(array);
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ItemSpinner.setAdapter(itemAdapter);
        ItemSpinner.setSelection(ChooseItem);
    }

    void setPaperTypeSpinner() {
        JsonArray array = SpinnerData.get("paper_type").getAsJsonArray();
        List list = ArratToList(array);
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PaperTypeSpinner.setAdapter(itemAdapter);
        PaperTypeSpinner.setSelection(ChoosePaper);
    }

    //设置列表
    void setHistoryList() {
        LayoutInflater inflater = getLayoutInflater();
       /* List<Test> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Test test = new Test(i, "" + i, "" + i, "" + i, "" + i, "" + i, "" + i, 1, "" + i, "" + i);
            list.add(test);
        }*/
        TestAdapter adapter = new TestAdapter(inflater, testList);
        HistoryList.setAdapter(adapter);
    }

    //初始化事件
    void initEvent() {
        TakePhoto.setOnClickListener(new View.OnClickListener() {//打开相机
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(i);
            }
        });
        Album.setOnClickListener(new View.OnClickListener() {//打开相册
            @Override
            public void onClick(View v) {
                OpenAlbum();
            }
        });

        User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UserActivity.class);
                startActivity(i);
            }
        });

        TypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, position + "--" + id, Toast.LENGTH_SHORT).show();
                if (IsFirstLoad) {
                    IsFirstLoad = false;
                } else {
                    ChooseType = position;
                    ChooseItem = 0;
                    setItemSpinner();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ChooseItem = position;
                GetScoreList();
                //Toast.makeText(MainActivity.this, position + "--" + id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        PaperTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ChoosePaper=position;
                GetScoreList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        HistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, position + "--" + id, Toast.LENGTH_SHORT).show();
                toResult(position);
            }
        });


    }

    void toResult(int index){
        Test test =testList.get(index);
        if(test.getCheck_result()==""|| test.getCheck_result()==null){
            Toast.makeText(this,"暂无试卷信息",Toast.LENGTH_SHORT).show();
        }else {
            Intent i = new Intent(MainActivity.this, ResultActivity.class);
            i.putExtra("test", test);
            startActivity(i);
        }
    }

    //回去intent返回参数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            //Toast.makeText(this, imagePath, Toast.LENGTH_LONG);
            Log.e("图片", imagePath);
            c.close();
            avi_bg.setVisibility(View.VISIBLE);
            avi.show();
            UploadImage(imagePath);

        }
    }

    //打开相册
    public void OpenAlbum() {
        /*Intent i = new Intent(Intent.ACTION_PICK, null);
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image*//**//*");*/
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, IMAGE_CODE);
       /* RequestParams requestParams = new RequestParams(URL.test);
        URL.PostData(requestParams, this);*/
    }

    //上传图片
    public void UploadImage(String path) {
        //现在是单图片测试，以后要改成多图
        List<String> list = new ArrayList<>();
        list.add(path);
        RequestParams requestParams = new RequestParams(URL.UpLoadImageUrl);
        try {
            for (String p : list) {
                Bitmap bitmap = BitmapFactory.decodeFile(p);
                //photo.setImageBitmap(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] datas = baos.toByteArray();
                byte[] encode = Base64.encode(datas, Base64.DEFAULT);
                String photo = new String(encode);
                Log.e("图片", photo);
                requestParams.addBodyParameter("image", photo);
                requestParams.addBodyParameter("name", p.substring(p.lastIndexOf("/") + 1, p.lastIndexOf(".")));
                baos.flush();
                baos.close();
            }
            URL.PostData(requestParams, this);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "上传出错", Toast.LENGTH_SHORT).show();
        }

    }

    //jsonArray转换成list
    List ArratToList(JsonArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (JsonElement i : jsonArray) {
            //Toast.makeText(this, i.getAsString(), Toast.LENGTH_SHORT).show();
            list.add(i.getAsString());
        }
        return list;
    }

    //获取成绩列表
    void GetScoreList() {
        RequestParams requestParams = new RequestParams(URL.GetTestListByClassUrl);
        String grade = SpinnerData.get("grade").getAsJsonArray().get(ChooseType).getAsString();
        String classes = SpinnerData.get("classes").getAsJsonArray().get(ChooseType).getAsJsonArray().get(ChooseItem).getAsString();
        String paper_type=SpinnerData.get("paper_type").getAsJsonArray().get(ChoosePaper).getAsString();
        requestParams.addBodyParameter("grade", grade);
        requestParams.addBodyParameter("classes", classes);
        requestParams.addBodyParameter("paper_type",paper_type);
        URL.PostData(requestParams, this);
    }

    /*网络请求成功*/
    @Override
    public void onSuccess(String result) {
        //Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
        String code = jsonObject.get("code").getAsString();
        System.out.println(code);
        switch (code) {
            case "200":
                SpinnerData = jsonObject.get("data").getAsJsonObject();
                JsonArray array = SpinnerData.get("grade").getAsJsonArray();
                List list = ArratToList(array);
                //int i=0,j=0;
                for (int x = 0; x < list.size(); x++) {
                    if (list.get(x).toString().equals(LoginStatus.getTeacher().getGrade())) {
                        System.out.print(LoginStatus.getTeacher().getGrade() + x + "\n");
                        ChooseType = x;
                        break;
                    }
                }
                array = SpinnerData.get("classes").getAsJsonArray().get(ChooseType).getAsJsonArray();
                list = ArratToList(array);
                for (int x = 0; x < list.size(); x++) {
                    if (list.get(x).toString().equals(LoginStatus.getTeacher().getClasses())) {
                        System.out.print(LoginStatus.getTeacher().getClasses() + x + "\n");
                        ChooseItem = x;
                        break;
                    }
                }
                setTypeSpinner();
                setItemSpinner();
                setPaperTypeSpinner();
                GetScoreList();

                break;
            case "201":
                //Toast.makeText(this, jsonObject.get("data").getAsInt(), Toast.LENGTH_SHORT).show();
                avi.hide();
                avi_bg.setVisibility(View.GONE);
                Score = jsonObject.get("data").getAsInt();
                ShowTestScorePanel();
                break;
            case "203":
                testList = new ArrayList<>();
                JsonArray array1 = jsonObject.get("data").getAsJsonArray();
                for (JsonElement e : array1) {
                    testList.add(gson.fromJson(e, Test.class));
                }
                setHistoryList();
                break;
            case "202":
                try {
                    studentData = gson.fromJson(jsonObject.get("data"), Student.class);
                    if (studentData == null) {

                    } else {
                        DialogName.setText(studentData.getName());
                        DialogGrade.setText(studentData.getGrade());
                        DialogClass.setText(studentData.getClasses());
                    }
                } catch (Exception e) {

                }
                break;
            case "205":
                Toast.makeText(this, "成绩保存成功", Toast.LENGTH_SHORT).show();
                break;
            case "405":
            case "402":
            case "400":
            case "401":
            case "403":
                Toast.makeText(this, jsonObject.get("msg").getAsString(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /*网络请求失败*/
    @Override
    public void onError(Throwable ex) {
        /*Log.e("网络", ex.toString());*/
        System.out.print(ex);
        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
    }

    /*网络请求取消*/
    @Override
    public void onCancelled(Callback.CancelledException cex) {
    }

    /*网络请求完成*/
    @Override
    public void onFinished() {
    }

    //弹出对话框
    public void ShowTestScorePanel() {
        AlertDialog.Builder FormDialog = new AlertDialog.Builder(MainActivity.this);
        FormDialog.setTitle("批改完成!");
        FormDialog.setMessage("你的成绩是" + Score + "，是不是要录入成绩!");
        FormDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InputMessage();
            }
        });
        FormDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        FormDialog.show();
    }

    //获取学生信息
    public void GetStudentInfo(String s) {
        RequestParams requestParams = new RequestParams(URL.GetStudentByNumUrl);
        requestParams.addBodyParameter("num", s);
        URL.PostData(requestParams, this);
    }

    //弹出信息输入框
    public void InputMessage() {
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_style, null);
        DialogName = (EditText) view.findViewById(R.id.dialog_name);
        DialogNum = (EditText) view.findViewById(R.id.dialog_num);
        DialogGrade = (EditText) view.findViewById(R.id.dialog_grade);
        DialogClass = (EditText) view.findViewById(R.id.dialog_class);
        DialogPaper = (EditText) view.findViewById(R.id.dialog_paper);
        DialogNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
                GetStudentInfo(s.toString());
            }
        });
        inputDialog.setTitle("请输入学生信息");
        inputDialog.setView(view);
        inputDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(MainActivity.this, DialogName.getText(), Toast.LENGTH_SHORT).show();
                InsertTestInfo();
            }
        });
        inputDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        inputDialog.show();
    }

    public void InsertTestInfo() {
        Gson gson = new Gson();
        RequestParams requestParams = new RequestParams(URL.InsertTestInfoUrl);
        /*Test test=new Test();
        test.setSubject(LoginStatus.getTeacher().getSubject());
        test.setPaper_num(DialogPaper.getText().toString());
        test.setTeacher_name(LoginStatus.getTeacher().getName());
        test.setTeacher_num(LoginStatus.getTeacher().getNum());
        test.setStudent_num(studentData.getNum());
        test.setStudent_name(studentData.getName());
        test.setScore(Score);
        test.setClasses(studentData.getClasses());
        test.setGrade(studentData.getGrade());
        requestParams.addBodyParameter("test",gson.toJson(test,Test.class));*/
        requestParams.addBodyParameter("subject", LoginStatus.getTeacher().getSubject());
        requestParams.addBodyParameter("paper_num", DialogPaper.getText().toString());
        requestParams.addBodyParameter("teacher_name", LoginStatus.getTeacher().getName());
        requestParams.addBodyParameter("teacher_num", LoginStatus.getTeacher().getNum());
        requestParams.addBodyParameter("student_num", studentData.getNum());
        requestParams.addBodyParameter("student_name", studentData.getName());
        requestParams.addBodyParameter("score", Score + "");
        requestParams.addBodyParameter("classes", studentData.getClasses());
        requestParams.addBodyParameter("grade", studentData.getGrade());
        URL.PostData(requestParams, this);
    }
}