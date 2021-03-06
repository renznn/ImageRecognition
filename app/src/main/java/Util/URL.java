package Util;


import org.xutils.*;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2017/12/3.
 */

public class URL {
    public static String url="http://192.168.1.7:9094/";
    /*public static String UpLoadImageUrl="http://192.168.1.101:9094/ReceiverDemo";*/
    public static String UpLoadImageUrl=url+"ReceiverDemo";
    public static String test=url+"test";

    public static String CheckLoginUrl=url+"CheckLogin";

    public static String GetGradeAndClassListUrl=url+"GetGradeAndClassList";

    public static String GetTestListByClassUrl=url+"GetTestListByClass";

    public static String GetStudentByNumUrl=url+"GetStudentByNum";

    public static String InsertTestInfoUrl=url+"InsertTestInfo";

    public static String UpdateTeacherInfoUrl=url+"UpdateTeacherInfo";


    public static void GetData(RequestParams RP , final HttpCallback hc)
    {
        Callback.Cancelable cancelable = x.http().get(RP, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                hc.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hc.onError(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                hc.onCancelled(cex);
            }
            @Override
            public void onFinished() {
                hc.onFinished();
            }
        });

    }

    public static void PostData(RequestParams RP ,final HttpCallback hc)
    {
        Callback.Cancelable cancelable = x.http().post(RP, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                hc.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hc.onError(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                hc.onError(cex);
            }
            @Override
            public void onFinished() {
                hc.onFinished();
            }
        });

    }
}
