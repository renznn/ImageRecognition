package model;

/**
 * Created by Administrator on 2018/3/25.
 */

public class LoginStatus {
    static Teacher teacher;

    public LoginStatus(Teacher teacher) {
        LoginStatus.teacher = teacher;
    }

    public static Teacher getTeacher() {
        return teacher;
    }

    public static void setTeacher(Teacher teacher) {
        LoginStatus.teacher = teacher;
    }
}
